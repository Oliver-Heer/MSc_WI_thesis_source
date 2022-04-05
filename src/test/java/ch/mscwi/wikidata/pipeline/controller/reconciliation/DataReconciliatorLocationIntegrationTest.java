package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.persistence.ILocationRepository;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataReconciliatorLocationIntegrationTest {

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private ILocationRepository locationRepo;

  @Test
  void reconcileLocation_FOUND() {
    LocationDTO bernhard = createLocationDTO(1L, "Bernhard Theater", ReconciliationState.NEW);
    LocationDTO winterthur = createLocationDTO(2L, "Theater Winterthur", ReconciliationState.NEW);

    List<LocationDTO> reconciledDTOs = reconciliator.reconcileLocations(List.of(bernhard, winterthur));
    LocationDTO reconciledBernhard = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledBernhard.getState());

    LocationDTO reconciledWinterthur = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.FOUND, reconciledWinterthur.getState());

    // From DB
    locationRepo.saveAllAndFlush(reconciledDTOs);
    List<LocationDTO> dtosFromDB = locationRepo.findAll();
    assertEquals(2, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().anyMatch(dto -> StringUtils.isNotBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileLocation_NOT_FOUND() {
    LocationDTO shouldNotExistDTO = createLocationDTO(1L, "ThisLocationShouldNotExist", ReconciliationState.NEW);

    List<LocationDTO> reconciledDTOs = reconciliator.reconcileLocations(List.of(shouldNotExistDTO));
    LocationDTO notFoundDTO = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    locationRepo.saveAllAndFlush(reconciledDTOs);
    List<LocationDTO> dtosFromDB = locationRepo.findAll();
    assertEquals(1, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.NOT_FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> StringUtils.isBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileLocation_partially_FOUND() {
    LocationDTO bernhard = createLocationDTO(1L, "Bernhard Theater", ReconciliationState.NEW);
    LocationDTO shouldNotExistDTO = createLocationDTO(2L, "ThisLocationShouldNotExist", ReconciliationState.NEW);

    List<LocationDTO> reconciledDTOs = reconciliator.reconcileLocations(List.of(bernhard, shouldNotExistDTO));
    LocationDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    LocationDTO notFoundDTO = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    locationRepo.saveAllAndFlush(reconciledDTOs);
    LocationDTO bernhardFromDB = locationRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, bernhardFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(bernhardFromDB.getWikidataUid()));

    LocationDTO notFoundFromDB = locationRepo.findById(2L).get();
    assertEquals(ReconciliationState.NOT_FOUND, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  @Test
  void reconcileLocation_only_reconcile_NEW() {
    LocationDTO bernhard = createLocationDTO(1L, "Bernhard Theater", ReconciliationState.NEW);
    LocationDTO winterthur = createLocationDTO(2L, "Theater Winterthur", ReconciliationState.IGNORE);

    List<LocationDTO> reconciledDTOs = reconciliator.reconcileLocations(List.of(bernhard, winterthur));
    LocationDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    LocationDTO reconciledBallett = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.IGNORE, reconciledBallett.getState());

    // From DB
    locationRepo.saveAllAndFlush(reconciledDTOs);
    LocationDTO bernhardFromDB = locationRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, bernhardFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(bernhardFromDB.getWikidataUid()));

    LocationDTO notFoundFromDB = locationRepo.findById(2L).get();
    assertEquals(ReconciliationState.IGNORE, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  private LocationDTO createLocationDTO(long originId, String name, ReconciliationState state) {
    LocationDTO dto = new LocationDTO();
    dto.setId(originId);
    dto.setName(name);
    dto.setState(state);
    return dto;
  }

}

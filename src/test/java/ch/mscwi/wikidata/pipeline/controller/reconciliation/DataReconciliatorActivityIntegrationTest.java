package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.persistence.IActivityRepository;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataReconciliatorActivityIntegrationTest {

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private IActivityRepository activityRepo;

  @Test
  void reconcileActivity_FOUND() {
    ActivityDTO wilhelm = createActivityDTO(1L, "Wilhelm Tell", "Theater von Peter Löffler", ReconciliationState.NEW);
    ActivityDTO prometheus = createActivityDTO(2L, "Prometheus", "Theater von Franz Wenzler", ReconciliationState.NEW);

    List<ActivityDTO> reconciledDTOs = reconciliator.reconcileActivities(List.of(wilhelm, prometheus));
    ActivityDTO reconciledWilhelm = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledWilhelm.getState());

    ActivityDTO reconciledPrometheus = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.FOUND, reconciledPrometheus.getState());

    // From DB
    activityRepo.saveAllAndFlush(reconciledDTOs);
    List<ActivityDTO> dtosFromDB = activityRepo.findAll();
    assertEquals(2, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().anyMatch(dto -> StringUtils.isNotBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileActivity_NOT_FOUND() {
    ActivityDTO shouldNotExistDTO = createActivityDTO(1L, "ThisActivityShouldNotExist", "ThisDoesNotMatter", ReconciliationState.NEW);

    List<ActivityDTO> reconciledDTOs = reconciliator.reconcileActivities(List.of(shouldNotExistDTO));
    ActivityDTO notFoundDTO = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    activityRepo.saveAllAndFlush(reconciledDTOs);
    List<ActivityDTO> dtosFromDB = activityRepo.findAll();
    assertEquals(1, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.NOT_FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> StringUtils.isBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileActivity_partially_FOUND() {
    ActivityDTO wilhelm = createActivityDTO(1L, "Wilhelm Tell", "Theater von Peter Löffler", ReconciliationState.NEW);
    ActivityDTO shouldNotExistDTO = createActivityDTO(2L, "ThisActivityShouldNotExist", "ThisDoesNotMatter", ReconciliationState.NEW);

    List<ActivityDTO> reconciledDTOs = reconciliator.reconcileActivities(List.of(wilhelm, shouldNotExistDTO));
    ActivityDTO reconciledWilhelm = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledWilhelm.getState());

    ActivityDTO notFoundDTO = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    activityRepo.saveAllAndFlush(reconciledDTOs);
    ActivityDTO wilhelmFromDB = activityRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, wilhelmFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(wilhelmFromDB.getWikidataUid()));

    ActivityDTO notFoundFromDB = activityRepo.findById(2L).get();
    assertEquals(ReconciliationState.NOT_FOUND, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  @Test
  void reconcileActivity_only_reconcile_NEW() {
    ActivityDTO wilhelm = createActivityDTO(1L, "Wilhelm Tell", "Theater von Peter Löffler", ReconciliationState.NEW);
    ActivityDTO prometheus = createActivityDTO(2L, "Prometheus", "Theater von Franz Wenzler", ReconciliationState.IGNORE);

    List<ActivityDTO> reconciledDTOs = reconciliator.reconcileActivities(List.of(wilhelm, prometheus));
    ActivityDTO reconciledWilhelm = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledWilhelm.getState());

    ActivityDTO reconciledPrometheus = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.IGNORE, reconciledPrometheus.getState());

    // From DB
    activityRepo.saveAllAndFlush(reconciledDTOs);
    ActivityDTO wilhelmFromDB = activityRepo.findById(1L).get();
    assertEquals(ReconciliationState.FOUND, wilhelmFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(wilhelmFromDB.getWikidataUid()));

    ActivityDTO notFoundFromDB = activityRepo.findById(2L).get();
    assertEquals(ReconciliationState.IGNORE, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  private ActivityDTO createActivityDTO(long originId, String title, String subTitle, ReconciliationState state) {
    ActivityDTO dto = new ActivityDTO();
    dto.setOriginId(originId);
    dto.setTitle(title);
    dto.setSubTitle(subTitle);
    dto.setState(state);
    return dto;
  }
}

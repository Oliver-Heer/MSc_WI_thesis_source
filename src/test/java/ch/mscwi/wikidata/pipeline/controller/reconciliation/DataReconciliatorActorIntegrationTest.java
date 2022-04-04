package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.persistence.IActorRepository;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataReconciliatorActorIntegrationTest {

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private IActorRepository actorRepo;

  @Test
  void reconcileActor_FOUND() {
    ActorDTO patrice = createActorDTO("Patrice Caurier", ReconciliationState.NEW);
    ActorDTO christian = createActorDTO("Christian Arseni", ReconciliationState.NEW);

    List<ActorDTO> reconciledDTOs = reconciliator.reconcileActors(List.of(patrice, christian));
    ActorDTO reconciledPatrice = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledPatrice.getState());

    ActorDTO reconciledChristian = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.FOUND, reconciledChristian.getState());

    // From DB
    actorRepo.saveAllAndFlush(reconciledDTOs);
    List<ActorDTO> dtosFromDB = actorRepo.findAll();
    assertEquals(2, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().anyMatch(dto -> StringUtils.isNotBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileActor_NOT_FOUND() {
    ActorDTO shouldNotExistDTO = createActorDTO("ThisActorShouldNotExist", ReconciliationState.NEW);

    List<ActorDTO> reconciledDTOs = reconciliator.reconcileActors(List.of(shouldNotExistDTO));
    ActorDTO notFoundDTO = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    actorRepo.saveAllAndFlush(reconciledDTOs);
    List<ActorDTO> dtosFromDB = actorRepo.findAll();
    assertEquals(1, dtosFromDB.size());
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> ReconciliationState.NOT_FOUND == dto.getState()));
    assertEquals(true, dtosFromDB.stream().allMatch(dto -> StringUtils.isBlank(dto.getWikidataUid())));
  }

  @Test
  void reconcileActor_partially_FOUND() {
    ActorDTO patrice = createActorDTO("Patrice Caurier", ReconciliationState.NEW);
    ActorDTO shouldNotExistDTO = createActorDTO("ThisActorShouldNotExist", ReconciliationState.NEW);

    List<ActorDTO> reconciledDTOs = reconciliator.reconcileActors(List.of(patrice, shouldNotExistDTO));
    ActorDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    ActorDTO notFoundDTO = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.NOT_FOUND, notFoundDTO.getState());

    // From DB
    actorRepo.saveAllAndFlush(reconciledDTOs);
    ActorDTO patriceFromDB = actorRepo.findByName("Patrice Caurier").get();
    assertEquals(ReconciliationState.FOUND, patriceFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(patriceFromDB.getWikidataUid()));

    ActorDTO notFoundFromDB = actorRepo.findByName("ThisActorShouldNotExist").get();
    assertEquals(ReconciliationState.NOT_FOUND, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  @Test
  void reconcileActor_only_reconcile_NEW() {
    ActorDTO patrice = new ActorDTO();
    patrice.setName("Patrice Caurier");
    patrice.setState(ReconciliationState.NEW);

    ActorDTO christian = new ActorDTO();
    christian.setName("Christian Arseni");
    christian.setState(ReconciliationState.IGNORE);

    List<ActorDTO> reconciledDTOs = reconciliator.reconcileActors(List.of(patrice, christian));
    ActorDTO reconciledOper = reconciledDTOs.get(0);
    assertEquals(ReconciliationState.FOUND, reconciledOper.getState());

    ActorDTO reconciledBallett = reconciledDTOs.get(1);
    assertEquals(ReconciliationState.IGNORE, reconciledBallett.getState());

    // From DB
    actorRepo.saveAllAndFlush(reconciledDTOs);
    ActorDTO patriceFromDB = actorRepo.findByName("Patrice Caurier").get();
    assertEquals(ReconciliationState.FOUND, patriceFromDB.getState());
    assertEquals(true, StringUtils.isNotBlank(patriceFromDB.getWikidataUid()));

    ActorDTO notFoundFromDB = actorRepo.findByName("Christian Arseni").get();
    assertEquals(ReconciliationState.IGNORE, notFoundFromDB.getState());
    assertEquals(true, StringUtils.isBlank(notFoundFromDB.getWikidataUid()));
  }

  private ActorDTO createActorDTO(String name, ReconciliationState state) {
    ActorDTO dto = new ActorDTO();
    dto.setName(name);
    dto.setState(state);
    return dto;
  }

}

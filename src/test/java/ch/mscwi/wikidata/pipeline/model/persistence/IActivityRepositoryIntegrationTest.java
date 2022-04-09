package ch.mscwi.wikidata.pipeline.model.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTOBuilder;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class IActivityRepositoryIntegrationTest {

  @Autowired
  private IActivityRepository activityRepo;

  @Test
  void findByState() {
    ActivityDTO activity1 = new ActivityDTOBuilder()
        .withOriginId(1L)
        .withState(ReconciliationState.NEW)
        .build();

    ActivityDTO activity2 = new ActivityDTOBuilder()
        .withOriginId(2L)
        .withState(ReconciliationState.FOUND)
        .build();

    activityRepo.saveAll(List.of(activity1, activity2));

    List<ActivityDTO> findNEW = activityRepo.findAllByStateIn(Set.of(ReconciliationState.NEW));
    assertEquals(1, findNEW.size());

    List<ActivityDTO> findBoth = activityRepo.findAllByStateIn(Set.of(ReconciliationState.NEW, ReconciliationState.FOUND));
    assertEquals(2, findBoth.size());
  }

}

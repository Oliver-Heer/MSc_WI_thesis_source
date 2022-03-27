package ch.mscwi.wikidata.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.persistence.ActivityDTO;
import ch.mscwi.wikidata.pipeline.persistence.IActivityRepository;

@SpringBootTest
public class ReactorIntegrationTest {

  @Autowired
  private Reactor reactor;

  @Autowired
  private IActivityRepository activityRepo;

  @Test
  void persistAndLoadActivityTest() {
    String xml = TestUtils.getTestResource(TestUtils.TEST_XML_1);
    reactor.procure(xml);

    Activity activityToPersist = reactor.activities.get(17);
    long originId = activityToPersist.originId;

    assertEquals(originId, activityToPersist.originId);
    assertEquals("Don Giovanni", activityToPersist.activityDetail.title);
    assertEquals("Oper von Wolfgang Amadeus Mozart", activityToPersist.activityDetail.subTitle);
    assertEquals("Don Giovanni", activityToPersist.activityDetailEnglish.title);
    assertEquals("Opera by Wolfgang Amadeus Mozart", activityToPersist.activityDetailEnglish.subTitle);

    // TODO
    // Location Name
    // Genres
    // Actors

    ActivityDTO dtoFromDB = activityRepo.findById(originId).get();
    assertEquals(originId, dtoFromDB.getOriginId());
    assertEquals("Don Giovanni", dtoFromDB.getTitle());
    assertEquals("Oper von Wolfgang Amadeus Mozart", dtoFromDB.getSubTitle());
    assertEquals("Don Giovanni", dtoFromDB.getTitleEn());
    assertEquals("Opera by Wolfgang Amadeus Mozart", dtoFromDB.getSubTitleEn());
    assertEquals("Test-Organizer", dtoFromDB.getOrganizer());
  }
}


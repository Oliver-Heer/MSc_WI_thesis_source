package ch.mscwi.wikidata.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.persistence.ActivityDTO;
import ch.mscwi.wikidata.pipeline.persistence.ActivityDTOBuilder;
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
    reactor.procure(xml, "Zurich Opera");

    Activity activity = reactor.activities.get(17);
    long originId = activity.originId;

    ActivityDTO dtoToPersist = ActivityDTOBuilder.toActivityDTO(activity);
    assertEquals(originId, dtoToPersist.getOriginId());
    assertEquals("Don Giovanni", dtoToPersist.getTitle());
    assertEquals("Oper von Wolfgang Amadeus Mozart", dtoToPersist.getSubTitle());
    assertEquals("Don Giovanni", dtoToPersist.getTitleEn());
    assertEquals("Opera by Wolfgang Amadeus Mozart", dtoToPersist.getSubTitleEn());

    activityRepo.save(dtoToPersist);

    ActivityDTO dtoFromDB = activityRepo.findById(originId).get();
    assertEquals(originId, dtoFromDB.getOriginId());
    assertEquals("Don Giovanni", dtoFromDB.getTitle());
    assertEquals("Oper von Wolfgang Amadeus Mozart", dtoFromDB.getSubTitle());
    assertEquals("Don Giovanni", dtoFromDB.getTitleEn());
    assertEquals("Opera by Wolfgang Amadeus Mozart", dtoFromDB.getSubTitleEn());
  }
}


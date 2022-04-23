package ch.mscwi.wikidata.pipeline.controller.publication;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataPublicatorIntegrationTest {

  @Autowired
  private DataPublicatorBot publicatorBot;

  @Test
  @Disabled
  void publishLocation() throws Exception {
    LocationDTO location = new LocationDTO();
    location.setName("Spiegelsaal");
    location.setState(ReconciliationState.CREATE);

    publicatorBot.publishNewLocation(location);
  }


  @Test
  @Disabled
  void publishActor() throws Exception {
    ActorDTO actor = new ActorDTO();
    actor.setName("Peter Pan");
    actor.setState(ReconciliationState.CREATE);

    publicatorBot.publishNewActor(actor);
  }

}

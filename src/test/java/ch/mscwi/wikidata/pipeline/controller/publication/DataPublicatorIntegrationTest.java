package ch.mscwi.wikidata.pipeline.controller.publication;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
@Transactional
public class DataPublicatorIntegrationTest {

  @Autowired
  private DataPublicatorBot publicatorBot;

  @Test
  @Disabled
  void doStuff() throws Exception {
    LocationDTO location = new LocationDTO();
    location.setName("Spiegelsaal");
    location.setState(ReconciliationState.CREATE);

    publicatorBot.publishNewLocation(location);
  }

}

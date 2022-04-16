package ch.mscwi.wikidata.pipeline.controller.publication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DataPublicatorIntegrationTest {

  @Autowired
  private DataPublicatorBot publicatorBot;

  @Test
  void doStuff() {
    publicatorBot.publishNewActor();
    // TODO
  }

}

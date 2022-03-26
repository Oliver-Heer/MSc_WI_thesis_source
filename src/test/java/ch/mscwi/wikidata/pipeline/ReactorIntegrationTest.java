package ch.mscwi.wikidata.pipeline;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

@SpringBootTest
public class ReactorIntegrationTest {

  @Autowired
  private Reactor reactor;

  @Test
  void procureTest() {
    String xml = TestUtils.getTestResource(TestUtils.TEST_XML_1);
    reactor.procure(xml, "Zurich Opera");

  }
}


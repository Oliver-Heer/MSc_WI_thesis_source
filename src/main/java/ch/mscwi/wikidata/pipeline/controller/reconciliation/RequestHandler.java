package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Service
public class RequestHandler {

  @Autowired
  private ReconciliationProperties reconProperties;

  public ReconciliationResponse sendQuery(String query) {
    ResponseSpec retrieve = WebClient.create()
        .post()
        .uri(reconProperties.getService())
        .body(BodyInserters.fromFormData("queries", query))
        .retrieve();

    return retrieve.bodyToMono(ReconciliationResponse.class).block();
  }

}

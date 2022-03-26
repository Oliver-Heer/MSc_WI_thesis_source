package ch.mscwi.wikidata.pipeline.controller;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationQueryBuilder;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationResponse;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineClients;
import gmbh.dtap.refine.client.command.CreateProjectResponse;
import gmbh.dtap.refine.client.command.GetCsrfTokenResponse;
import gmbh.dtap.refine.client.command.RefineCommands;

@Service
public class DataReconciliator {

  public void reconcile() {
    String query = new ReconciliationQueryBuilder("Query")
        .withQuery("Donald Trump")
        .withType("Q5")
        .withLimit(1)
        .build();
    
    ResponseSpec retrieve = WebClient.create()
        .post()
        .uri("https://wikidata.reconci.link/de/api")
        .body(BodyInserters.fromFormData("queries", query))
        .retrieve();

    ReconciliationResponse response = retrieve.bodyToMono(ReconciliationResponse.class).block();
    System.err.println(response);
  }

  public static URL sendToOpenRefine(List<Activity> activities) throws Exception {

    try (RefineClient client = RefineClients.create("http://localhost:3333")) {

      File csvFile = DataPreparer.prepare(activities);

      GetCsrfTokenResponse tokenResponse = RefineCommands.getCsrfToken().build().execute(client);

      CreateProjectResponse createProjectResponse = RefineCommands
          .createProject()
          .token(tokenResponse.getToken())
          .name(csvFile.getName())
          .file(csvFile)
          .build()
          .execute(client);

      return createProjectResponse.getLocation();
    }

  }

}

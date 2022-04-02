package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import ch.mscwi.wikidata.pipeline.controller.preparation.OpenRefinePreparer;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineClients;
import gmbh.dtap.refine.client.command.CreateProjectResponse;
import gmbh.dtap.refine.client.command.GetCsrfTokenResponse;
import gmbh.dtap.refine.client.command.RefineCommands;

@Service
public class DataReconciliator {

  @Autowired
  private ReconciliationProperties reconProperties;

  public List<GenreDTO> reconcileGenre(List<GenreDTO> genreDTOs) {
    List<String> queries = genreDTOs.stream()
        .filter(dto -> ReconciliationState.NEW == dto.getState())
        .map(dto -> {
          return new ReconciliationQueryBuilder(String.valueOf(dto.getOriginId()))
              .withQuery(dto.getName())
              .withType(reconProperties.getGenreEntity())
              .build();
        })
        .collect(Collectors.toList());

    String batchQuery = ReconciliationQueryBuilder.toBatchQuery(queries);
    ReconciliationResponse response = sendQuery(batchQuery);

    genreDTOs.stream()
        .filter(dto -> ReconciliationState.NEW == dto.getState())
        .forEach(dto -> {
            String originId = String.valueOf(dto.getOriginId());
            String uid = response.getEntities().get(originId);
      
            // no reconciliation candidate found
            if (uid == null) {
              dto.setState(ReconciliationState.NOT_FOUND);
            }
            else {
              dto.setWikidataUid(uid);
              dto.setState(ReconciliationState.FOUND);
            }
        });

    return genreDTOs;
  }

  private ReconciliationResponse sendQuery(String query) {
    ResponseSpec retrieve = WebClient.create()
        .post()
        .uri(reconProperties.getService())
        .body(BodyInserters.fromFormData("queries", query))
        .retrieve();

    return retrieve.bodyToMono(ReconciliationResponse.class).block();
  }

  public static URL sendToOpenRefine(List<Activity> activities) throws Exception {

    try (RefineClient client = RefineClients.create("http://localhost:3333")) {

      File csvFile = OpenRefinePreparer.prepare(activities);

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

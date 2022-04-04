package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.controller.preparation.OpenRefinePreparer;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
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

  @Autowired
  private RequestHandler requestHandler;

  @SuppressWarnings("unchecked")
  public List<GenreDTO> reconcileGenres(List<GenreDTO> dtos) {
    List<String> queries = dtos.stream()
        .filter(dto -> ReconciliationState.NEW == dto.getState())
        .map(dto -> {
            return new ReconciliationQueryBuilder(dto.getStringID())
                .withQuery(dto.getName())
                .withType(reconProperties.getGenreEntity())
                .build();
        })
        .collect(Collectors.toList());

    return (List<GenreDTO>) reconcileBatch(dtos, queries);
  }

  private List<? extends AbstractWikidataDTO> reconcileBatch(List<? extends AbstractWikidataDTO> dtos, List<String> queries) {
    String batchQuery = ReconciliationQueryBuilder.toBatchQuery(queries);
    ReconciliationResponse response = requestHandler.sendQuery(batchQuery);

    dtos.stream()
        .filter(dto -> ReconciliationState.NEW == dto.getState())
        .forEach(dto -> {
            String uid = response.getEntities().get(dto.getStringID());

            if (uid == null) {
              dto.setState(ReconciliationState.NOT_FOUND);
            }
            else {
              dto.setWikidataUid(uid);
              dto.setState(ReconciliationState.FOUND);
            }
        });

    return dtos;
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

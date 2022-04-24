package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.controller.preparation.OpenRefinePreparer;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineClients;
import gmbh.dtap.refine.client.command.CreateProjectResponse;
import gmbh.dtap.refine.client.command.GetCsrfTokenResponse;
import gmbh.dtap.refine.client.command.RefineCommands;

@Service
public class DataReconciliator {

  private Logger logger = LoggerFactory.getLogger(DataReconciliator.class);

  @Autowired
  private ReconciliationProperties reconProperties;

  @Autowired
  private RequestHandler requestHandler;

  @SuppressWarnings("unchecked")
  public List<ActivityDTO> reconcileActivities(List<ActivityDTO> dtos) {
    logger.info("Reconciling " + dtos.size() + " new activities");

    Map<String, String> properties = reconProperties.getActivityProperties();

    return (List<ActivityDTO>) reconcileBatch(dtos, dto -> {
      ReconciliationQueryBuilder queryBuilder = new ReconciliationQueryBuilder(dto.getStringID())
          .withQuery(((ActivityDTO)dto).getTitle())
          .withType(reconProperties.getActivityEntity())
          .addProperty("P57", ((ActivityDTO)dto).getSubTitle());

      properties.forEach((key, value) -> queryBuilder.addProperty(key, value));

      ((ActivityDTO)dto).getGenres().stream()
          .forEach(genre -> {
              queryBuilder.addProperty("P136", genre.getName());
          });

      return queryBuilder.build();
    });
  }

  @SuppressWarnings("unchecked")
  public List<GenreDTO> reconcileGenres(List<GenreDTO> dtos) {
    logger.info("Reconciling " + dtos.size() + " new genres");

    return (List<GenreDTO>) reconcileBatch(dtos, dto -> {
      return new ReconciliationQueryBuilder(dto.getStringID())
          .withQuery(((GenreDTO)dto).getName())
          .withType(reconProperties.getGenreEntity())
          .build();
    });
  }

  @SuppressWarnings("unchecked")
  public List<LocationDTO> reconcileLocations(List<LocationDTO> dtos) {
    logger.info("Reconciling " + dtos.size() + " new locations");

    Map<String, String> properties = reconProperties.getLocationProperties();

    return (List<LocationDTO>) reconcileBatch(dtos, dto -> {
      ReconciliationQueryBuilder queryBuilder = new ReconciliationQueryBuilder(dto.getStringID())
          .withQuery(((LocationDTO)dto).getName())
          .withType(reconProperties.getLocationEntity());

      properties.forEach((key, value) -> queryBuilder.addProperty(key, value));

      return queryBuilder.build();
    });
  }

  @SuppressWarnings("unchecked")
  public List<ActorDTO> reconcileActors(List<ActorDTO> dtos) {
    logger.info("Reconciling " + dtos.size() + " new actors");

    Map<String, String> properties = reconProperties.getActorProperties();

    return (List<ActorDTO>) reconcileBatch(dtos, dto -> {
      ReconciliationQueryBuilder queryBuilder = new ReconciliationQueryBuilder(dto.getStringID())
          .withQuery(((ActorDTO)dto).getName())
          .withType(reconProperties.getActorEntity());

      properties.forEach((key, value) -> queryBuilder.addProperty(key, value));

      return queryBuilder.build();
    });
  }

  private List<? extends AbstractWikidataDTO> reconcileBatch(List<? extends AbstractWikidataDTO> dtos, Function<AbstractWikidataDTO, String> queryFunction) {
    List<String> queries = dtos.stream()
        .filter(dto -> ReconciliationState.NEW == dto.getState())
        .map(dto -> queryFunction.apply(dto))
        .collect(Collectors.toList());

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

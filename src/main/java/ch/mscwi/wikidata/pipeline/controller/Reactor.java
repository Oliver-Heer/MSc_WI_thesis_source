package ch.mscwi.wikidata.pipeline.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.controller.preparation.DataPreparer;
import ch.mscwi.wikidata.pipeline.controller.procurement.XmlProcurer;
import ch.mscwi.wikidata.pipeline.controller.publication.DataPublicatorBot;
import ch.mscwi.wikidata.pipeline.controller.reconciliation.DataReconciliator;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;
import ch.mscwi.wikidata.pipeline.model.persistence.DataPersistor;
import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@Service
@Scope("singleton")
public class Reactor {

  private Logger logger = LoggerFactory.getLogger(Reactor.class);

  @Autowired
  private XmlProcurer procurer;

  @Autowired
  private DataPreparer preparer;

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private DataPersistor persistor;

  @Autowired
  private DataPublicatorBot publicatorBot;

  private static final Set<ReconciliationState> RECONCILIATION_STATES = Set.of(
      ReconciliationState.FOUND,
      ReconciliationState.NOT_FOUND,
      ReconciliationState.ERROR
  );

  private static final Set<ReconciliationState> REFERENCE_READY_STATES = Set.of(
      ReconciliationState.APPROVED,
      ReconciliationState.IGNORE
  );

  public List<Activity> activities = new ArrayList<>();
  public List<URL> openRefineURLs = new ArrayList<>();

  @Scheduled(cron = "0 45 22 * * *")
  public void clearActivities() {
    logger.info("Activities cleared");
    activities = new ArrayList<>();
  }

  @Scheduled(cron = "0 0 23 * * *")
  public void procure() {
    procure("https://www.opernhaus.ch/xmlexport/kzexport.xml");
  }

  @Scheduled(cron = "0 15 23 * * *")
  public void reconcile() {
    Set<ReconciliationState> state = Set.of(ReconciliationState.NEW);

    // Genres
    List<GenreDTO> newGenres = persistor.getGenreRepo().findAllByStateIn(state);
    List<GenreDTO> reconciledGenres = reconciliator.reconcileGenres(newGenres);
    persistor.saveAllGenres(reconciledGenres);

    // Locations
    List<LocationDTO> newLocations = persistor.getLocationRepo().findAllByStateIn(state);
    List<LocationDTO> reconciledLocations = reconciliator.reconcileLocations(newLocations);
    persistor.saveAllLocations(reconciledLocations);

    // Actors
    List<ActorDTO> newActors = persistor.getActorRepo().findAllByStateIn(state);
    List<ActorDTO> reconciledActors = reconciliator.reconcileActors(newActors);
    persistor.saveAllActors(reconciledActors);

    // Activities
    List<ActivityDTO> newActivities = persistor.getActivityRepo().findAllByStateIn(state);
    List<ActivityDTO> reconciledActivities = reconciliator.reconcileActivities(newActivities);
    persistor.saveAllActivities(reconciledActivities);
  }

  public void procure(String url) {
    logger.info("Start procurement: " + url);
    try {
      ImportActivities procurement = procurer.procure(url);
      if (procurement == null) {
        return;
      }

      activities = new ArrayList<>();
      activities.addAll(procurement.activities);

      List<Activity> newActivities = procurement.activities.stream()
          .filter(activity -> !hasBeenProcured(activity.originId))
          .peek(activity -> logger.info("Procured new activity: " + activity.originId + " " + activity.activityDetail.title))
          .collect(Collectors.toList());

      List<ActivityDTO> activityDTOs = newActivities.stream()
          .map(activity -> preparer.toActivityDTO(activity))
          .collect(Collectors.toList());

      persistor.saveAllActivities(activityDTOs);

    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  private boolean hasBeenProcured(final long originId){
    // TODO should check date as well
    boolean inDB = persistor.getActivityRepo().existsById(originId);
    if (inDB) {
      logger.info("Encountered already persisted Entity, did not procure " + originId);
      return true;
    }

    return false;
  }

  public void sendToOpenRefine(String openRefineUrl) {
    logger.info("Send to OpenRefine " + openRefineUrl);
    try {
      URL openRefineURL = DataReconciliator.sendToOpenRefine(activities);
      this.openRefineURLs.add(openRefineURL);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public List<GenreDTO> getGenresForReconciliation() {
    return persistor.getGenreRepo().findAllByStateIn(RECONCILIATION_STATES);
  }

  public List<LocationDTO> getLocationsForReconciliation() {
    return persistor.getLocationRepo().findAllByStateIn(RECONCILIATION_STATES);
  }

  public List<ActorDTO> getActorsForReconciliation() {
    return persistor.getActorRepo().findAllByStateIn(RECONCILIATION_STATES);
  }

  public List<ActivityDTO> getActivitiesForPublication() {
    List<ActivityDTO> activities = persistor.getActivityRepo().findAllByStateIn(RECONCILIATION_STATES); // activity FOUND || NOT_FOUND || ERROR
    List<ActivityDTO> filteredActivities = activities.stream()
        .filter(activity -> referencesReadyForPublication(activity.getGenres())) // genres APPROVED || IGNORED
        .filter(activity -> referencesReadyForPublication(activity.getActors())) // actors APPROVED || IGNORED
        .filter(activity -> referencesReadyForPublication(List.of(activity.getLocation()))) // location APPROVED || IGNORED
        .collect(Collectors.toList());
    
    return filteredActivities;
  }

  private boolean referencesReadyForPublication(Collection<? extends AbstractWikidataDTO> dtos) {
    return dtos.stream()
        .allMatch(dto -> REFERENCE_READY_STATES.contains(dto.getState()));
  }

  public void saveActivity(ActivityDTO activityDTO) {
    persistor.saveAllActivities(List.of(activityDTO));
  }

  private boolean isApprovable(AbstractWikidataDTO dto) {
    boolean wikidataUidPresent = StringUtils.isNotBlank(dto.getWikidataUid());
    boolean startsWithQ = StringUtils.startsWith(dto.getWikidataUid(), "Q");
    return wikidataUidPresent && startsWithQ;
  }

  public void approveAndSaveGenre(GenreDTO genreDTO) {
    if (!isApprovable(genreDTO)) {
      logger.info("Could not approve Genre " + genreDTO.getOriginId() + " " + genreDTO.getName() + " Wikidata UID is missing or invalid");
    }

    logger.info("Approved Genre " + genreDTO.getOriginId() + " " + genreDTO.getName() + " with Wikidata UID " + genreDTO.getWikidataUid());
    genreDTO.setState(ReconciliationState.APPROVED);
    persistor.saveAllGenres(List.of(genreDTO));
  }

  public void ignoreAndSaveGenre(GenreDTO genreDTO) {
    logger.info("Ignore Genre " + genreDTO.getOriginId() + " " + genreDTO.getName());
    genreDTO.setState(ReconciliationState.IGNORE);
    persistor.saveAllGenres(List.of(genreDTO));
  }

  public void approveAndSaveLocation(LocationDTO locationDTO) {
    if (!isApprovable(locationDTO)) {
      logger.info("Could not approve Genre " + locationDTO.getId() + " " + locationDTO.getName() + " Wikidata UID is missing or invalid");
    }

    logger.info("Approved Location " + locationDTO.getId() + " " + locationDTO.getName() + " with Wikidata UID " + locationDTO.getWikidataUid());
    locationDTO.setState(ReconciliationState.APPROVED);
    persistor.saveAllLocations(List.of(locationDTO));
  }

  public void ignoreAndSaveLocation(LocationDTO locationDTO) {
    logger.info("Ignore Location " + locationDTO.getId() + " " + locationDTO.getName());
    locationDTO.setState(ReconciliationState.IGNORE);
    persistor.saveAllLocations(List.of(locationDTO));
  }

  public void approveAndSaveActor(ActorDTO actorDTO) {
    if (!isApprovable(actorDTO)) {
      logger.info("Could not approve Actor " + actorDTO.getName() + " Wikidata UID is missing or invalid");
    }

    logger.info("Approved Actor " + actorDTO.getName() + " with Wikidata UID " + actorDTO.getWikidataUid());
    actorDTO.setState(ReconciliationState.APPROVED);
    persistor.saveAllActors(List.of(actorDTO));
  }

  public void ignoreAndSaveActor(ActorDTO actorDTO) {
    logger.info("Ignore Actor " + actorDTO.getName());
    actorDTO.setState(ReconciliationState.IGNORE);
    persistor.saveAllActors(List.of(actorDTO));
  }

  public void approveAndSaveActivity(ActivityDTO activityDTO) {
    if (!isApprovable(activityDTO)) {
      logger.info("Could not approve Activity " + activityDTO.getTitle() + " Wikidata UID is missing or invalid");
    }

    logger.info("Approved Activity " + activityDTO.getTitle() + " with Wikidata UID " + activityDTO.getWikidataUid());
    activityDTO.setState(ReconciliationState.APPROVED);
    persistor.saveAllActivities(List.of(activityDTO));
  }

  public void ignoreAndSaveActivity(ActivityDTO activityDTO) {
    logger.info("Ignore Activity " + activityDTO.getTitle());
    activityDTO.setState(ReconciliationState.IGNORE);
    persistor.saveAllActivities(List.of(activityDTO));
  }

  public String createNewActivity(ActivityDTO activityDTO) {
    try {
      return publicatorBot.publishNewActivity(activityDTO);
    } catch (Exception e) {
      return null;
    }
  }

  public String createNewLocation(LocationDTO locationDTO) {
    try {
      return publicatorBot.publishNewLocation(locationDTO);
    } catch (Exception e) {
      return null;
    }
  }

  public String createNewActor(ActorDTO actorDTO) {
    try {
      return publicatorBot.publishNewActor(actorDTO);
    } catch (Exception e) {
      return null;
    }
  }
}

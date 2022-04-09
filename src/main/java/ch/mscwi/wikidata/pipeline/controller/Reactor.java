package ch.mscwi.wikidata.pipeline.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.controller.preparation.DataPreparer;
import ch.mscwi.wikidata.pipeline.controller.procurement.XmlProcurer;
import ch.mscwi.wikidata.pipeline.controller.reconciliation.DataReconciliator;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;
import ch.mscwi.wikidata.pipeline.model.persistence.DataPersistor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

@Service
@Scope("singleton")
public class Reactor {

  Logger logger = LoggerFactory.getLogger(Reactor.class);

  @Autowired
  private XmlProcurer procurer;

  @Autowired
  private DataPreparer preparer;

  @Autowired
  private DataReconciliator reconciliator;

  @Autowired
  private DataPersistor persistor;

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
    // Flag isReconciling?
//    reconciliator.reconcile();
  }

  public void procure(String url) {
    logger.info("Start procurement: " + url);
    try {
      ImportActivities procurement = procurer.procure(url);
      if (procurement == null) {
        return;
      }

      List<Activity> newActivities = procurement.activities.stream()
          .filter(activity -> !hasBeenProcured(activity.originId))
          .peek(activity -> logger.info("Procured new activity: " + activity.originId + " " + activity.activityDetail.title))
          .collect(Collectors.toList());

      activities.addAll(newActivities);

      List<ActivityDTO> activityDTOs = newActivities.stream()
          .map(activity -> preparer.toActivityDTO(activity))
          .collect(Collectors.toList());

      persistor.saveAll(activityDTOs);

    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
  }

  private boolean hasBeenProcured(final long originId){
    boolean inCurrentActivityList = activities.stream().anyMatch(activity -> activity.originId == originId);
    if (inCurrentActivityList) {
      return true;
    }

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
      // TODO
      e.printStackTrace();
    }
  }

}

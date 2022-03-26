package ch.mscwi.wikidata.pipeline.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.controller.procurement.XmlProcurer;
import ch.mscwi.wikidata.pipeline.controller.reconciliation.DataReconciliator;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;

@Service
@Scope("singleton")
public class Reactor {

  @Autowired
  private DataReconciliator reconciliator;

  public List<Activity> activities = new ArrayList<>();
  public List<URL> openRefineURLs = new ArrayList<>();

  private Reactor() { /* Singleton */ }

  @Scheduled(cron = "0 0 23 * * *")
  private void procure() {
    procure("https://www.opernhaus.ch/xmlexport/kzexport.xml", "Zurich Opera");
  }

  public void procure(String url, String organizer) {
    try {
      ImportActivities procurement = XmlProcurer.procure(url);

      if (procurement != null) {
        List<Activity> newActivities = procurement.activities.stream()
            .filter(activity -> !hasBeenProcured(activity.originId))
            .collect(Collectors.toList());

        newActivities.forEach(activity -> activity.organizer = organizer);

        activities.addAll(newActivities);
      }

    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
  }

  private boolean hasBeenProcured(final long originId){
    return activities.stream().anyMatch(activity -> activity.originId == originId);
  }

  @Scheduled(cron = "0 15 23 * * SUN")
  public void reconcile() {
    // Flag isReconciling?
    reconciliator.reconcile();
  }

  public void sendToOpenRefine(String openRefineUrl) {
    try {
      URL openRefineURL = DataReconciliator.sendToOpenRefine(activities);
      this.openRefineURLs.add(openRefineURL);
    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
  }

}

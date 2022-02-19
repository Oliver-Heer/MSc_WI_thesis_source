package ch.mscwi.wikidata.pipeline.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;
import ch.mscwi.wikidata.pipeline.model.wikidata.PerformanceWork;

public class Reactor {

  public List<Activity> activities = new ArrayList<>();
  public List<PerformanceWork> performanceWorks = new ArrayList<>();

  private static final Reactor reactor = new Reactor();

  private Reactor() { /* Singleton */ }

  public static Reactor getReactor() {
    return reactor;
  }

  public void procure(String url) {
    try {
      ImportActivities procurement = XmlProcurer.procure(url);

      if (procurement != null) {
        List<Activity> newActivities = procurement.activities.stream()
            .filter(activity -> !hasBeenProcured(activity.originId))
            .collect(Collectors.toList());
        activities.addAll(newActivities);

        prepare();
      }

    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
  }

  private boolean hasBeenProcured(final long originId){
    return activities.stream().anyMatch(activity -> activity.originId == originId);
  }

  public void prepare() {
    activities.stream()
        .filter(activity -> !activity.inPreparationStep)
        .forEach(activity -> {
          performanceWorks.add(DataPreparer.toPerformanceWork(activity));
          activity.inPreparationStep = true;
        });
  }

}

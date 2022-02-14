package ch.mscwi.wikidata.pipeline.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.mscwi.wikidata.pipeline.model.Activity;
import ch.mscwi.wikidata.pipeline.model.ImportActivities;

public class Reactor {

  public List<Activity> activities = new ArrayList<>();

  private static final Reactor reactor = new Reactor();

  private Reactor() { /* Singleton */ }

  public static Reactor getReactor() {
    return reactor;
  }

  public List<Activity> procure(String url) {
    try {
      ImportActivities procurement = XmlProcurer.procure(url);

      if (procurement != null) {
        List<Activity> newActivities = procurement.activities.stream()
            .filter(activity -> !hasBeenProcured(activity.originId))
            .collect(Collectors.toList());
        activities.addAll(newActivities);
      }

    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
    return activities;
  }

  public boolean hasBeenProcured(final long originId){
    return activities.stream().anyMatch(activity -> activity.originId == originId);
  }

}

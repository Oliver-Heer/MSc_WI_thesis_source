package ch.mscwi.wikidata.pipeline.controller;

import java.util.StringJoiner;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.wikidata.Performance;
import ch.mscwi.wikidata.pipeline.model.wikidata.PerformanceWork;
import ch.mscwi.wikidata.pipeline.model.wikidata.PerformingArtsProduction;

public class DataPreparer {

  public static PerformanceWork toPerformanceWork(Activity activity) {
    if (activity == null) {
      return null;
    }

    PerformingArtsProduction performingArtsProduction = createPerformingArtsProduction(activity);
    PerformanceWork performanceWork = createPerformanceWork(activity, performingArtsProduction);
    activity.activityDates.stream()
        .map(activityDate -> createPerformance(activityDate))
        .forEach(performance -> performingArtsProduction.withPerformance(performance));

    return performanceWork;
  }

  private static PerformanceWork createPerformanceWork(Activity activity, PerformingArtsProduction performingArtsProduction) {
    return (PerformanceWork) new PerformanceWork(performingArtsProduction, activity.activityDetail.title)
        .withProperty("wdt:P31/wdt:P279*", "instance of/subclass of", "wd:Q17538722")
        .withProperty("rdfs:label", "label", activity.activityDetail.title);
  }

  private static PerformingArtsProduction createPerformingArtsProduction(Activity activity) {
    StringJoiner genreJoiner = new StringJoiner(";");
    activity.activitySettings.genres.forEach(genre -> genreJoiner.add(genre.name));

    return (PerformingArtsProduction) new PerformingArtsProduction()
        .withProperty("P664", "organizer", "Opernhaus Zürich")
        .withProperty("P276", "location", activity.activityDetail.location.name)
        .withProperty("P136", "genre", genreJoiner.toString());
  }

  private static Performance createPerformance(ActivityDate activityDate) {
    return (Performance) new Performance()
        .withProperty("P585", "point in time", String.valueOf(activityDate.startDate))
        .withProperty("P580", "start time", activityDate.startTime)
        .withProperty("P582", "end time", activityDate.endTime);
  }

}

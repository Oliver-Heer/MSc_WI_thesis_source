package ch.mscwi.wikidata.pipeline.controller.preparation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;

public class PreparationUtils {

  public static Set<Cast> consolidateCast(List<ActivityDate> activityDates) {
    Set<Cast> consolidatedCast = new HashSet<>();
    activityDates.stream().forEach(date -> consolidatedCast.addAll(date.activityCast));
    return consolidatedCast;
  }

}

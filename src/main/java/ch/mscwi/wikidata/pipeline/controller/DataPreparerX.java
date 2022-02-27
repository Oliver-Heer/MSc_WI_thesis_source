package ch.mscwi.wikidata.pipeline.controller;

import java.util.StringJoiner;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.IWikidataObject;
import ch.mscwi.wikidata.pipeline.model.wikidata.WikidataObject;
import ch.mscwi.wikidata.pipeline.model.wikidata.WikidataObject.WikidataObjectBuilder;

public class DataPreparer {

  public static IWikidataObject toWork(Activity activity) {
    if (activity == null) {
      return null;
    }

    StringJoiner genreJoiner = new StringJoiner(";");
    activity.activitySettings.genres.forEach(genre -> genreJoiner.add(genre.name));

    WikidataObject performingArtsProduction = new WikidataObjectBuilder("Q43099500")
        .withRDFSLabel(activity.activityDetail.title, activity.activityDetail.languageCode)
        .withSchemaDescription(activity.activityDetail.shortDescription, activity.activityDetail.languageCode)
        .withProperty("P664", "organizer", "Opernhaus Zürich")
        .withProperty("P276", "location", activity.activityDetail.location.name)
        .withProperty("P136", "genre", genreJoiner.toString())
        .build();

    WikidataObject work = new WikidataObjectBuilder("Q386724")
        .withRDFSLabel(activity.activityDetail.title, activity.activityDetail.languageCode)
        .withProperty("wdt:P31/wdt:P279*", "instance of/subclass of", "wd:Q17538722")
        .withChild(performingArtsProduction)
        .build();

    return work;
  }

}

package ch.mscwi.wikidata.pipeline.controller.publication;

import java.util.List;

public class WikidataEntity {

  public static final String PROPERTY_INSTANCE_OF = "P31";
  public static final String PROPERTY_COUNTRY = "P17";
  public static final String PROPERTY_OCCUPATION = "P106";

  public static final String ENTITY_VENUE = "Q17350442";
  public static final String ENTITY_SWITZERLAND = "Q39";
  public static final String ENTITY_HUMAN = "Q5";
  public static final String ENTITY_ARTIST = "Q483501";

  public static List<String> forLocation() {
    return List.of(
        PROPERTY_INSTANCE_OF,
        PROPERTY_COUNTRY,
        ENTITY_VENUE,
        ENTITY_SWITZERLAND
    );
  }

  public static List<String> forActor() {
    return List.of(
        PROPERTY_INSTANCE_OF,
        PROPERTY_OCCUPATION,
        ENTITY_HUMAN,
        ENTITY_ARTIST
    );
  }

}

package ch.mscwi.wikidata.pipeline.controller.publication;

import java.util.List;

public class WikidataEntity {

  public static final String PROPERTY_INSTANCE_OF = "P31";
  public static final String PROPERTY_COUNTRY = "P17";
  public static final String PROPERTY_OCCUPATION = "P106";
  public static final String PROPERTY_PRODUCTION_COMPANY = "P272";
  public static final String PROPERTY_LOCATION = "P276";
  public static final String PROPERTY_TITLE = "P1476";
  public static final String PROPERTY_SUBTITLE = "P1680";
  public static final String PROPERTY_CAST_MEMBER = "P161";
  public static final String PROPERTY_GENRE = "P136";

  public static final String ENTITY_VENUE = "Q17350442";
  public static final String ENTITY_SWITZERLAND = "Q39";
  public static final String ENTITY_HUMAN = "Q5";
  public static final String ENTITY_ARTIST = "Q483501";
  public static final String ENTITY_PERFORMING_ARTS_PRODUCTION = "Q43099500";
  public static final String ENTITY_ZURICH_OPERA = "Q15278528";

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

  public static List<String> forActivity() {
    return List.of(
        PROPERTY_INSTANCE_OF,
        PROPERTY_PRODUCTION_COMPANY,
        PROPERTY_LOCATION,
        PROPERTY_TITLE,
        PROPERTY_SUBTITLE,
        PROPERTY_CAST_MEMBER,
        PROPERTY_GENRE,
        ENTITY_PERFORMING_ARTS_PRODUCTION,
        ENTITY_ZURICH_OPERA
    );
  }

}

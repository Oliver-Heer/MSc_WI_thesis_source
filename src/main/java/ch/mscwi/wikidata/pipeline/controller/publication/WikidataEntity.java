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
  public static final String PROPERTY_REFERENCE_URL = "P854";

  public static final String ENTITY_VENUE = "Q17350442";
  public static final String ENTITY_SWITZERLAND = "Q39";
  public static final String ENTITY_HUMAN = "Q5";
  public static final String ENTITY_ARTIST = "Q483501";
  public static final String ENTITY_PERFORMING_ARTS_PRODUCTION = "Q43099500";
  public static final String ENTITY_ZURICH_OPERA = "Q15278528";

  public static final String PROPERTY_HAS_ROLE = "P3831";
  public static final String ENTITY_STAGING = "Q3508687"; // Inszenierung
  public static final String ENTITY_CONDUCTOR = "Q158852"; // Musikalische Leitung, Dirigent
  public static final String ENTITY_SCENOGRAPHER = "Q2707485"; // Bühnenbild, Bühnenbildmitarbeit, Szenische Einrichtung, "Künstlerische Mitarbeit Bühnenbild"
  public static final String ENTITY_COSTUME_DESIGNER = "Q1323191"; // Kostüme, Kostümmitarbeit
  public static final String ENTITY_LIGHTING_DESIGNER = "Q1823479"; // Lichtgestaltung
  public static final String ENTITY_DRAMATURGE = "Q487596"; // Dramaturgie
  public static final String ENTITY_CHOREOGRAPHER = "Q2490358"; // Choreografie
  public static final String ENTITY_MUSICIAN = "Q639669"; // Musik
  public static final String ENTITY_SOPRANO_SINGER = "Q98834068"; // Sopran

  public static List<String> forLocation() {
    return List.of(
        PROPERTY_INSTANCE_OF,
        PROPERTY_COUNTRY,
        PROPERTY_REFERENCE_URL,
        ENTITY_VENUE,
        ENTITY_SWITZERLAND
    );
  }

  public static List<String> forActor() {
    return List.of(
        PROPERTY_INSTANCE_OF,
        PROPERTY_OCCUPATION,
        PROPERTY_REFERENCE_URL,
        ENTITY_HUMAN,
        ENTITY_ARTIST,
        ENTITY_STAGING,
        ENTITY_CONDUCTOR,
        ENTITY_SCENOGRAPHER,
        ENTITY_COSTUME_DESIGNER,
        ENTITY_LIGHTING_DESIGNER,
        ENTITY_DRAMATURGE,
        ENTITY_CHOREOGRAPHER,
        ENTITY_MUSICIAN,
        ENTITY_SOPRANO_SINGER
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
        PROPERTY_REFERENCE_URL,
        PROPERTY_HAS_ROLE,
        ENTITY_PERFORMING_ARTS_PRODUCTION,
        ENTITY_ZURICH_OPERA
    );
  }

}

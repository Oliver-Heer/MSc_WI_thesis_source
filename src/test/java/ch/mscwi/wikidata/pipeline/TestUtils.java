package ch.mscwi.wikidata.pipeline;

import java.io.File;

public class TestUtils {

  public static final String TEST_XML_1 = "opernhaus_zürich_2021_12_25.xml";
  public static final String EMPTY_ACTIVITIES = "empty_activities.xml";
  public static final String EMPTY_ACTIVITY_DATES = "empty_activitydates.xml";
  public static final String EMPTY_ACTIVITY_CAST = "empty_activitycast.xml";

  public static String getTestResource(String resourceName) {
    String resource = "src/test/resources/xml/" + resourceName;
    return "file://" + new File(resource).getAbsolutePath();
  }

}

package ch.mscwi.wikidata.pipeline.controller.procurement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;

import ch.mscwi.wikidata.pipeline.TestUtils;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDetail;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ImportActivities;

public class XmlProcureUnitTest {

  @Test
  void procureTest() throws Exception {
    String xml = TestUtils.getTestResource(TestUtils.TEST_XML_1);
    ImportActivities importActivities = XmlProcurer.procure(xml);

    assertNotNull(importActivities);
    assertNotNull(importActivities.activities);
    
    // Activity
    assertEquals(19, importActivities.activities.size());
    Activity donGiovanni = importActivities.activities.get(17);
    assertEquals(7007, donGiovanni.originId);
    assertEquals("2021-12-24 22:20:44", donGiovanni.originLastUpdatedAt);

    // ActivityDetail
    ActivityDetail details = donGiovanni.activityDetail;
    assertEquals("Don Giovanni", details.title);
    assertEquals("Oper von Wolfgang Amadeus Mozart", details.subTitle);
    assertEquals(1050, details.location.id);
    assertEquals("Hauptbühne Opernhaus", details.location.name);

    // ActivityDetailEnglish
    ActivityDetail detailsEnglish = donGiovanni.activityDetailEnglish;
    assertEquals("Don Giovanni", detailsEnglish.title);
    assertEquals("Opera by Wolfgang Amadeus Mozart", detailsEnglish.subTitle);

    // ActivityDates
    assertEquals(6, donGiovanni.activityDates.size());
    ActivityDate date = donGiovanni.activityDates.get(0);
    assertEquals(91174, date.originId);
    assertEquals(new GregorianCalendar(2022, Calendar.JANUARY, 25).getTime(), date.startDate);
    assertEquals("19:30", date.startTime);
    assertEquals("22:45", date.endTime);

    // ActivityCast
    assertEquals(20, date.activityCast.size());
    Cast cast = date.activityCast.get(0);
    assertEquals("Jordan de Souza", cast.name);
    assertEquals("Musikalische Leitung", cast.role);
    assertEquals("Musikalische Leitung", cast.roleCategory);

    // ActivitySettings
    assertEquals(1, donGiovanni.activitySettings.genres.size());
    Genre genre = donGiovanni.activitySettings.genres.get(0);
    assertEquals(1040, genre.originId);
    assertEquals("Oper", genre.name);
  }

  @Test
  void procureTest_emptyActivities() throws Exception {
    String xml = TestUtils.getTestResource(TestUtils.EMPTY_ACTIVITIES);
    ImportActivities importActivities = XmlProcurer.procure(xml);

    assertNotNull(importActivities);
    assertNotNull(importActivities.activities);
    assertEquals(0, importActivities.activities.size());
  }

  @Test
  void procureTest_emptyActivityDates() throws Exception {
    String xml = TestUtils.getTestResource(TestUtils.EMPTY_ACTIVITY_DATES);
    ImportActivities importActivities = XmlProcurer.procure(xml);

    Activity activity = importActivities.activities.get(0);
    assertNotNull(activity.activityDates);
    assertEquals(0, activity.activityDates.size());
  }

  @Test
  void procureTest_emptyActivityCast() throws Exception {
    String xml = TestUtils.getTestResource(TestUtils.EMPTY_ACTIVITY_CAST);
    ImportActivities importActivities = XmlProcurer.procure(xml);

    Activity activity = importActivities.activities.get(0);
    ActivityDate activityDate = activity.activityDates.get(0);
    assertNotNull(activityDate.activityCast);
    assertEquals(0, activityDate.activityCast.size());
  }
}


package ch.mscwi.wikidata.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDetail;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;

@SpringBootTest
public class ReactorIntegrationTest {

  @Autowired
  private Reactor reactor;

  private String getTestResource(String resourceName) {
    String resource = "src/test/resources/xml/" + resourceName;
    return "file://" + new File(resource).getAbsolutePath();
  }

  @Test
  void procureTest() {
    String xml = getTestResource("opernhaus_zürich_2021_12_25.xml");
    reactor.procure(xml, "Zurich Opera");

    // Activity
    assertEquals(19, reactor.activities.size());
    Activity donGiovanni = reactor.activities.get(17);
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
}


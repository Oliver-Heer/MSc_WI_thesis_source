package ch.mscwi.wikidata.pipeline.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.persistence.IActivityRepository;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@SpringBootTest
public class ReactorIntegrationTest {

  @Autowired
  private Reactor reactor;

  @Autowired
  private IActivityRepository activityRepo;

  @Test
  void persistAndLoadActivityTest() {
    String xml = TestUtils.getTestResource(TestUtils.TEST_XML_1);
    reactor.procure(xml);

    Activity activityToPersist = reactor.activities.get(17);
    long originId = activityToPersist.originId;

    ActivityDTO dtoFromDB = activityRepo.findById(originId).get();

    assertEquals(originId, dtoFromDB.getOriginId());
    assertDetails(activityToPersist, dtoFromDB);
    assertGenres(activityToPersist, dtoFromDB);
    assertRoles(activityToPersist, dtoFromDB);
    assertActors(activityToPersist, dtoFromDB);
    assertEquals(ReconciliationState.NEW, dtoFromDB.getState());
  }

  private void assertDetails(Activity activityToPersist, ActivityDTO dtoFromDB) {
    // Activity from XML
    assertEquals("Don Giovanni", activityToPersist.activityDetail.title);
    assertEquals("Oper von Wolfgang Amadeus Mozart", activityToPersist.activityDetail.subTitle);
    assertEquals("Don Giovanni", activityToPersist.activityDetailEnglish.title);
    assertEquals("Opera by Wolfgang Amadeus Mozart", activityToPersist.activityDetailEnglish.subTitle);
    assertEquals("Hauptbühne Opernhaus", activityToPersist.activityDetail.location.name);

    // DTO from DB
    assertEquals("Don Giovanni", dtoFromDB.getTitle());
    assertEquals("Oper von Wolfgang Amadeus Mozart", dtoFromDB.getSubTitle());
    assertEquals("Don Giovanni", dtoFromDB.getTitleEn());
    assertEquals("Opera by Wolfgang Amadeus Mozart", dtoFromDB.getSubTitleEn());
    assertEquals("Test-Organizer", dtoFromDB.getOrganizer());
    assertEquals("Hauptbühne Opernhaus", dtoFromDB.getLocation().getName());
    assertEquals(ReconciliationState.NEW, dtoFromDB.getLocation().getState());
  }

  private void assertGenres(Activity activityToPersist, ActivityDTO dtoFromDB) {
    // Activity from XML
    assertEquals(1, activityToPersist.activitySettings.genres.size());
    assertEquals(1040L, activityToPersist.activitySettings.genres.get(0).originId);
    assertEquals("Oper", activityToPersist.activitySettings.genres.get(0).name);

    // DTO from DB
    assertEquals(1, dtoFromDB.getGenres().size());
    assertEquals(true, dtoFromDB.getGenres().stream().anyMatch(dto -> 1040L == dto.getOriginId()));
    assertEquals(true, dtoFromDB.getGenres().stream().anyMatch(dto -> StringUtils.equals("Oper", dto.getName())));
    assertEquals(true, dtoFromDB.getGenres().stream().allMatch(dto -> ReconciliationState.NEW == dto.getState()));
  }

  private void assertRoles(Activity activityToPersist, ActivityDTO dtoFromDB) {
    // Activity from XML
    List<Cast> activityCast = activityToPersist.activityDates.get(0).activityCast;
    assertEquals(20, activityCast.size());

    Cast cast = activityCast.get(0);
    assertEquals(22254L, cast.originId);
    assertEquals("Musikalische Leitung", cast.role);
    assertEquals("Musikalische Leitung", cast.roleCategory);

    // DTO from DB
    assertEquals(20, dtoFromDB.getRoles().size());
    assertEquals(true, dtoFromDB.getRoles().stream().anyMatch(dto -> 22254L == dto.getOriginId()));
    assertEquals(true, dtoFromDB.getRoles().stream().anyMatch(dto -> StringUtils.equals("Musikalische Leitung", dto.getRole())));
    assertEquals(true, dtoFromDB.getRoles().stream().anyMatch(dto -> StringUtils.equals("Musikalische Leitung", dto.getRoleCategory())));
    assertEquals(true, dtoFromDB.getRoles().stream().allMatch(dto -> ReconciliationState.NEW == dto.getState()));
  }

  private void assertActors(Activity activityToPersist, ActivityDTO dtoFromDB) {
    // Activity from XML
    List<Cast> activityCast = activityToPersist.activityDates.get(0).activityCast;
    assertEquals(20, activityCast.size());

    Cast cast = activityCast.get(0);
    assertEquals("Jordan de Souza", cast.name);

    // DTO from DB
    List<ActorDTO> actors = dtoFromDB.getRoles().stream().flatMap(role -> role.getActors().stream()).collect(Collectors.toList());
    assertEquals(17, actors.size());
    assertEquals(true, actors.stream().anyMatch(dto -> StringUtils.equals("Jordan de Souza", dto.getName())));
    assertEquals(true, actors.stream().anyMatch(dto -> !StringUtils.isBlank(dto.getName())));
    assertEquals(true, actors.stream().allMatch(dto -> ReconciliationState.NEW == dto.getState()));
  }

}


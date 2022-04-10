package ch.mscwi.wikidata.pipeline.model.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Location;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTOBuilder;

@SpringBootTest
@Transactional
public class DataPersistorIntegrationTest {

  @Autowired
  private IActivityRepository activityRepo;

  @Autowired
  private DataPersistor persistor;

  @Test
  void find() {
    ActivityDTO activityDTO = createActivityDTO(1L);
    persistor.saveAllActivities(List.of(activityDTO));

    ActivityDTO dtoFromDB = activityRepo.findById(1L).get();
    assertActivityDTO(activityDTO, dtoFromDB);

    List<ActivityDTO> findAll = activityRepo.findAll();
    assertEquals(1, findAll.size());
    assertActivityDTO(activityDTO, findAll.get(0));
  }

  @Test
  void persistEntityTwice() {
    ActivityDTO activityDTO = createActivityDTO(1L);
    persistor.saveAllActivities(List.of(activityDTO));

    ActivityDTO dtoFromDB = activityRepo.findById(1L).get();
    assertActivityDTO(activityDTO, dtoFromDB);

    activityDTO.setTitle("Different Title");
    persistor.saveAllActivities(List.of(activityDTO));

    ActivityDTO secondDtoFromDB = activityRepo.findById(1L).get();
    assertActivityDTO(dtoFromDB, secondDtoFromDB);
  }

  private void assertActivityDTO(ActivityDTO beforePersistence, ActivityDTO afterPersistence) {
    assertEquals(beforePersistence.getTitle(), afterPersistence.getTitle());
    assertEquals(beforePersistence.getSubTitle(), afterPersistence.getSubTitle());
    assertEquals(beforePersistence.getTitleEn(), afterPersistence.getTitleEn());
    assertEquals(beforePersistence.getSubTitleEn(), afterPersistence.getSubTitleEn());
    assertEquals(beforePersistence.getOrganizer(), afterPersistence.getOrganizer());

    assertEquals(beforePersistence.getLocation().getId(), afterPersistence.getLocation().getId());
    assertEquals(beforePersistence.getLocation().getName(), afterPersistence.getLocation().getName());

    assertEquals(beforePersistence.getGenres().size(), afterPersistence.getGenres().size());
    assertEquals(beforePersistence.getActors().size(), afterPersistence.getActors().size());
  }

  private ActivityDTO createActivityDTO(long originId) {
    return new ActivityDTOBuilder()
        .withOriginId(originId)
        .withTitle("Title")
        .withTitleEn("TitleEn")
        .withSubTitle("SubTitle")
        .withSubTitleEn("SubTitleEn")
        .withOrganizer("Organizer")
        .withLocation(createLocation(originId))
        .withGenres(createGenres())
        .withRolesAndActors(createCast())
        .build();
  }

  private List<Genre> createGenres() {
    List<Genre> genres = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      Genre genre = new Genre();
      genre.originId = i;
      genre.name = "Genre" + i;
      genres.add(genre);
    }

    return genres;
  }

  private Set<Cast> createCast() {
    Set<Cast> castSet = new HashSet<>();

    for (int i = 0; i < 5; i++) {
      Cast cast = new Cast();
      cast.originId = i;
      cast.name = "Name" + i;
      cast.role = "Role" + i;
      cast.roleCategory = "RoleCategory" + i;
    }

    return castSet;
  }

  private Location createLocation(long id) {
    Location location = new Location();
    location.id = id;
    location.name = "Location";
    return location;
  }
}

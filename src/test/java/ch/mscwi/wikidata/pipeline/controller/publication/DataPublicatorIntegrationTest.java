package ch.mscwi.wikidata.pipeline.controller.publication;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;

@SpringBootTest
@Transactional
public class DataPublicatorIntegrationTest {

  @Autowired
  private DataPublicatorBot publicatorBot;

  @Test
  @Disabled
  void publishLocation() throws Exception {
    LocationDTO location = new LocationDTO();
    location.setName("Spiegelsaal");
    location.setState(ReconciliationState.CREATE);

    publicatorBot.publishNewLocation(location);
  }

  @Test
  @Disabled
  void publishActor() throws Exception {
    ActorDTO actor = new ActorDTO();
    actor.setName("Peter Pan");
    actor.setState(ReconciliationState.CREATE);

    publicatorBot.publishNewActor(actor);
  }

  @Test
  @Disabled
  void publishActivity() throws Exception {
    ActivityDTO activity = createActivityDTO();

    publicatorBot.publishNewActivity(activity);
  }

  private ActivityDTO createActivityDTO() {
    LocationDTO locationDTO = new LocationDTO();
    locationDTO.setWikidataUid("Q1000006");

    GenreDTO genreDTO = new GenreDTO();
    genreDTO.setWikidataUid("Q1000002");
    genreDTO.setState(ReconciliationState.APPROVED);

    RoleDTO roleDTO1 = createRole("Q1000007");
    RoleDTO roleDTO2 = createRole("Q1000022");

    String titleSuffix = String.valueOf(new Date().getTime());
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setTitle("Title_" + titleSuffix);
    activityDTO.setTitleEn("TitleEn_" + titleSuffix);
    activityDTO.setSubTitle("Subtitle");
    activityDTO.setSubTitleEn("SubtitleEn");
    activityDTO.setOrganizer("Zurich Opera");
    activityDTO.setLocation(locationDTO);
    activityDTO.setGenres(Set.of(genreDTO));
    activityDTO.setRoles(Set.of(roleDTO1, roleDTO2));
    activityDTO.setState(ReconciliationState.CREATE);
    return activityDTO;
  }

  private RoleDTO createRole(String actorId) {
    ActorDTO actorDTO = new ActorDTO();
    actorDTO.setWikidataUid(actorId);
    actorDTO.setState(ReconciliationState.APPROVED);

    RoleDTO roleDTO = new RoleDTO();
    roleDTO.addActor(actorDTO);
    return roleDTO;
  }

}

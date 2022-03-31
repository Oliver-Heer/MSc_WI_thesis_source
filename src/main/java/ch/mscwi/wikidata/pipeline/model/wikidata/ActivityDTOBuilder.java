package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Location;

public class ActivityDTOBuilder {

  private long originId;
  private String wikidataUid;
  private String title;
  private String titleEn;
  private String subTitle;
  private String subTitleEn;
  private String organizer;
  private LocationDTO location;
  private ReconciliationState state;

  private Set<GenreDTO> genreDTOs;
  private Set<RoleDTO> roleDTOs;

  public ActivityDTOBuilder withOriginId(long originId) {
    this.originId = originId;
    return this;
  }

  public ActivityDTOBuilder withWikidataUid(String wikidataUid) {
    this.wikidataUid = wikidataUid;
    return this;
  }

  public ActivityDTOBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public ActivityDTOBuilder withTitleEn(String titleEn) {
    this.titleEn = titleEn;
    return this;
  }

  public ActivityDTOBuilder withSubTitle(String subTitle) {
    this.subTitle = subTitle;
    return this;
  }

  public ActivityDTOBuilder withSubTitleEn(String subTitleEn) {
    this.subTitleEn = subTitleEn;
    return this;
  }

  public ActivityDTOBuilder withOrganizer(String organizer) {
    this.organizer = organizer;
    return this;
  }

  public ActivityDTOBuilder withState(ReconciliationState state) {
    this.state = state;
    return this;
  }

  public ActivityDTOBuilder withLocation(Location location) {
    LocationDTO locationDTO = new LocationDTO();
    locationDTO.setId(location.id);
    locationDTO.setName(location.name);
    locationDTO.setState(ReconciliationState.NEW);
    this.location = locationDTO;
    return this;
  }

  public ActivityDTOBuilder withGenres(List<Genre> genres) {
    Set<GenreDTO> genreDTOs = genres.stream()
        .map(genre -> {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setOriginId(genre.originId);
            genreDTO.setName(genre.name);
            genreDTO.setState(ReconciliationState.NEW);
            return genreDTO;
        })
        .collect(Collectors.toSet());

    this.genreDTOs = genreDTOs;
    return this;
  }

  public ActivityDTOBuilder withRolesAndActors(Set<Cast> cast) {
    Set<RoleDTO> roleDTOs = cast.stream()
        .map(role -> {
          RoleDTO roleDTO = new RoleDTO();
          roleDTO.setOriginId(role.originId);
          roleDTO.setRole(role.role);
          roleDTO.setRoleCategory(role.roleCategory);
          roleDTO.setState(ReconciliationState.NEW);

          if (!StringUtils.isBlank(role.role)) {
            ActorDTO actorDTO = new ActorDTO();
            actorDTO.setName(role.name);
            actorDTO.setState(ReconciliationState.NEW);
            roleDTO.addActor(actorDTO);
          }

          return roleDTO;
        })
        .collect(Collectors.toSet());

    this.roleDTOs = roleDTOs;
    return this;
  }

  public ActivityDTO build() {
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setOriginId(originId);
    activityDTO.setWikidataUid(wikidataUid);
    activityDTO.setTitle(title);
    activityDTO.setSubTitle(subTitle);
    activityDTO.setTitleEn(titleEn);
    activityDTO.setSubTitleEn(subTitleEn);
    activityDTO.setOrganizer(organizer);
    activityDTO.setLocation(location);
    activityDTO.setState(state);
    activityDTO.setGenres(genreDTOs);
    activityDTO.setRoles(roleDTOs);
    return activityDTO;
  }

}

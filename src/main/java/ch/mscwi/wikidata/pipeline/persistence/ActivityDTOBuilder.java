package ch.mscwi.wikidata.pipeline.persistence;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

  private Set<GenreDTO> genreDTOs;
  private Set<ActorDTO> actorDTOs;

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

  public ActivityDTOBuilder withLocation(Location location) {
    LocationDTO locationDTO = new LocationDTO();
    locationDTO.setId(location.id);
    locationDTO.setName(location.name);
    this.location = locationDTO;
    return this;
  }

  public ActivityDTOBuilder withGenres(List<Genre> genres) {
    Set<GenreDTO> genreDTOs = genres.stream()
        .map(genre -> {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setOriginId(genre.originId);
            genreDTO.setName(genre.name);
            return genreDTO;
        })
        .collect(Collectors.toSet());

    this.genreDTOs = genreDTOs;
    return this;
  }

  public ActivityDTOBuilder withActors(Set<Cast> cast) {
    Set<ActorDTO> actorDTOs = cast.stream()
        .map(actor -> {
            ActorDTO actorDTO = new ActorDTO();
            actorDTO.setOriginId(actor.originId);
            actorDTO.setName(actor.name);
            actorDTO.setRole(actor.role);
            actorDTO.setRoleCategory(actor.roleCategory);
            return actorDTO;
        })
        .collect(Collectors.toSet());

    this.actorDTOs = actorDTOs;
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
    activityDTO.setGenres(genreDTOs);
    activityDTO.setActors(actorDTOs);
    return activityDTO;
  }

}

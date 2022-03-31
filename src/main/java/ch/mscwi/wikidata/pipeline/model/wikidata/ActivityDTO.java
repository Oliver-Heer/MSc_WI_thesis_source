package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Activity")
public class ActivityDTO extends AbstractWikidataDTO {

  @Id
  private long originId;
  private String title;
  private String titleEn;
  private String subTitle;
  private String subTitleEn;
  private String organizer;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private LocationDTO location;

  @ManyToMany(targetEntity = GenreDTO.class, cascade = CascadeType.PERSIST)
  private Collection<GenreDTO> genres = new HashSet<>();

  @ManyToMany(targetEntity = RoleDTO.class, cascade = CascadeType.PERSIST)
  private Collection<RoleDTO> roles = new HashSet<>();

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public String getSubTitleEn() {
    return subTitleEn;
  }

  public void setSubTitleEn(String subTitleEn) {
    this.subTitleEn = subTitleEn;
  }

  public String getOrganizer() {
    return organizer;
  }

  public void setOrganizer(String organizer) {
    this.organizer = organizer;
  }

  public LocationDTO getLocation() {
    return location;
  }

  public void setLocation(LocationDTO location) {
    this.location = location;
  }

  public Collection<GenreDTO> getGenres() {
    return genres;
  }

  public void setGenres(Set<GenreDTO> genres) {
    this.genres = genres;
  }

  public Collection<RoleDTO> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleDTO> roles) {
    this.roles = roles;
  }

  public List<ActorDTO> getActors() {
    return roles.stream().flatMap(role -> role.getActors().stream()).collect(Collectors.toList());
  }

}

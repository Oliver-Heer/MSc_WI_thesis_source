package ch.mscwi.wikidata.pipeline.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;

@Entity
@Table(name="Genre")
public class GenreDTO {

  @Id
  private long originId;
  private String wikidataUid;
  private String name;

  @ManyToMany(targetEntity = ActivityDTO.class, mappedBy = "genres", cascade = CascadeType.ALL)
  private Set<Activity> activities = new HashSet<>();

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getWikidataUid() {
    return wikidataUid;
  }

  public void setWikidataUid(String wikidataUid) {
    this.wikidataUid = wikidataUid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}

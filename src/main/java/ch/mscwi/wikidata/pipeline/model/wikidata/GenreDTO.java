package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Genre")
public class GenreDTO extends AbstractWikidataDTO {

  @Id
  private long originId;
  private String name;

  @ManyToMany(targetEntity = ActivityDTO.class, mappedBy = "genres")
  private Collection<ActivityDTO> activities = new HashSet<>();

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(Collection<ActivityDTO> activities) {
    this.activities = activities;
  }

}

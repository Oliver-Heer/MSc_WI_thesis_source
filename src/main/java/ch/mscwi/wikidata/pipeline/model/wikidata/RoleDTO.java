package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Role")
public class RoleDTO extends AbstractWikidataDTO {

  @Id
  private long originId;
  private String role;
  private String roleCategory;

  @ManyToMany(targetEntity = ActivityDTO.class, mappedBy = "roles")
  private Collection<ActivityDTO> activities = new HashSet<>();

  @ManyToMany(targetEntity = ActorDTO.class, cascade = CascadeType.PERSIST)
  private Collection<ActorDTO> actors = new HashSet<>();

  @Override
  public String getStringID() {
    return String.valueOf(this.originId);
  }

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getRoleCategory() {
    return roleCategory;
  }

  public void setRoleCategory(String roleCategory) {
    this.roleCategory = roleCategory;
  }

  public Collection<ActivityDTO> getActivities() {
    return activities;
  }

  public void setActivities(Collection<ActivityDTO> activities) {
    this.activities = activities;
  }

  public Collection<ActorDTO> getActors() {
    return actors;
  }

  public void addActor(ActorDTO actor) {
    this.actors.add(actor);
  }

}

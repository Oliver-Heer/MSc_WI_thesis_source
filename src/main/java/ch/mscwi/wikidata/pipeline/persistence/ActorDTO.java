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
@Table(name="Actor")
public class ActorDTO {

  @Id
  private long originId;
  private String name;
  private String role;
  private String roleCategory;

  @ManyToMany(targetEntity = ActivityDTO.class, mappedBy = "actors", cascade = CascadeType.ALL)
  private Set<Activity> activities = new HashSet<>();

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

}

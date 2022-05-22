package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Actor")
public class ActorDTO extends AbstractWikidataDTO {

  @Id
  private String name;

  @ManyToMany(targetEntity = RoleDTO.class, mappedBy = "actors")
  private Collection<RoleDTO> roles = new HashSet<>();

  @Override
  public String getStringID() {
    return name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<RoleDTO> getRoles() {
    return roles;
  }

}

package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

import com.github.jknack.handlebars.internal.lang3.StringUtils;

public class Cast implements IKulturZueriObject {

  @XmlAttribute
  public long originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String role;

  @XmlAttribute
  public String roleCategory;

  @XmlAttribute
  public long IsStarRole;

  @XmlAttribute
  public long sort;

  @Override
  public boolean equals(Object cast) {
    if (cast == this) {
      return true;
    }

    if (!(cast instanceof Cast)) {
      return false;
    }

    Cast other = (Cast)cast;
    boolean namesIdentical = StringUtils.equals(this.name, other.name);
    boolean roleIdentical = StringUtils.equals(this.role, other.role);
    boolean roleCategoryIdentical = StringUtils.equals(this.roleCategory, other.roleCategory);

    return (namesIdentical && roleIdentical && roleCategoryIdentical);
  }

  @Override
  public final int hashCode() {
      int result = 17;
      if (name != null) {
          result = 31 * result + name.hashCode();
      }

      if (role != null) {
          result = 31 * result + role.hashCode();
      }

      if (roleCategory != null) {
          result = 31 * result + roleCategory.hashCode();
      }

      return result;
  }
}

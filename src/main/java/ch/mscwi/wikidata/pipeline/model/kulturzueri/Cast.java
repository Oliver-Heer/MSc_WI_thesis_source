package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

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
}

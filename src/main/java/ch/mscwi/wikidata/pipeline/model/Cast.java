package ch.mscwi.wikidata.pipeline.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Cast {

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

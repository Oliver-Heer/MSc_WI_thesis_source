package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Cast {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String role;

  @XmlAttribute
  public String roleCategory;

  @XmlAttribute
  public int IsStarRole;

  @XmlAttribute
  public int sort;
}

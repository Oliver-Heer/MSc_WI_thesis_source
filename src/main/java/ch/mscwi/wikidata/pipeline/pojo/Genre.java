package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Genre {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public int branchId;
}

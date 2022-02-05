package ch.mscwi.wikidata.pipeline.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Genre {

  @XmlAttribute
  public long originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public long branchId;
}

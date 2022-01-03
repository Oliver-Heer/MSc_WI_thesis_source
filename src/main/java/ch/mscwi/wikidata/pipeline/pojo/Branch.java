package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Branch {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String name;
}

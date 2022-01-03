package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

  @XmlAttribute
  public int id;

  @XmlAttribute
  public String name;
}

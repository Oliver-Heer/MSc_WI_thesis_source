package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

  @XmlAttribute
  public long id;

  @XmlAttribute
  public String name;
}

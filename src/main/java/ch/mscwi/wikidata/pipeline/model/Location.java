package ch.mscwi.wikidata.pipeline.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

  @XmlAttribute
  public long id;

  @XmlAttribute
  public String name;
}

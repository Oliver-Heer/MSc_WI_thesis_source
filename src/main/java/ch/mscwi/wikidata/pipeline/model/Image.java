package ch.mscwi.wikidata.pipeline.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Image {

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String credits;

  @XmlAttribute
  public String url;
}

package ch.mscwi.wikidata.pipeline.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Source {

  @XmlAttribute
  public long Id;

  @XmlAttribute
  public String url;
}

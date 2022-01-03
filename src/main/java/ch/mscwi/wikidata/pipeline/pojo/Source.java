package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Source {

  @XmlAttribute
  public int Id;

  @XmlAttribute
  public String url;
}

package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;

public class Source {

  @XmlAttribute
  public long Id;

  @XmlAttribute
  public String url;
}

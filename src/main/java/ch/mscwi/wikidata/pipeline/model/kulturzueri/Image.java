package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

public class Image implements IKulturZueriObject {

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String credits;

  @XmlAttribute
  public String url;
}

package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

public class Video implements IKulturZueriObject {

  @XmlAttribute
  public String url;
}

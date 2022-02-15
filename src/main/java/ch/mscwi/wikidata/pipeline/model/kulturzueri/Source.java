package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

public class Source implements IKulturZueriObject {

  @XmlAttribute
  public long Id;

  @XmlAttribute
  public String url;
}

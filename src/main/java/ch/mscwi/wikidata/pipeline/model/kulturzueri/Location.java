package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

public class Location implements IKulturZueriObject {

  @XmlAttribute
  public long id;

  @XmlAttribute
  public String name;
}

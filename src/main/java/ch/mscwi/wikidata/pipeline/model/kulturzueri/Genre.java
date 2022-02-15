package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;

public class Genre implements IKulturZueriObject {

  @XmlAttribute
  public long originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public long branchId;
}

package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ActivityDetail implements IKulturZueriObject {

  @XmlAttribute
  public String languageCode;

  @XmlElement(name="Title")
  public String title;

  @XmlElement(name="SubTitle")
  public String subTitle;

  @XmlElement(name="ShortDescription")
  public String shortDescription;

  @XmlElement(name="LongDescription")
  public String longDescription;

  @XmlElement(name="OriginURL")
  public String originURL;

  @XmlElement(name="Location")
  public Location location = new Location();
}

package ch.mscwi.wikidata.pipeline.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ActivityDetailEnglish {

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

  @XmlElement(name="CastInformation")
  public Object castInformation;
}

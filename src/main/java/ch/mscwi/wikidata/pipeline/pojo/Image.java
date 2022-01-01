package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Image {

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String credits;

  @XmlAttribute
  public String url;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---Image---");
    joiner.add("name:" + name);
    joiner.add("credits: " + credits);
    joiner.add("url: " + url);

    return joiner.toString();
  }
}

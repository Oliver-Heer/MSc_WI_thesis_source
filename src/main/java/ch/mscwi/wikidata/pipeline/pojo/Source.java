package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Source {

  @XmlAttribute
  public int Id;

  @XmlAttribute
  public String url;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---Source---");
    joiner.add("Id: " + Id);
    joiner.add("url: " + url);

    return joiner.toString();
  }
}

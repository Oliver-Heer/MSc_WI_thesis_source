package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Branch {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String name;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---Branch---");
    joiner.add("originId: " + originId);
    joiner.add("name: " + name);

    return joiner.toString();
  }
}

package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Genre {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public int branchId;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---Genre---");
    joiner.add("originId: " + originId);
    joiner.add("name: " + name);
    joiner.add("branchId: " + branchId);

    return joiner.toString();
  }
}

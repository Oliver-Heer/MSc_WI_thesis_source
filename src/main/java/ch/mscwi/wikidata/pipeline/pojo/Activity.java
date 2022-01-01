package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Activity {

  @XmlAttribute
  public int ownerId;

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String originLastUpdatedAt;

  public ActivityDetail ActivityDetail;
  public ActivityDetailEnglish ActivityDetailEnglish;
  public ActivityDates ActivityDates;
  public ActivityMultimedia ActivityMultimedia;
  public ActivitySettings ActivitySettings;
  public String text;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("###Activity###");
    joiner.add("ownerId: " + ownerId);
    joiner.add("originId: " + originId);
    joiner.add("originLastUpdatedAt: " + originLastUpdatedAt);
    joiner.add("text: " + text);
    joiner.add(String.valueOf(ActivityDetail));
    joiner.add(String.valueOf(ActivityDetailEnglish));
    joiner.add(String.valueOf(ActivityDates));
    joiner.add(String.valueOf(ActivityMultimedia));
    joiner.add(String.valueOf(ActivitySettings));

    return joiner.toString();
  }
}

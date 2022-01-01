package ch.mscwi.wikidata.pipeline.pojo;

import java.util.Date;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class ActivityDate {

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public Date startDate;

  @XmlAttribute
  public String startTime;

  @XmlAttribute
  public String endTime;

  @XmlAttribute
  public String originLastUpdatedAt;

  public String text;
  public String Price;
  public String TicketURL;
  public ActivityCast ActivityCast;
  public ActivityDateSettings ActivityDateSettings;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---ActivityDate---");
    joiner.add("originId: " + originId);
    joiner.add("Date: " + startDate);
    joiner.add("startTime: " + startTime);
    joiner.add("endTime: " + endTime);
    joiner.add("originLastUpdatedAt: " + originLastUpdatedAt);
    joiner.add("text: " + text);
    joiner.add("Price: " + Price);
    joiner.add("TicketURL: " + TicketURL);
    joiner.add(String.valueOf(ActivityCast));
    joiner.add(String.valueOf(ActivityDateSettings));

    return joiner.toString();
  }
}

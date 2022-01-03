package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Activity {

  @XmlAttribute
  public int ownerId;

  @XmlAttribute
  public int originId;

  @XmlAttribute
  public String originLastUpdatedAt;

  @XmlElementWrapper(name="ActivityDates")
  @XmlElement(name="ActivityDate")
  List<ActivityDate> activityDates;

  @XmlElement(name="ActivityDetail")
  public ActivityDetail activityDetail;

  @XmlElement(name="ActivityDetailEnglish")
  public ActivityDetailEnglish activityDetailEnglish;

  @XmlElement(name="ActivityMultimedia")
  public ActivityMultimedia activityMultimedia;

  @XmlElement(name="ActivitySettings")
  public ActivitySettings activitySettings;

  public int getOriginId() {
    return originId;
  }

  public String getOriginLastUpdatedAt() {
    return originLastUpdatedAt;
  }

  public ActivityDetail getActivityDetail() {
    return activityDetail;
  }
}
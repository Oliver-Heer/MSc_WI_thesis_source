package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Activity implements IKulturZueriObject {

  @XmlAttribute
  public long ownerId;

  @XmlAttribute
  public long originId;

  @XmlAttribute
  public String originLastUpdatedAt;

  @XmlElementWrapper(name="ActivityDates")
  @XmlElement(name="ActivityDate")
  public List<ActivityDate> activityDates = new ArrayList<>();

  @XmlElement(name="ActivityDetail")
  public ActivityDetail activityDetail = new ActivityDetail();

  @XmlElement(name="ActivityDetailEnglish")
  public ActivityDetail activityDetailEnglish = new ActivityDetail();

  @XmlElement(name="ActivityMultimedia")
  public ActivityMultimedia activityMultimedia = new ActivityMultimedia();

  @XmlElement(name="ActivitySettings")
  public ActivitySettings activitySettings = new ActivitySettings();

  public boolean inPreparationStep;
}

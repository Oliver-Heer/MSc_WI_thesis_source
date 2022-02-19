package ch.mscwi.wikidata.pipeline.model.kulturzueri;

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
  public List<ActivityDate> activityDates;

  @XmlElement(name="ActivityDetail")
  public ActivityDetail activityDetail;

  @XmlElement(name="ActivityDetailEnglish")
  public ActivityDetail activityDetailEnglish;

  @XmlElement(name="ActivityMultimedia")
  public ActivityMultimedia activityMultimedia;

  @XmlElement(name="ActivitySettings")
  public ActivitySettings activitySettings;

  public boolean inPreparationStep;
}

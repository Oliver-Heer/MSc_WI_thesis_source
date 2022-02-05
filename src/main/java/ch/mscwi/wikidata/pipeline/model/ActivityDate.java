package ch.mscwi.wikidata.pipeline.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivityDate {

  @XmlAttribute
  public long originId;

  @XmlAttribute
  public Date startDate;

  @XmlAttribute
  public String startTime;

  @XmlAttribute
  public String endTime;

  @XmlAttribute
  public String originLastUpdatedAt;

  @XmlElement(name="Price")
  public String price;
  
  @XmlElement(name="TicketURL")
  public String ticketURL;

  @XmlElementWrapper(name="ActivityCast")
  @XmlElement(name="Cast")
  public List<Cast> activityCast;
}

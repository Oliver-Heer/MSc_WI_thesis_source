package ch.mscwi.wikidata.pipeline.pojo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "importActivities", namespace="https://www.opernhaus.ch")
public class ImportActivities {

  @XmlElement(name="ImportFileLastUpdate")
  public Date importFileLastUpdate;

  @XmlElement(name="StartExportDate")
  public Date startExportDate;

  @XmlElement(name="EndExportDate")
  public Date endExportDate;

  @XmlElement(name="Source")
  public Source source;

  @XmlElementWrapper(name="Activities")
  @XmlElement(name="Activity")
  public List<Activity> activities;
}
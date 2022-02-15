package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "kdz:importActivities")
public class ImportActivities implements IKulturZueriObject {

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

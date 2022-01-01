package ch.mscwi.wikidata.pipeline.pojo;

import java.util.Date;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "importActivities", namespace="https://www.opernhaus.ch")
public class ImportActivities {
  public Date ImportFileLastUpdate;
  public Date StartExportDate;
  public Date EndExportDate;
  public String text;
  public Source Source;
  public Activities Activities;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add("---ImportActivities---");
    joiner.add("ImportFileLastUpdate: " + ImportFileLastUpdate);
    joiner.add("StartExportDate: " + StartExportDate);
    joiner.add("EndExportDate: " + EndExportDate);
    joiner.add("text: " + text);
    joiner.add(String.valueOf(Source));
    joiner.add(String.valueOf(Activities));

    return joiner.toString();
  }
}

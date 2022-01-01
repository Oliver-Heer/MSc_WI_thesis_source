package ch.mscwi.wikidata.pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.mscwi.wikidata.pipeline.pojo.ImportActivities;

@SpringBootApplication
public class PipelineRunner {

  public static void main(String[] args) {
    SpringApplication.run(PipelineRunner.class, args);
  }

  public static String run() throws Exception {
    ImportActivities importActivities = XmlProcurer.procure("https://www.opernhaus.ch/xmlexport/kzexport.xml");
    return String.valueOf(importActivities);
  }

}

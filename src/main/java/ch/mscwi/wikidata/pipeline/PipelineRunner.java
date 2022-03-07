package ch.mscwi.wikidata.pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PipelineRunner {

  public static void main(String[] args) {
    SpringApplication.run(PipelineRunner.class, args);
  }

}

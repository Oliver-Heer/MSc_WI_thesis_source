package ch.mscwi.wikidata.pipeline;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.mscwi.wikidata.pipeline.pojo.Activity;
import ch.mscwi.wikidata.util.PipelineUtil;

@SpringBootApplication
public class PipelineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PipelineRunner.class, args);
    }
	
	public static String run() throws Exception {
		PipelineOptions options = PipelineOptionsFactory.create();
		options.setRunner(DirectRunner.class);
		options.setJobName("Opernhaus_Zürich_Pipeline");
		
		Pipeline pipeline = Pipeline.create(options);
		
    	PCollection<Activity> data = XmlProcurer.procure(pipeline, "https://www.opernhaus.ch/xmlexport/kzexport.xml");
		data.apply(PipelineUtil.printActivities());
    	
    	PipelineResult result = pipeline.run();
    	return String.valueOf(result.getState());
	}

}

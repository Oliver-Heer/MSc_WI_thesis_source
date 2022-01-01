package ch.mscwi.wikidata.util;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.ParDo.SingleOutput;

import ch.mscwi.wikidata.pipeline.pojo.Activity;

public class PipelineUtil {
	
	public static SingleOutput<Activity, Activity> printActivities() {
		return ParDo.of(new DoFn<Activity, Activity>() {
			@ProcessElement
			public void processElement(ProcessContext context) {
				System.out.println(context.element());
			}
		});
	}

}

package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;
import java.util.StringJoiner;

public class ActivityDates { 
	public List<ActivityDate> ActivityDate;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		if(ActivityDate == null) {
			return "";
		}
		
		ActivityDate.stream().forEach(date -> {
			joiner.add(String.valueOf(date));
		});
		
		return joiner.toString();
	}
}

package ch.mscwi.wikidata.pojo;

import java.util.List;
import java.util.StringJoiner;

public class Videos { 
	public List<Video> Video;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		if(Video == null) {
			return "";
		}
		
		Video.stream().forEach(video -> {
			joiner.add(String.valueOf(video));
		});
		
		return joiner.toString();
	}
}

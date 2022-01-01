package ch.mscwi.wikidata.pojo;

import java.util.StringJoiner;

public class ActivitySettings { 
	public Branches Branches;
	public Genres Genres;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		joiner.add(String.valueOf(Branches));
		joiner.add(String.valueOf(Genres));
		
		return joiner.toString();
	}
}

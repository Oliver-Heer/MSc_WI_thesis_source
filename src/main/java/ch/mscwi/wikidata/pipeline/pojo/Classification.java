package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

public class Classification { 
	public int originId;
	public String name;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		joiner.add("originId:" + originId);
		joiner.add("name:" + name);
		
		return joiner.toString();
	}
}

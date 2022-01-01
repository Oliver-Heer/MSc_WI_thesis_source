package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {
	
	@XmlAttribute
	public int id;
	
	@XmlAttribute
	public String name;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		joiner.add("---Location---");
		joiner.add("Id: " + id);
		joiner.add("Name: " + name);

		return joiner.toString();
	}
}

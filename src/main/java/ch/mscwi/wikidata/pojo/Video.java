package ch.mscwi.wikidata.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Video {
	
	@XmlAttribute
	public String url;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		joiner.add("---Video---");
		joiner.add("url: " + url);
		return joiner.toString();
	}

}

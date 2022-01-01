package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class Cast {
	
	@XmlAttribute
	public int originId;
	
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String role;
	
	@XmlAttribute
	public String roleCategory;
	
	@XmlAttribute
	public int IsStarRole;
	
	@XmlAttribute
	public int sort;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		joiner.add("---Cast---");
		joiner.add("originId: " + originId);
		joiner.add("name: " + name);
		joiner.add("role: " + role);
		joiner.add("roleCategory: " + roleCategory);
		joiner.add("IsStarRole: " + IsStarRole);
		joiner.add("sort: " + sort);
		
		return joiner.toString();
	}
}

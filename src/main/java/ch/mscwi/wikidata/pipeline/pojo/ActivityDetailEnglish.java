package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlAttribute;

public class ActivityDetailEnglish {
	
	@XmlAttribute
	public String languageCode;

	public String Title;
	public String SubTitle;
	public String ShortDescription;
	public String LongDescription;
	public Object CastInformation;
	public String text;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		joiner.add("---ActivityDetailEnglish---");
		joiner.add("LanguageCode: " + languageCode);
		joiner.add("Title: " + Title);
		joiner.add("SubTitle: " + SubTitle);
		joiner.add("ShortDescription: " + ShortDescription);
		joiner.add("LongDescription: " + LongDescription);
		joiner.add("text: " + text);
		joiner.add(String.valueOf(CastInformation));

		return joiner.toString();
	}
}

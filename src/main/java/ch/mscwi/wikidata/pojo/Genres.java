package ch.mscwi.wikidata.pojo;

import java.util.List;
import java.util.StringJoiner;

public class Genres { 
	public List<Genre> Genre;
	
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));
		
		if(Genre == null) {
			return "";
		}
		
		Genre.stream().forEach(genre -> {
			joiner.add(String.valueOf(genre));
		});
		
		return joiner.toString();
	}
}

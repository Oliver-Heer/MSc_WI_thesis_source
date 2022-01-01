package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;
import java.util.StringJoiner;

public class Activities {
  public List<Activity> Activity;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    if (Activity == null) {
      return "";
    }

    Activity.stream().forEach(activity -> {
      joiner.add(String.valueOf(activity));
    });

    return joiner.toString();
  }
}

package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;
import java.util.StringJoiner;

public class ActivityCast {
  public List<Cast> Cast;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    if (Cast == null) {
      return "";
    }

    Cast.stream().forEach(cast -> {
      joiner.add(String.valueOf(cast));
    });

    return joiner.toString();
  }
}

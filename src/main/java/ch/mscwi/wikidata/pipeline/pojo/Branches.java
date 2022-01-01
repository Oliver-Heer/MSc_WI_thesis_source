package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;
import java.util.StringJoiner;

public class Branches {
  public List<Branch> Branch;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    if (Branch == null) {
      return "";
    }

    Branch.stream().forEach(branch -> {
      joiner.add(String.valueOf(branch));
    });

    return joiner.toString();
  }
}

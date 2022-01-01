package ch.mscwi.wikidata.pipeline.pojo;

import java.util.StringJoiner;

public class ActivityMultimedia {
  public Videos Videos;
  public Images Images;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    joiner.add(String.valueOf(Videos));
    joiner.add(String.valueOf(Images));

    return joiner.toString();
  }
}

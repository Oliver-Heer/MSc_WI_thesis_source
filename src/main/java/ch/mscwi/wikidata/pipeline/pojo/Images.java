package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;
import java.util.StringJoiner;

public class Images {
  public List<Image> Image;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

    if (Image == null) {
      return "";
    }

    Image.stream().forEach(image -> {
      joiner.add(String.valueOf(image));
    });

    return joiner.toString();
  }
}

package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivityMultimedia {
  @XmlElementWrapper(name="Videos")
  @XmlElement(name="Video")
  List<Video> videos;

  @XmlElementWrapper(name="Images")
  @XmlElement(name="Image")
  List<Image> images;
}

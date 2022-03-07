package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivityMultimedia implements IKulturZueriObject {
  @XmlElementWrapper(name="Videos")
  @XmlElement(name="Video")
  public List<Video> videos = new ArrayList<>();

  @XmlElementWrapper(name="Images")
  @XmlElement(name="Image")
  public List<Image> images = new ArrayList<>();
}

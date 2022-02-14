package ch.mscwi.wikidata.pipeline.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivitySettings {
  @XmlElementWrapper(name="Branches")
  @XmlElement(name="Branch")
  public List<Branch> branches;

  @XmlElementWrapper(name="Genres")
  @XmlElement(name="Genre")
  public List<Genre> genres;
}
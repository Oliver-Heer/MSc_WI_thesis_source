package ch.mscwi.wikidata.pipeline.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivitySettings {
  @XmlElementWrapper(name="Branches")
  @XmlElement(name="Branch")
  List<Branch> branches;

  @XmlElementWrapper(name="Genres")
  @XmlElement(name="Genre")
  List<Genre> genres;
}

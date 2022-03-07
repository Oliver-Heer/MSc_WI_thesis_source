package ch.mscwi.wikidata.pipeline.model.kulturzueri;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ActivitySettings implements IKulturZueriObject {
  @XmlElementWrapper(name="Branches")
  @XmlElement(name="Branch")
  public List<Branch> branches = new ArrayList<>();

  @XmlElementWrapper(name="Genres")
  @XmlElement(name="Genre")
  public List<Genre> genres = new ArrayList<>();
}

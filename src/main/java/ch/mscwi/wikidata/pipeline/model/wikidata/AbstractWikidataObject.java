package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWikidataObject implements IWikidataObject {

  private List<Property> properties = new ArrayList<>();

  @Override
  public IWikidataObject withProperty(String pIdentifer, String label, String value) {
    properties.add(new Property(pIdentifer, label, value));
    return this;
  }

  @Override
  public List<Property> getProperties() {
    return properties;
  }

}

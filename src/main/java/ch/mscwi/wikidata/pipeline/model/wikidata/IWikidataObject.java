package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.List;

public interface IWikidataObject {

  public String getQIdentifier();

  public List<IWikidataObject> getChildren();

  public List<Property> getProperties();

}

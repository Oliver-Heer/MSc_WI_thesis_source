package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.List;

public interface IWikidataObject {

  public String getQIdentifier();

  public IWikidataObject withProperty(String pIdentifer, String label, String value);

  public List<Property> getProperties();

}

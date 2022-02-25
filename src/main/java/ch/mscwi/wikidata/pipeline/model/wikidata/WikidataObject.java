package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.ArrayList;
import java.util.List;

public class WikidataObject implements IWikidataObject {

  private final String qIdentifier;
  private final List<IWikidataObject> children;
  private final List<Property> properties;

  private WikidataObject(WikidataObjectBuilder builder) {
    this.qIdentifier = builder.qIdentifier;
    this.children = builder.children;
    this.properties = builder.properties;
  }

  @Override
  public String getQIdentifier() {
    return qIdentifier;
  }

  @Override
  public List<IWikidataObject> getChildren() {
    return children;
  }

  @Override
  public List<Property> getProperties() {
    return properties;
  }

  public static class WikidataObjectBuilder {

    private final String qIdentifier;
    private final List<IWikidataObject> children = new ArrayList<>();
    private final List<Property> properties = new ArrayList<>();

    public WikidataObjectBuilder(String qIdentifier) {
      this.qIdentifier = qIdentifier;
    }

    public WikidataObjectBuilder withChild(IWikidataObject child) {
      children.add(child);
      return this;
    }

    public WikidataObjectBuilder withProperty(String pIdentifer, String label, String value) {
      properties.add(new Property(pIdentifer, label, value));
      return this;
    }

    public WikidataObjectBuilder withRDFSLabel(String value, String language) {
      String label = value + "@" + language;
      properties.add(new Property("rdfs:label", "label", label));
      return this;
    }

    public WikidataObjectBuilder withSchemaDescription(String value, String language) {
      String label = value + "@" + language;
      properties.add(new Property("schema:description", "description", label));
      return this;
    }

    public WikidataObject build() {
      return new WikidataObject(this);
    }

  }

}

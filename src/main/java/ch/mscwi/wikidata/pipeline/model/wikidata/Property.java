package ch.mscwi.wikidata.pipeline.model.wikidata;

public class Property {

  public final String pIdentifier;
  public final String label;
  public final String value;

  public Property(final String pIdentifier, final String label, String value) {
    this.pIdentifier = pIdentifier;
    this.label = label;
    this.value = value;
  }

}

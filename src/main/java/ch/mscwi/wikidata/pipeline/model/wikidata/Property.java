package ch.mscwi.wikidata.pipeline.model.wikidata;

public class Property {

  public final String pIdentifier;
  public final String description;
  public final String value;
  public final boolean isRDFSLabel;

  public Property(final String pIdentifier, final String description, String value, boolean isRDFSLabel) {
    this.pIdentifier = pIdentifier;
    this.description = description;
    this.value = value;
    this.isRDFSLabel = isRDFSLabel;
  }

  public Property(final String pIdentifier, final String description, String value) {
    this.pIdentifier = pIdentifier;
    this.description = description;
    this.value = value;
    this.isRDFSLabel = false;
  }

}

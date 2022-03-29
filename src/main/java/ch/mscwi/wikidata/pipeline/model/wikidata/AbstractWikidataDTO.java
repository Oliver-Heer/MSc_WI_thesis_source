package ch.mscwi.wikidata.pipeline.model.wikidata;

public abstract class AbstractWikidataDTO {

  private String wikidataUid;

  public String getWikidataUid() {
    return wikidataUid;
  }

  public void setWikidataUid(String wikidataUid) {
    this.wikidataUid = wikidataUid;
  }

}

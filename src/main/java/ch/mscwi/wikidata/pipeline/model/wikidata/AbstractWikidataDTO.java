package ch.mscwi.wikidata.pipeline.model.wikidata;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractWikidataDTO {

  private String wikidataUid;

  @Enumerated(EnumType.STRING)
  private ReconciliationState state;

  public String getWikidataUid() {
    return wikidataUid;
  }

  public void setWikidataUid(String wikidataUid) {
    this.wikidataUid = wikidataUid;
  }

  public ReconciliationState getState() {
    return state;
  }

  public void setState(ReconciliationState state) {
    this.state = state;
  }

}

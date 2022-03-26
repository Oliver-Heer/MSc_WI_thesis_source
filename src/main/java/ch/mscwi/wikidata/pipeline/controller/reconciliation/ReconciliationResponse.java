package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

public class ReconciliationResponse {

  public String queryId;

  @JsonAnySetter
  public void setQuery(String queryId, Map<String, JsonNode> result) {
    this.queryId = queryId;

    System.err.println(result);
  }

}

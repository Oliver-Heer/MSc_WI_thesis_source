package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

public class ReconciliationResponse {

  private final Map<String, String> entities = new HashMap<>();

  @JsonAnySetter
  public void setQuery(String queryId, Map<String, JsonNode> result) {
    JsonNode jsonResult = result.get("result");

    if (jsonResult.isEmpty()) {
      entities.put(queryId, null);
      return;
    }

    String uid = String.valueOf(jsonResult.get(0).get("id"));
    String strippedUid = StringUtils.strip(uid, "\"");
    entities.put(queryId, strippedUid);
  }

  public Map<String, String> getEntities() {
    return entities;
  }

}

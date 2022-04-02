package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.util.List;
import java.util.StringJoiner;

import javax.json.Json;

import org.apache.commons.lang3.StringUtils;

public class ReconciliationQueryBuilder {

  private String identifier = "";
  private String query = "";
  private String type = "Q35120";
  private int limit = 1;

  public ReconciliationQueryBuilder(String identifier) {
    this.identifier = identifier;
  }

  public ReconciliationQueryBuilder withQuery(String query) {
    this.query = query;
    return this;
  }

  public ReconciliationQueryBuilder withType(String type) {
    this.type = type;
    return this;
  }

  public ReconciliationQueryBuilder withLimit(int limit) {
    this.limit = limit;
    return this;
  }

  public String build() {
    return Json.createObjectBuilder()
        .add(identifier, Json.createObjectBuilder()
            .add("query", query)
            .add("type", type)
            .add("limit", limit))
        .build()
        .toString();
  };

  public static String toBatchQuery(List<String> queries) {
    StringJoiner queryJoiner = new StringJoiner(",", "{", "}");

    queries.stream()
        .map(query -> StringUtils.removeStart(query, "{"))
        .map(query -> StringUtils.removeEnd(query, "}"))
        .forEach(query -> queryJoiner.add(query));

    return queryJoiner.toString();
  }

}

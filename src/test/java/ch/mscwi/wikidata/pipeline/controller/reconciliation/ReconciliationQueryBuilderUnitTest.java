package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ReconciliationQueryBuilderUnitTest {

  @Test
  void emptyDefaultEntity() {
    String query = new ReconciliationQueryBuilder("ID")
        .build();

    assertEquals("{\"ID\":{\"query\":\"\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[]}}", query);
  }

  @Test
  void type() {
    String query = new ReconciliationQueryBuilder("ID")
        .withType("Q5")
        .build();

    assertEquals("{\"ID\":{\"query\":\"\",\"type\":\"Q5\",\"limit\":10,\"properties\":[]}}", query);
  }

  @Test
  void limit() {
    String query = new ReconciliationQueryBuilder("ID")
        .withLimit(99)
        .build();

    assertEquals("{\"ID\":{\"query\":\"\",\"type\":\"Q35120\",\"limit\":99,\"properties\":[]}}", query);
  }

  @Test
  void query() {
    String query = new ReconciliationQueryBuilder("ID")
        .withQuery("Some Query String")
        .build();

    assertEquals("{\"ID\":{\"query\":\"Some Query String\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[]}}", query);
  }

  @Test
  void property() {
    String query = new ReconciliationQueryBuilder("ID")
        .addProperty("p1", "v1")
        .build();

    assertEquals("{\"ID\":{\"query\":\"\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[{\"pid\":\"p1\",\"v\":\"v1\"}]}}", query);
  }

  @Test
  void multipleProperties() {
    String query = new ReconciliationQueryBuilder("ID")
        .addProperty("p1", "v1")
        .addProperty("p2", "v2")
        .build();

    assertEquals("{\"ID\":{\"query\":\"\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[{\"pid\":\"p1\",\"v\":\"v1\"},{\"pid\":\"p2\",\"v\":\"v2\"}]}}", query);
  }

  @Test
  void fullBuilder() {
    String query = new ReconciliationQueryBuilder("ID")
        .withQuery("Some Query String")
        .withType("Q5")
        .withLimit(17)
        .addProperty("P1", "v1")
        .addProperty("P2", "v2")
        .build();

    assertEquals("{\"ID\":{\"query\":\"Some Query String\",\"type\":\"Q5\",\"limit\":17,\"properties\":[{\"pid\":\"P1\",\"v\":\"v1\"},{\"pid\":\"P2\",\"v\":\"v2\"}]}}", query);
  }

  @Test
  void toBatchQuery() {
    String q1 = new ReconciliationQueryBuilder("1").withQuery("query1").build();
    String q2 = new ReconciliationQueryBuilder("2").withQuery("query2").build();

    String batchQuery = ReconciliationQueryBuilder.toBatchQuery(List.of(q1, q2));
    assertEquals("{\"1\":{\"query\":\"query1\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[]},\"2\":{\"query\":\"query2\",\"type\":\"Q35120\",\"limit\":10,\"properties\":[]}}", batchQuery);
  }

}

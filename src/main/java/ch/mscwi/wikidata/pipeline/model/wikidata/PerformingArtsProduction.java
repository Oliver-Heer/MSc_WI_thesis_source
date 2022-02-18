package ch.mscwi.wikidata.pipeline.model.wikidata;

import java.util.ArrayList;
import java.util.List;

public class PerformingArtsProduction extends AbstractWikidataObject {

  private static final String qIdentifier = "Q43099500";

  private List<Performance> performances = new ArrayList<>();

  @Override
  public String getQIdentifier() {
    return qIdentifier;
  }

  public PerformingArtsProduction withPerformance(Performance performance) {
    performances.add(performance);
    return this;
  }

  public List<Performance> getPerformances() {
    return performances;
  }

}

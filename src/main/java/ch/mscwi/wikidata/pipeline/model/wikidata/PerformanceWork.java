package ch.mscwi.wikidata.pipeline.model.wikidata;

public class PerformanceWork extends AbstractWikidataObject {

  private static final String qIdentifier = "Q17538722";

  public final PerformingArtsProduction performingArtsProduction;
  public final String title;

  public PerformanceWork(PerformingArtsProduction performingArtsProduction, String title) {
    this.performingArtsProduction = performingArtsProduction;
    this.title = title;
  }

  @Override
  public String getQIdentifier() {
    return qIdentifier;
  }

}

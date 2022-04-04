package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationProperties {

  private String service;
  private String genreEntity;
  private String locationEntity;

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getGenreEntity() {
    return genreEntity;
  }

  public void setGenreEntity(String genreEntity) {
    this.genreEntity = genreEntity;
  }

  public String getLocationEntity() {
    return locationEntity;
  }

  public void setLocationEntity(String locationEntity) {
    this.locationEntity = locationEntity;
  }

}

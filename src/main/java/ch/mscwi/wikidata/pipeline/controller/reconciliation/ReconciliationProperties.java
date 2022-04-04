package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationProperties {

  private String service;
  private String genreEntity;
  private String locationEntity;
  private String actorEntity;
  private Map<String, String> actorProperties;

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

  public String getActorEntity() {
    return actorEntity;
  }

  public void setActorEntity(String actorEntity) {
    this.actorEntity = actorEntity;
  }

  public Map<String, String> getActorProperties() {
    return actorProperties;
  }

  public void setActorProperties(Map<String, String> actorProperties) {
    this.actorProperties = actorProperties;
  }

}

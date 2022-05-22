package ch.mscwi.wikidata.pipeline.controller.reconciliation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "reconciliation")
public class ReconciliationProperties {

  private String service;
  private String activityEntity;
  private String genreEntity;
  private String locationEntity;
  private String actorEntity;
  private String roleEntity;
  private Map<String, String> activityProperties = new HashMap<>();
  private Map<String, String> actorProperties = new HashMap<>();
  private Map<String, String> locationProperties = new HashMap<>();

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getActivityEntity() {
    return activityEntity;
  }

  public void setActivityEntity(String activityEntity) {
    this.activityEntity = activityEntity;
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

  public String getRoleEntity() {
    return roleEntity;
  }

  public void setRoleEntity(String roleEntity) {
    this.roleEntity = roleEntity;
  }

  public Map<String, String> getActivityProperties() {
    return activityProperties;
  }

  public void setActivityProperties(Map<String, String> activityProperties) {
    this.activityProperties = activityProperties;
  }

  public Map<String, String> getActorProperties() {
    return actorProperties;
  }

  public void setActorProperties(Map<String, String> actorProperties) {
    this.actorProperties = actorProperties;
  }

  public Map<String, String> getLocationProperties() {
    return locationProperties;
  }

  public void setLocationProperties(Map<String, String> locationProperties) {
    this.locationProperties = locationProperties;
  }

}

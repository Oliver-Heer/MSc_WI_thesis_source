package ch.mscwi.wikidata.pipeline.controller.publication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "publicator")
public class PublicatorProperties {

  private String username;
  private String password;
  private String targetIri;
  private String targetWikidataAPI;
  private String botUserAgent;
  private boolean isBotApproved;
  private boolean publishingEnabled;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getTargetIri() {
    return targetIri;
  }

  public void setTargetIri(String targetIri) {
    this.targetIri = targetIri;
  }

  public String getTargetWikidataAPI() {
    return targetWikidataAPI;
  }

  public void setTargetWikidataAPI(String targetWikidataAPI) {
    this.targetWikidataAPI = targetWikidataAPI;
  }

  public String getBotUserAgent() {
    return botUserAgent;
  }

  public void setBotUserAgent(String botUserAgent) {
    this.botUserAgent = botUserAgent;
  }

  public boolean isBotApproved() {
    return isBotApproved;
  }

  public void setBotApproved(boolean isBotApproved) {
    this.isBotApproved = isBotApproved;
  }

  public boolean isPublishingEnabled() {
    return publishingEnabled;
  }

  public void setPublishingEnabled(boolean publishingEnabled) {
    this.publishingEnabled = publishingEnabled;
  }

}

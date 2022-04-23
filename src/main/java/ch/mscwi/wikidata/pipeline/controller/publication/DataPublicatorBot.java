package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;

@Service
public class DataPublicatorBot {

  private Logger logger = LoggerFactory.getLogger(DataPublicatorBot.class);

  @Autowired
  private LocationStatement locationStatement;

  private PublicatorProperties publicatorProperties;

  private WikibaseDataEditor dataEditor;
  private WikibaseDataFetcher dataFetcher;

  private final BasicApiConnection connection;

  public DataPublicatorBot(PublicatorProperties publicatorProperties) {
    this.publicatorProperties = publicatorProperties;

    isPropertyConfigured("publicator.botUserAgent", this.publicatorProperties.getBotUserAgent());
    isPropertyConfigured("publicator.targetIri", this.publicatorProperties.getTargetIri());
    isPropertyConfigured("publicator.targetWikidataAPI", this.publicatorProperties.getTargetWikidataAPI());
    isPropertyConfigured("publicator.username", this.publicatorProperties.getUsername());
    isPropertyConfigured("publicator.password", this.publicatorProperties.getPassword());

    WebResourceFetcherImpl.setUserAgent(publicatorProperties.getBotUserAgent());
    connection = new BasicApiConnection(publicatorProperties.getTargetWikidataAPI());
    login();

    dataFetcher = new WikibaseDataFetcher(connection, publicatorProperties.getTargetIri());

    dataEditor = new WikibaseDataEditor(connection, publicatorProperties.getTargetIri());
    if (!publicatorProperties.isPublishingEnabled()) {
      logger.info("Publicator is disabled");
      dataEditor.disableEditing();
    }
  }

  private void login() {
    try {
      connection.login(publicatorProperties.getUsername(), publicatorProperties.getPassword());
    } catch (LoginFailedException e) {
      logger.error("Login failed " + e.getMessage());
      new RuntimeException(e.getMessage());
    }
  }

  private void checkLogin() {
    try {
      connection.checkCredentials();
    } catch (IOException | MediaWikiApiErrorException e) {
      logger.warn("Credentials or CSRF Token expired " + e.getMessage());
      login();
    }
  }

  public String publishNewLocation(LocationDTO location) throws MediaWikiApiErrorException, IOException {
    checkLogin();
    ItemDocument newLocation = locationStatement.prepareLocationStatement(dataFetcher, location);

    if (!publicatorProperties.isPublishingEnabled()) {
      logger.info("Publishing disabled, new Location preview: " + newLocation);
      return null;
    }

    logger.info("Creating new Location " + location.getName());
    return publishDocument(newLocation);
  }

  private String publishDocument(ItemDocument newDocument) throws IOException, MediaWikiApiErrorException {
    EntityDocument newEntity = dataEditor.createEntityDocument(newDocument, "Creating new entity", null);
    String newEntityIdentifier = String.valueOf(newEntity.getEntityId());
    logger.info("Created new Entity " + newEntityIdentifier);
    return newEntityIdentifier;
  }

  private void isPropertyConfigured(String propertyName, String property) {
    if (StringUtils.isBlank(property)) {
      String errorMessage = propertyName + " is not configured";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

}

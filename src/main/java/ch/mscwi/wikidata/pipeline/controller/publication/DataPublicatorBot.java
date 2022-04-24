package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;

@Service
public class DataPublicatorBot {

  private Logger logger = LoggerFactory.getLogger(DataPublicatorBot.class);

  @Autowired
  private AbstractStatement locationStatement;

  @Autowired
  private AbstractStatement actorStatement;

  @Autowired
  private AbstractStatement activityStatement;

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
    ItemDocument newLocation = locationStatement.prepareStatement(dataFetcher, location);

    logger.info("Creating new Location " + location.getName());
    return publishDocument(newLocation);
  }

  public String publishNewActor(ActorDTO actor) throws MediaWikiApiErrorException, IOException {
    checkLogin();
    ItemDocument newActor = actorStatement.prepareStatement(dataFetcher, actor);

    logger.info("Creating new Actor " + actor.getName());
    return publishDocument(newActor);
  }

  public String publishNewActivity(ActivityDTO activity) throws MediaWikiApiErrorException, IOException {
    checkLogin();
    ItemDocument newActivity = activityStatement.prepareStatement(dataFetcher, activity);

    logger.info("Creating new Activity " + activity.getTitle());
    return publishDocument(newActivity);
  }

  private String publishDocument(ItemDocument newDocument) throws IOException, MediaWikiApiErrorException {
    if (!publicatorProperties.isPublishingEnabled()) {
      logger.info("Publishing disabled, new Entity preview: " + newDocument);
      return null;
    }

    EntityDocument newEntity = dataEditor.createEntityDocument(newDocument, "Creating new entity", null);
    EntityIdValue newEntityUrl = newEntity.getEntityId();
    logger.info("Created new Entity " + String.valueOf(newEntityUrl));
    return newEntityUrl.getId();
  }

  private void isPropertyConfigured(String propertyName, String property) {
    if (StringUtils.isBlank(property)) {
      String errorMessage = propertyName + " is not configured";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

}

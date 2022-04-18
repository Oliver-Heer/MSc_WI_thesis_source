package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.BasicApiConnection;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;

@Service
public class DataPublicatorBot {

  private Logger logger = LoggerFactory.getLogger(DataPublicatorBot.class);

  private PublicatorProperties publicatorProperties;

  private WikibaseDataEditor dataEditor;
  private WikibaseDataFetcher dataFetcher;

  public DataPublicatorBot(PublicatorProperties publicatorProperties) {
    this.publicatorProperties = publicatorProperties;

    isPropertyConfigured("publicator.botUserAgent", this.publicatorProperties.getBotUserAgent());
    isPropertyConfigured("publicator.targetIri", this.publicatorProperties.getTargetIri());
    isPropertyConfigured("publicator.targetWikidataAPI", this.publicatorProperties.getTargetWikidataAPI());

    WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
    ApiConnection connection = new BasicApiConnection(publicatorProperties.getTargetWikidataAPI());
    // connection.login("username", "password");

    dataFetcher = new WikibaseDataFetcher(connection, publicatorProperties.getTargetIri());

    dataEditor = new WikibaseDataEditor(connection, publicatorProperties.getTargetIri());
    if (!publicatorProperties.isPublishingEnabled()) {
      logger.info("Publicator is disabled");
      dataEditor.disableEditing();
    }
  }

  public String publishNewLocation(LocationDTO location) throws MediaWikiApiErrorException, IOException {
    List<String> wikidataEntityIDs = WikidataEntity.forLocation();
    Map<String, EntityDocument> wikidataEntities = dataFetcher.getEntityDocuments(wikidataEntityIDs);

    if (wikidataEntityIDs.size() != wikidataEntities.size()) {
      String errorMessage = "Required entity vanished on Wikidata, required: " + String.valueOf(wikidataEntityIDs) + " found: " + String.valueOf(wikidataEntities.keySet());
      logger.warn(errorMessage);
      throw new RuntimeException(errorMessage);
    }

    Statement instanceOfStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_INSTANCE_OF, WikidataEntity.ENTITY_VENUE);
    Statement countryStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_COUNTRY, WikidataEntity.ENTITY_SWITZERLAND);

    ItemDocument newDocument = ItemDocumentBuilder.forItemId(ItemIdValue.NULL)
        .withLabel(location.getName(), "de")
        .withStatement(instanceOfStatement)
        .withStatement(countryStatement)
        .build();

    logger.info("Creating new Location " + location.getName());
    EntityDocument newEntity = dataEditor.createEntityDocument(newDocument, "Creating new location", null);
    String newEntityIdentifier = String.valueOf(newEntity.getEntityId());
    logger.info("Created new Location " + newEntityIdentifier);
    return newEntityIdentifier;
  }

  private Statement createReferenceStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String entityKey) {
    PropertyIdValue property = (PropertyIdValue) wikidataEntities.get(propertyKey).getEntityId();
    EntityIdValue value = wikidataEntities.get(entityKey).getEntityId();
    return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, property)
        .withValue((Value) value)
        .build();
  }

  private void isPropertyConfigured(String propertyName, String property) {
    if (StringUtils.isBlank(property)) {
      String errorMessage = propertyName + " is not configured";
      logger.error(errorMessage);
      throw new RuntimeException(errorMessage);
    }
  }

}

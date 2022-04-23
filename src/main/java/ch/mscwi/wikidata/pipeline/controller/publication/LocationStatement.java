package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;

@Service
public class LocationStatement extends AbstractStatement {

  private Logger logger = LoggerFactory.getLogger(LocationStatement.class);

  public ItemDocument prepareLocationStatement(WikibaseDataFetcher dataFetcher, LocationDTO location) throws MediaWikiApiErrorException, IOException {
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
        .withLabel(location.getName(), "en")
        .withLabel(location.getName(), "de")
        .withStatement(instanceOfStatement)
        .withStatement(countryStatement)
        .build();

    return newDocument;
  }

}

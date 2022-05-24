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

import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;

@Service
public class ActorStatement extends AbstractStatement {

  private Logger logger = LoggerFactory.getLogger(ActorStatement.class);

  @Override
  public <T extends AbstractWikidataDTO> ItemDocument prepareStatement(WikibaseDataFetcher dataFetcher, T dto) throws MediaWikiApiErrorException, IOException {
    ActorDTO actor = (ActorDTO) dto;
    List<String> wikidataEntityIDs = WikidataEntity.forActor();
    Map<String, EntityDocument> wikidataEntities = dataFetcher.getEntityDocuments(wikidataEntityIDs);

    if (wikidataEntityIDs.size() != wikidataEntities.size()) {
      String errorMessage = "Required entity vanished on Wikidata, required: " + String.valueOf(wikidataEntityIDs) + " found: " + String.valueOf(wikidataEntities.keySet());
      logger.warn(errorMessage);
      throw new RuntimeException(errorMessage);
    }

    Statement instanceOfStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_INSTANCE_OF, WikidataEntity.ENTITY_HUMAN);
    Statement occupationStatement = createOccupationStatement(wikidataEntities, actor);

    ItemDocumentBuilder builder = ItemDocumentBuilder.forItemId(ItemIdValue.NULL)
        .withLabel(actor.getName(), "en")
        .withLabel(actor.getName(), "de")
        .withStatement(instanceOfStatement);

    if (occupationStatement != null) {
        builder.withStatement(occupationStatement);
    }

    return builder.build();
  }

}

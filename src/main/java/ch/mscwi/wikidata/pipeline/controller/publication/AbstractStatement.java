package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;
import java.util.Map;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;

public abstract class AbstractStatement {

  public abstract <T extends AbstractWikidataDTO> ItemDocument prepareStatement(WikibaseDataFetcher dataFetcher,T dto) throws MediaWikiApiErrorException, IOException;

  public Statement createReferenceStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String entityKey) {
    PropertyIdValue property = (PropertyIdValue) wikidataEntities.get(propertyKey).getEntityId();
    EntityIdValue value = wikidataEntities.get(entityKey).getEntityId();
    return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, property)
        .withValue((Value) value)
        .build();
  }

  public Statement createValueStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String value) {
    return createValueStatement(wikidataEntities, propertyKey, value, "de");
  }

  public Statement createValueStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String value, String language) {
    PropertyIdValue property = (PropertyIdValue) wikidataEntities.get(propertyKey).getEntityId();
    return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, property)
        .withValue(Datamodel.makeMonolingualTextValue(value, language))
        .build();
  }

}

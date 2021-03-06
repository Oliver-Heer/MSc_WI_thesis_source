package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

@Service
public class ActivityStatement extends AbstractStatement {

  private Logger logger = LoggerFactory.getLogger(ActivityStatement.class);

  @Autowired
  private PublicatorProperties properties;

  @Override
  public <T extends AbstractWikidataDTO> ItemDocument prepareStatement(WikibaseDataFetcher dataFetcher, T dto) throws MediaWikiApiErrorException, IOException {
    ActivityDTO activity = (ActivityDTO) dto;
    ArrayList<String> wikidataEntityIDs = new ArrayList<String>();
    wikidataEntityIDs.addAll(WikidataEntity.forActivity());
    wikidataEntityIDs.addAll(getGenreUids(activity));
    wikidataEntityIDs.addAll(getActorUids(activity));
    wikidataEntityIDs.addAll(getRoleUids(activity));

    LocationDTO location = activity.getLocation();
    if (location != null && ReconciliationState.APPROVED == location.getState()) {
      wikidataEntityIDs.add(location.getWikidataUid());
    }

    boolean anyReferenceNotReconciled = wikidataEntityIDs.stream().anyMatch(StringUtils::isBlank);
    if (anyReferenceNotReconciled) {
      String errorMessage = "Aborting publishing, not all dependent references reconciled for activity " + activity.getTitle();
      logger.warn(errorMessage);
      throw new RuntimeException(errorMessage);
    }

    Map<String, EntityDocument> wikidataEntities = dataFetcher.getEntityDocuments(wikidataEntityIDs);

    if (wikidataEntityIDs.size() != wikidataEntities.size()) {
      String errorMessage = "Required entity vanished on Wikidata, required: " + String.valueOf(wikidataEntityIDs) + " found: " + String.valueOf(wikidataEntities.keySet());
      logger.warn(errorMessage);
      throw new RuntimeException(errorMessage);
    }

    Statement instanceOfStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_INSTANCE_OF, WikidataEntity.ENTITY_PERFORMING_ARTS_PRODUCTION);
    Statement productionCompanyStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_PRODUCTION_COMPANY, WikidataEntity.ENTITY_ZURICH_OPERA);

    ItemDocumentBuilder documentBuilder = ItemDocumentBuilder.forItemId(ItemIdValue.NULL)
        .withStatement(instanceOfStatement)
        .withStatement(productionCompanyStatement);

    if (location != null && StringUtils.isNotBlank(location.getWikidataUid())) {
      Statement locationStatement = createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_LOCATION, location.getWikidataUid());
      documentBuilder.withStatement(locationStatement);
    }

    String title = activity.getTitle();
    if (StringUtils.isNotBlank(title)) {
      documentBuilder.withLabel(title, "de");
      Statement titleStatement = createValueStatement(wikidataEntities, WikidataEntity.PROPERTY_TITLE, title, "de");
      documentBuilder.withStatement(titleStatement);
    }

    String titleEn = activity.getTitleEn();
    if (StringUtils.isNotBlank(titleEn)) {
      documentBuilder.withLabel(titleEn, "en");
      Statement titleEnStatement = createValueStatement(wikidataEntities, WikidataEntity.PROPERTY_TITLE, titleEn, "en");
      documentBuilder.withStatement(titleEnStatement);
    }

    String subtitle = activity.getSubTitle();
    if (StringUtils.isNotBlank(subtitle)) {
      Statement subtitleStatement = createValueStatement(wikidataEntities, WikidataEntity.PROPERTY_SUBTITLE, subtitle, "de");
      documentBuilder.withStatement(subtitleStatement);
    }

    String subtitleEn = activity.getSubTitleEn();
    if (StringUtils.isNotBlank(subtitleEn)) {
      Statement subtitleEnStatement = createValueStatement(wikidataEntities, WikidataEntity.PROPERTY_SUBTITLE, subtitleEn, "en");
      documentBuilder.withStatement(subtitleEnStatement);
    }

    documentBuilder.withDescription(properties.getDescriptionDe(), "de");
    documentBuilder.withDescription(properties.getDescriptionEn(), "en");

    activity.getGenres().stream()
        .filter(genre -> StringUtils.isNotBlank(genre.getWikidataUid()))
        .filter(genre -> ReconciliationState.APPROVED == genre.getState())
        .map(genre -> createReferenceStatement(wikidataEntities, WikidataEntity.PROPERTY_GENRE, genre.getWikidataUid()))
        .forEach(statement -> documentBuilder.withStatement(statement));

    activity.getActors().stream()
        .filter(actor -> StringUtils.isNotBlank(actor.getWikidataUid()))
        .filter(actor -> ReconciliationState.APPROVED == actor.getState())
        .map(actor -> createActorReferenceStatement(wikidataEntities, actor))
        .forEach(statement -> documentBuilder.withStatement(statement));

    return documentBuilder.build();
  }

  private Set<String> getActorUids(ActivityDTO activity) {
    return activity.getActors().stream()
        .filter(actor -> actor.getState() == ReconciliationState.APPROVED)
        .map(actor -> actor.getWikidataUid())
        .filter(uid -> StringUtils.isNotBlank(uid))
        .collect(Collectors.toSet());
  }

  private Set<String> getRoleUids(ActivityDTO activity) {
    return activity.getRoles().stream()
        .filter(roles -> roles.getState() == ReconciliationState.APPROVED)
        .map(role -> role.getWikidataUid())
        .filter(uid -> StringUtils.isNotBlank(uid))
        .collect(Collectors.toSet());
  }

  private Set<String> getGenreUids(ActivityDTO activity) {
    return activity.getGenres().stream()
        .filter(genre -> genre.getState() == ReconciliationState.APPROVED)
        .map(genre -> genre.getWikidataUid())
        .filter(uid -> StringUtils.isNotBlank(uid))
        .collect(Collectors.toSet());
  }

}

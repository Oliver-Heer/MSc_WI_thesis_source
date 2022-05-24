package ch.mscwi.wikidata.pipeline.controller.publication;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ReferenceBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Reference;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
import org.wikidata.wdtk.datamodel.interfaces.TimeValue;
import org.wikidata.wdtk.datamodel.interfaces.Value;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import ch.mscwi.wikidata.pipeline.ConfigProperties;
import ch.mscwi.wikidata.pipeline.model.wikidata.AbstractWikidataDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;

public abstract class AbstractStatement {

  @Autowired
  private ConfigProperties config;

  public abstract <T extends AbstractWikidataDTO> ItemDocument prepareStatement(WikibaseDataFetcher dataFetcher, T dto) throws MediaWikiApiErrorException, IOException;

  public Statement createReferenceStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String entityKey) {
    PropertyIdValue property = (PropertyIdValue) wikidataEntities.get(propertyKey).getEntityId();
    EntityIdValue value = wikidataEntities.get(entityKey).getEntityId();
    return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, property)
        .withReference(reference(wikidataEntities))
        .withValue((Value) value)
        .build();
  }

  public Statement createActorReferenceStatement(Map<String, EntityDocument> wikidataEntities, ActorDTO actor) {
    Tuple tuple = getRole(actor);

    PropertyIdValue castMemberProperty = (PropertyIdValue) wikidataEntities.get(tuple.property).getEntityId();
    EntityIdValue castMemberValue = wikidataEntities.get(actor.getWikidataUid()).getEntityId();

    StatementBuilder builder = StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, castMemberProperty)
        .withReference(reference(wikidataEntities))
        .withValue((Value) castMemberValue);

    return builder.build();
  }

  public Statement createOccupationStatement(Map<String, EntityDocument> wikidataEntities, ActorDTO actor) {
    Tuple tuple = getRole(actor);
    if (tuple != null) {
      PropertyIdValue occupationProperty = (PropertyIdValue) wikidataEntities.get(WikidataEntity.PROPERTY_OCCUPATION).getEntityId();
      EntityIdValue occupationValue = wikidataEntities.get(tuple.entity).getEntityId();
      return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, occupationProperty)
          .withReference(reference(wikidataEntities))
          .withValue(occupationValue)
          .build();
    }
    return null;
  }

  public Statement createValueStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String value) {
    return createValueStatement(wikidataEntities, propertyKey, value, "de");
  }

  public Statement createValueStatement(Map<String, EntityDocument> wikidataEntities, String propertyKey, String value, String language) {
    PropertyIdValue property = (PropertyIdValue) wikidataEntities.get(propertyKey).getEntityId();
    return StatementBuilder.forSubjectAndProperty(ItemIdValue.NULL, property)
        .withValue(Datamodel.makeMonolingualTextValue(value, language))
        .withReference(reference(wikidataEntities))
        .build();
  }

  private Reference reference(Map<String, EntityDocument> wikidataEntities) {
    Calendar cal = Calendar.getInstance();
    byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
    byte month = (byte) (cal.get(Calendar.MONTH) + 1);
    long year = cal.get(Calendar.YEAR);
    var timeValue = Datamodel.makeTimeValue(year, month, day, TimeValue.CM_GREGORIAN_PRO);

    return ReferenceBuilder.newInstance()
        .withPropertyValue((PropertyIdValue) wikidataEntities.get(WikidataEntity.PROPERTY_REFERENCE_URL).getEntityId(), Datamodel.makeStringValue(config.getFeedUrl()))
        .withPropertyValue((PropertyIdValue) wikidataEntities.get(WikidataEntity.PROPERTY_RETRIEVED).getEntityId(), timeValue)
        .build();
  }

  private Tuple getRole(ActorDTO actor) {
    RoleDTO roleDTO = actor.getRoles().stream().findFirst().orElseGet(() -> null);
    if (roleDTO == null) {
      return null;
    }

    String role = roleDTO.getRole();
    if (StringUtils.isBlank(role)) {
      return null;
    }

    if (StringUtils.contains(role, "Inszenierung")) {
      return new Tuple(WikidataEntity.PROPERTY_DIRECTOR, WikidataEntity.ENTITY_STAGING);
    }
    else if (StringUtils.containsAny(role, "Musikalische Leitung", "Dirigent")) {
      return new Tuple(WikidataEntity.PROPERY_MUSICAL_CONDUCTOR, WikidataEntity.ENTITY_CONDUCTOR);
    }
    else if (StringUtils.containsAny(role, "Bühnenbild", "Bühnenbildmitarbeit", "Szenische Einrichtung", "Künstlerische Mitarbeit Bühnenbild")) {
      return new Tuple(WikidataEntity.PROPERTY_SCENOGRAPHER, WikidataEntity.ENTITY_SCENOGRAPHER);
    }
    else if (StringUtils.containsAny(role, "Kostüme", "Kostümmitarbeit", "Ausstattung")) {
      return new Tuple(WikidataEntity.PROPERTY_COSTUME_DESIGNER, WikidataEntity.ENTITY_COSTUME_DESIGNER);
    }
    else if (StringUtils.contains(role, "Lichtgestaltung")) {
      return new Tuple(WikidataEntity.PROPERTY_LIGHTING_DESIGNER, WikidataEntity.ENTITY_LIGHTING_DESIGNER);
    }
    else if (StringUtils.contains(role, "Dramaturgie")) {
      return new Tuple(WikidataEntity.PROPERTY_DRAMATURGE, WikidataEntity.ENTITY_DRAMATURGE);
    }
    else if (StringUtils.contains(role, "Choreografie")) {
      return new Tuple(WikidataEntity.PROPERTY_CHOREOGRAPHER, WikidataEntity.ENTITY_CHOREOGRAPHER);
    }
    else if (StringUtils.contains(role, "Musik")) {
      return new Tuple(WikidataEntity.PROPERTY_PERFORMER, WikidataEntity.ENTITY_MUSICIAN);
    }
    else if (StringUtils.contains(role, "Sopran")) {
      return new Tuple(WikidataEntity.PROPERTY_PERFORMER, WikidataEntity.ENTITY_SOPRANO_SINGER);
    };

    return new Tuple(WikidataEntity.PROPERTY_PERFORMER, WikidataEntity.ENTITY_ARTIST);
  }

  private class Tuple {
    public final String property;
    public final String entity;
    
    public Tuple(String property, String entity) {
      this.property = property;
      this.entity = entity;
    }
  }

}

package ch.mscwi.wikidata.pipeline.controller.preparation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTOBuilder;

@Service
public class DataPreparer {

  @Value("${config.organizer}")
  private String organizer;

  public ActivityDTO toActivityDTO(Activity xmlActivity) {
    return new ActivityDTOBuilder()
        .withOriginId(xmlActivity.originId)
        .withTitle(xmlActivity.activityDetail.title)
        .withSubTitle(xmlActivity.activityDetail.subTitle)
        .withTitleEn(xmlActivity.activityDetailEnglish.title)
        .withSubTitleEn(xmlActivity.activityDetailEnglish.subTitle)
        .withLocation(xmlActivity.activityDetail.location)
        .withGenres(xmlActivity.activitySettings.genres)
        .withActors(PreparationUtils.consolidateCast(xmlActivity.activityDates))
        .withOrganizer(organizer)
        .build();
  }

}

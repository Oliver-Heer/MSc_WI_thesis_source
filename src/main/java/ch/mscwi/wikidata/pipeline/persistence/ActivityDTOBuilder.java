package ch.mscwi.wikidata.pipeline.persistence;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;

public class ActivityDTOBuilder {

  private long originId;
  private String title;
  private String titleEn;
  private String subTitle;
  private String subTitleEn;

  public ActivityDTOBuilder withOriginId(long originId) {
    this.originId = originId;
    return this;
  }

  public ActivityDTOBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public ActivityDTOBuilder withTitleEn(String titleEn) {
    this.titleEn = titleEn;
    return this;
  }

  public ActivityDTOBuilder withSubTitle(String subTitle) {
    this.subTitle = subTitle;
    return this;
  }

  public ActivityDTOBuilder withSubTitleEn(String subTitleEn) {
    this.subTitleEn = subTitleEn;
    return this;
  }

  public ActivityDTO build() {
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setOriginId(originId);
    activityDTO.setTitle(title);
    activityDTO.setSubTitle(subTitle);
    activityDTO.setTitleEn(titleEn);
    activityDTO.setSubTitleEn(subTitleEn);
    return activityDTO;
  }

  public static ActivityDTO toActivityDTO(Activity activity) {
    ActivityDTO activityDTO = new ActivityDTO();
    activityDTO.setOriginId(activity.originId);
    activityDTO.setTitle(activity.activityDetail.title);
    activityDTO.setSubTitle(activity.activityDetail.subTitle);
    activityDTO.setTitleEn(activity.activityDetailEnglish.title);
    activityDTO.setSubTitleEn(activity.activityDetailEnglish.subTitle);
    return activityDTO;
  }
}

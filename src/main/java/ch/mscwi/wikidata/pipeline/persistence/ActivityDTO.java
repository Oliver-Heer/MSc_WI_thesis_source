package ch.mscwi.wikidata.pipeline.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ActivityDTO {

  @Id
  private long originId;

  private String title;

  private String titleEn;

  private String subTitle;

  private String subTitleEn;

  public long getOriginId() {
    return originId;
  }

  public void setOriginId(long originId) {
    this.originId = originId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitleEn() {
    return titleEn;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public String getSubTitleEn() {
    return subTitleEn;
  }

  public void setSubTitleEn(String subTitleEn) {
    this.subTitleEn = subTitleEn;
  }
}

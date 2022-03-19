package ch.mscwi.wikidata.pipeline.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Cast;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;

public class DataPreparer {

  public static File prepare(List<Activity> activities) {
    Path tempFilePath = FileHandler.createTempFile();

    try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, Charset.forName("UTF-8"))) {

      String headerLine = new LineBuilder()
          .withTitle("Title")
          .withDescription("SubTitle")
          .withGenre("Genre")
          .withLocation("Location")
          .withOrganizer("Organizer")
          .withCastMember("Cast_Member")
          .withCastMemberRole("Cast_Member_Role")
          .withCastMemberRoleCategory("Cast_Member_Role_Category")
          .build();
      writeLine(writer, headerLine);

      writeActivities(writer, activities);

    } catch (IOException e) {
      // TODO
      e.printStackTrace();
    }

    return tempFilePath.toFile();
  }

  private static void writeActivities(BufferedWriter writer, List<Activity> activities) throws IOException {
    for (Activity activity : activities) {
      String activityLine = new LineBuilder()
          .withTitle(activity.activityDetail.title)
          .withDescription(activity.activityDetail.subTitle)
          .withGenre(getGenre(activity))
          .withLocation(activity.activityDetail.location.name)
          .withOrganizer(activity.organizer)
          .build();
      writeLine(writer, activityLine);

      writeCast(writer, activity.activityDates);
    }
  }

  private static void writeCast(BufferedWriter writer, List<ActivityDate> activityDates) throws IOException {
    Set<Cast> consolidatedCast = consolidateCast(activityDates);
    for (Cast castMember : consolidatedCast) {

      String castLine = new LineBuilder()
          .withCastMember(castMember.name)
          .withCastMemberRole(castMember.role)
          .withCastMemberRoleCategory(castMember.roleCategory)
          .build();
      writeLine(writer, castLine);
    }
  }

  private static Set<Cast> consolidateCast(List<ActivityDate> activityDates) {
    Set<Cast> consolidatedCast = new HashSet<>();
    activityDates.stream().forEach(date -> consolidatedCast.addAll(date.activityCast));
    return consolidatedCast;
  }

  private static void writeLine(BufferedWriter writer, String line) throws IOException {
    writer.write(line);
    writer.write(System.lineSeparator());
  }

  private static String getGenre(Activity activity) {
    List<Genre> genres = activity.activitySettings.genres;
    if(genres.isEmpty()) {
      return "";
    }
    return genres.get(0).name;
  }

  public static class LineBuilder {

    private static final String DELIMITER = ",";
    private static final String DOUBLE_QUOTE = "\"";

    private String title = "";
    private String description = "";
    private String genre = "";
    private String location = "";
    private String organizer = "";
    private String castMember = "";
    private String castMemberRole = "";
    private String castMemberRoleCategory = "";

    private boolean escapeDelimiter = true;
    private boolean cleanseLinebreaks = true;
    private boolean cleanseDoubleQuotes = true;

    public LineBuilder() {}

    public LineBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public LineBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public LineBuilder withGenre(String genre) {
      this.genre = genre;
      return this;
    }

    public LineBuilder withLocation(String location) {
      this.location = location;
      return this;
    }

    public LineBuilder withOrganizer(String organizer) {
      this.organizer = organizer;
      return this;
    }

    public LineBuilder withCastMember(String castMember) {
      this.castMember = castMember;
      return this;
    }

    public LineBuilder withCastMemberRole(String castMemberRole) {
      this.castMemberRole = castMemberRole;
      return this;
    }

    public LineBuilder withCastMemberRoleCategory(String castMemberRoleCategory) {
      this.castMemberRoleCategory = castMemberRoleCategory;
      return this;
    }

    public LineBuilder escapeDelimiter(boolean cleanse) {
      this.escapeDelimiter = cleanse;
      return this;
    }

    public LineBuilder cleanseLinebreaks(boolean cleanse) {
      this.cleanseLinebreaks = cleanse;
      return this;
    }

    public LineBuilder cleanseDoubleQuotes(boolean cleanse) {
      this.cleanseDoubleQuotes = cleanse;
      return this;
    }

    private String cleanse(String text) {
      String cleansedText = text;

      if (cleanseDoubleQuotes) {
        cleansedText = StringUtils.remove(cleansedText,  DOUBLE_QUOTE);
      }

      if (escapeDelimiter) {
        String quotedDelimiter = DOUBLE_QUOTE + DELIMITER + DOUBLE_QUOTE;
        cleansedText = StringUtils.replace(cleansedText, DELIMITER, quotedDelimiter);
      }

      if(cleanseLinebreaks) {
        cleansedText = StringUtils.remove(cleansedText, "\r\n");
        cleansedText = StringUtils.remove(cleansedText, "\n");
      }
      return cleansedText;
    }

    public String build() {
      StringJoiner stringJoiner = new StringJoiner(DELIMITER);
      stringJoiner.add(cleanse(title));
      stringJoiner.add(cleanse(description));
      stringJoiner.add(cleanse(genre));
      stringJoiner.add(cleanse(location));
      stringJoiner.add(cleanse(organizer));
      stringJoiner.add(cleanse(castMember));
      stringJoiner.add(cleanse(castMemberRole));
      stringJoiner.add(cleanse(castMemberRoleCategory));
      return stringJoiner.toString();
    }

  }

}

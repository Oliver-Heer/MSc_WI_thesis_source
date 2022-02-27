package ch.mscwi.wikidata.pipeline.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import ch.mscwi.wikidata.pipeline.model.kulturzueri.Genre;

public class DataPreparer {

  private static final String ZURICH_OPERA = "Zurich Opera";

  public static File prepare(List<Activity> activities) {
    Path tempFilePath = FileHandler.createTempFile();

    try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, Charset.forName("UTF-8"))) {

      writeHeader(writer, "Title, Description, Genre, Location, Organizer");

      for (Activity activity : activities) {
        writeActivity(writer, activity);
      }

    } catch (IOException e) {
      // TODO
      e.printStackTrace();
    }

    return tempFilePath.toFile();
  }

  private static void writeHeader(BufferedWriter writer, String header) throws IOException {
    writer.write(header);
    writer.write(System.lineSeparator());
  }

  private static void writeActivity(BufferedWriter writer, Activity activity) throws IOException {
    StringJoiner stringJoiner = new StringJoiner(",");
    stringJoiner.add(activity.activityDetail.title); // Title
    stringJoiner.add(activity.activityDetail.subTitle); // Description
    stringJoiner.add(getGenre(activity)); // Genre
    stringJoiner.add(activity.activityDetail.location.name); // Location
    stringJoiner.add(ZURICH_OPERA); // Organizer

    writer.write(stringJoiner.toString());
    writer.write(System.lineSeparator());
  }

  private static String getGenre(Activity activity) {
    List<Genre> genres = activity.activitySettings.genres;
    if(genres.isEmpty()) {
      return "";
    }
    return genres.get(0).name;
  }

}

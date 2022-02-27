package ch.mscwi.wikidata.pipeline.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class FileHandler {

  private static final String PROCURER_NAME = "Opernhaus_Zürich";

  public static Path createTempFile() {
    String fileName = PROCURER_NAME + " " + new Date();

    try {
      return Files.createTempFile(fileName, ".csv");
    } catch (IOException e) {
      // TODO
      e.printStackTrace();
    }
    return null;
  }

}

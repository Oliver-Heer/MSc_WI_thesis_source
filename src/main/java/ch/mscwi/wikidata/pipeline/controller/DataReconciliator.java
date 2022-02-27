package ch.mscwi.wikidata.pipeline.controller;

import java.io.File;
import java.net.URL;
import java.util.List;

import ch.mscwi.wikidata.pipeline.model.kulturzueri.Activity;
import gmbh.dtap.refine.client.RefineClient;
import gmbh.dtap.refine.client.RefineClients;
import gmbh.dtap.refine.client.command.CreateProjectResponse;
import gmbh.dtap.refine.client.command.GetCsrfTokenResponse;
import gmbh.dtap.refine.client.command.RefineCommands;

public class DataReconciliator {

  public static URL reconcile(List<Activity> activities) throws Exception {

    try (RefineClient client = RefineClients.create("http://localhost:3333")) {

      File csvFile = DataPreparer.prepare(activities);

      GetCsrfTokenResponse tokenResponse = RefineCommands.getCsrfToken().build().execute(client);

      CreateProjectResponse createProjectResponse = RefineCommands
          .createProject()
          .token(tokenResponse.getToken())
          .name(csvFile.getName())
          .file(csvFile)
          .build()
          .execute(client);

      return createProjectResponse.getLocation();
    }

  }

}

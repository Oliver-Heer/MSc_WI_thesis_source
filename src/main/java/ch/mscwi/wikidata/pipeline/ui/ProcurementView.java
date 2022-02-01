package ch.mscwi.wikidata.pipeline.ui;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import ch.mscwi.wikidata.pipeline.XmlProcurer;
import ch.mscwi.wikidata.pipeline.pojo.Activity;
import ch.mscwi.wikidata.pipeline.pojo.ActivityDate;
import ch.mscwi.wikidata.pipeline.pojo.ActivityMultimedia;
import ch.mscwi.wikidata.pipeline.pojo.ActivitySettings;
import ch.mscwi.wikidata.pipeline.pojo.Branch;
import ch.mscwi.wikidata.pipeline.pojo.Genre;
import ch.mscwi.wikidata.pipeline.pojo.Image;
import ch.mscwi.wikidata.pipeline.pojo.ImportActivities;
import ch.mscwi.wikidata.pipeline.pojo.Video;

public class ProcurementView extends VerticalLayout {

  public ProcurementView() {
    addClassName("procureView");
    setSizeFull();

    Button procureButton = new Button("Procure");
    procureButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Grid<Activity> activityGrid = activityGrid();
    activityGrid.setItemDetailsRenderer(new ComponentRenderer<>(activity -> detailView(activity)));

    procureButton.addClickListener(click -> {
      ImportActivities procurement = procure();
      activityGrid.setItems(procurement.activities);
    });

    add(procureButton, new Label("Activities"), activityGrid);
  }

  private Grid<Activity> activityGrid() {
    Grid<Activity> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    grid.addColumn(act -> act.originId).setHeader("OriginId");
    grid.addColumn(act -> act.originLastUpdatedAt).setHeader("OriginLastUpdatedAt");
    return grid;
  }

  private VerticalLayout detailView(Activity activity) {
    VerticalLayout layout = new VerticalLayout();
    layout.add(new Label("ActivityDetails"));
    layout.add(activityDetailGrid(activity));

    layout.add(new Label("ActivityDates"));
    layout.add(activityDatesGrid(activity));

    layout.add(new Label("ActivityMultimedia"));
    layout.add(activityMultimediaImageGrid(activity));
    layout.add(activityMultimediaVideoGrid(activity));

    layout.add(new Label("ActivitySettings"));
    layout.add(activitySettingsBranchGrid(activity));
    layout.add(activitySettingsGenreGrid(activity));

    return layout;
  }

  private Grid<Activity> activityDetailGrid(Activity activity) {
    Grid<Activity> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.setItems(activity);

    if (activity.activityDetail != null) {
      grid.addColumn(act -> act.activityDetail.title).setHeader("Title");
      grid.addColumn(act -> act.activityDetail.subTitle).setHeader("SubTitle");
      grid.addColumn(act -> act.activityDetail.originURL).setHeader("OriginURL");

      if (activity.activityDetail.location != null) {
        grid.addColumn(act -> act.activityDetail.location.id).setHeader("Location#Id");
        grid.addColumn(act -> act.activityDetail.location.name).setHeader("Location#Name");
      }
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private Grid<ActivityDate> activityDatesGrid(Activity activity) {
    Grid<ActivityDate> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    if (activity.activityDates != null) {
      grid.setItems(activity.activityDates);
      grid.addColumn(date -> date.originId).setHeader("OriginId");
      grid.addColumn(date -> date.originLastUpdatedAt).setHeader("OriginLastUpdatedAt");
      grid.addColumn(date -> date.startDate).setHeader("StartDate");
      grid.addColumn(date -> date.startTime).setHeader("StartTime");
      grid.addColumn(date -> date.endTime).setHeader("EndTime");
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private Grid<Image> activityMultimediaImageGrid(Activity activity) {
    Grid<Image> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    ActivityMultimedia activityMultimedia = activity.activityMultimedia;
    if (activityMultimedia != null) {
      List<Image> images = activityMultimedia.images;
      if (images != null) {
        grid.setItems(images);
        grid.addColumn(image -> image.url).setHeader("Url");
        grid.addColumn(image -> image.name).setHeader("Name");
        grid.addColumn(image -> image.credits).setHeader("Credits");
      }
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private Grid<Video> activityMultimediaVideoGrid(Activity activity) {
    Grid<Video> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    ActivityMultimedia activityMultimedia = activity.activityMultimedia;
    if (activityMultimedia != null) {
      List<Video> videos = activityMultimedia.videos;
      if (videos != null) {
        grid.setItems(videos);
        grid.addColumn(video -> video.url).setHeader("Url");
      }
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private Grid<Branch> activitySettingsBranchGrid(Activity activity) {
    Grid<Branch> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    ActivitySettings activitySettings = activity.activitySettings;
    if (activitySettings != null) {
      List<Branch> branches = activitySettings.branches;
      if (branches != null) {
        grid.setItems(branches);
        grid.addColumn(branch -> branch.originId).setHeader("OriginId");
        grid.addColumn(branch -> branch.name).setHeader("Name");
      }
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private Grid<Genre> activitySettingsGenreGrid(Activity activity) {
    Grid<Genre> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    ActivitySettings activitySettings = activity.activitySettings;
    if (activitySettings != null) {
      List<Genre> genres = activitySettings.genres;
      if (genres != null) {
        grid.setItems(genres);
        grid.addColumn(genre -> genre.originId).setHeader("OriginId");
        grid.addColumn(genre -> genre.name).setHeader("Name");
        grid.addColumn(genre -> genre.branchId).setHeader("BranchId");
      }
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private <T> void streamlineColumns(List<Column<T>> columns) {
    columns.stream().forEach(column -> column.setAutoWidth(true));
  }

  private ImportActivities procure() {
    try {
      return XmlProcurer.procure("https://www.opernhaus.ch/xmlexport/kzexport.xml");
    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
    return null;
  }

}
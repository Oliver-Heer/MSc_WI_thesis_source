package ch.mscwi.wikidata.pipeline.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.Activity;
import ch.mscwi.wikidata.pipeline.model.ActivityDate;
import ch.mscwi.wikidata.pipeline.model.ActivityDetail;
import ch.mscwi.wikidata.pipeline.model.ActivityMultimedia;
import ch.mscwi.wikidata.pipeline.model.ActivitySettings;
import ch.mscwi.wikidata.pipeline.model.Branch;
import ch.mscwi.wikidata.pipeline.model.Cast;
import ch.mscwi.wikidata.pipeline.model.Genre;
import ch.mscwi.wikidata.pipeline.model.Image;
import ch.mscwi.wikidata.pipeline.model.Video;

public class ProcurementView extends VerticalLayout {

  private Reactor reactor = Reactor.getReactor();

  public ProcurementView() {
    addClassName("procureView");
    setSizeFull();

    TextField procureUrl = new TextField();
    procureUrl.setClearButtonVisible(true);
    procureUrl.setValue("https://www.opernhaus.ch/xmlexport/kzexport.xml");
    procureUrl.setWidth("25%");

    Button procureButton = new Button("Procure");
    procureButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.setWidthFull();
    actionLayout.add(procureUrl, procureButton);

    Grid<Activity> activityGrid = activityGrid();
    activityGrid.setItemDetailsRenderer(new ComponentRenderer<>(activity -> detailView(activity)));

    procureButton.addClickListener(click -> {
      reactor.procure(procureUrl.getValue());
      activityGrid.setItems(reactor.activities);
    });

    add(actionLayout, new Label("Activities"), activityGrid);
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

    ActivityDetail activityDetail = activity.activityDetail;
    if (activityDetail != null) {
      grid.addColumn(act -> act.activityDetail.title).setHeader("Title");
      grid.addColumn(act -> act.activityDetail.subTitle).setHeader("SubTitle");
      grid.addColumn(act -> act.activityDetail.originURL).setHeader("OriginURL");

      if (activityDetail.location != null) {
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

    List<ActivityDate> activityDates = activity.activityDates;
    if (activityDates != null) {
      grid.setItems(activityDates);
      grid.addColumn(date -> date.originId).setHeader("OriginId");
      grid.addColumn(date -> date.originLastUpdatedAt).setHeader("OriginLastUpdatedAt");
      grid.addColumn(date -> date.startDate).setHeader("StartDate");
      grid.addColumn(date -> date.startTime).setHeader("StartTime");
      grid.addColumn(date -> date.endTime).setHeader("EndTime");

      grid.setItemDetailsRenderer(new ComponentRenderer<>(activityDate -> activityDateDetails(activityDate)));
    }
    streamlineColumns(grid.getColumns());
    return grid;
  }

  private VerticalLayout activityDateDetails(ActivityDate activityDate) {
    VerticalLayout layout = new VerticalLayout();
    layout.add(new Label("ActivityCast"));
    
    Grid<Cast> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    List<Cast> activityCast = activityDate.activityCast;
    if (activityCast != null) {
      grid.setItems(activityCast);
      grid.addColumn(date -> date.originId).setHeader("OriginId");
      grid.addColumn(date -> date.name).setHeader("Name");
      grid.addColumn(date -> date.role).setHeader("Role");
      grid.addColumn(date -> date.roleCategory).setHeader("RoleCategory");
      grid.addColumn(date -> date.IsStarRole).setHeader("IsStarRole");
      grid.addColumn(date -> date.sort).setHeader("Sort");
    }
    streamlineColumns(grid.getColumns());
    layout.add(grid);
    return layout;
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

}
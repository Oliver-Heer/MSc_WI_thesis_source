package ch.mscwi.wikidata.pipeline.ui;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ch.mscwi.wikidata.pipeline.XmlProcurer;
import ch.mscwi.wikidata.pipeline.pojo.Activity;
import ch.mscwi.wikidata.pipeline.pojo.ActivityDate;
import ch.mscwi.wikidata.pipeline.pojo.ImportActivities;

@Route(value = "")
@PageTitle("Pipeline | Kulturzüri")
public class ProcureView extends VerticalLayout {

  public ProcureView() {
    addClassName("procureView");
    setSizeFull();

    Button procureButton = new Button("Procure");

    procureButton.addClickListener(click -> {
      ImportActivities procurement = procure();
      Grid<Activity> activityGrid = activityGrid();
      activityGrid.setItems(procurement.activities);
      activityGrid.setItemDetailsRenderer(new ComponentRenderer<>(activity -> detailView(activity)));
      add(activityGrid);
    });

    add(procureButton);
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
    layout.add(activityDetailGrid(activity));
    layout.add(activityDatesGrid(activity));
    return layout;
  }

  private Grid<Activity> activityDetailGrid(Activity activity) {
    Grid<Activity> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.setItems(activity);

    if (activity.activityDetail != null) {
      grid.addColumn(act -> act.activityDetail.languageCode).setHeader("LanguageCode");
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
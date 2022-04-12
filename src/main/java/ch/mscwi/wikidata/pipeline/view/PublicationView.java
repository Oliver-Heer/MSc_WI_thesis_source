package ch.mscwi.wikidata.pipeline.view;

import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

public class PublicationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();

  public PublicationView() {
    addClassName("publicationView");
    setSizeFull();

    Grid<ActivityDTO> activityGrid = activityGrid();
    activityGrid.setItems(reactor.getActivitiesForReconciliation());
    activityGrid.addSelectionListener(event -> {
      Optional<ActivityDTO> selection = event.getFirstSelectedItem();
      if (selection.isPresent()) {
        new PublicationDialog(selection.get()).open();
      }
    });

    Button refreshButton = new Button("Refresh");
    refreshButton.addClickListener(click -> activityGrid.setItems(reactor.getActivitiesForReconciliation()));

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.setWidthFull();
    actionLayout.add(refreshButton);

    add(actionLayout, activityGrid);
  }

  private Grid<ActivityDTO> activityGrid() {
    Grid<ActivityDTO> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    grid.addColumn(act -> act.getStringID()).setHeader("ID");
    grid.addColumn(act -> act.getTitle()).setHeader("Title");
    grid.addColumn(act -> act.getTitleEn()).setHeader("Title En");
    grid.addColumn(act -> act.getSubTitle()).setHeader("Subtitle");
    grid.addColumn(act -> act.getSubTitleEn()).setHeader("Subtitle En");
    grid.addColumn(act -> act.getState()).setHeader("State");
    grid.addColumn(act -> act.getWikidataUid()).setHeader("Wikidata UID");

    UiUtils.streamlineColumns(grid.getColumns());
    return grid;
  }

}
package ch.mscwi.wikidata.pipeline.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

public class PreparationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();

  public PreparationView() {
    addClassName("preparationView");
    setSizeFull();

    Grid<ActivityDTO> activityGrid = activityGrid();
    activityGrid.setItems(reactor.getActivitiesForPreparation());

    Button reconcileButton = new Button("Reconcile");
    reconcileButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
    reconcileButton.addClickListener(click -> {
      reactor.reconcile();
      activityGrid.setItems(reactor.getActivitiesForPreparation());
    });

    Button refreshButton = new Button("Refresh");
    refreshButton.addClickListener(click -> activityGrid.setItems(reactor.getActivitiesForPreparation()));

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.setWidthFull();
    actionLayout.add(reconcileButton, refreshButton);

    add(actionLayout, new Label("Activities"), activityGrid);
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

    UiUtils.streamlineColumns(grid.getColumns());
    return grid;
  }

}
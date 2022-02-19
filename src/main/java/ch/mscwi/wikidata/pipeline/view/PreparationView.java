package ch.mscwi.wikidata.pipeline.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.Performance;
import ch.mscwi.wikidata.pipeline.model.wikidata.PerformanceWork;
import ch.mscwi.wikidata.pipeline.model.wikidata.Property;

public class PreparationView extends VerticalLayout {

  private Reactor reactor = Reactor.getReactor();

  public PreparationView() {
    addClassName("preparationView");
    setSizeFull();

    Grid<PerformanceWork> performanceWorkGrid = performanceWorkGrid();

    add(new Label("Performance Work"), performanceWorkGrid);
  }

  private Grid<PerformanceWork> performanceWorkGrid() {
    Grid<PerformanceWork> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.addColumn(performanceWork -> performanceWork.title);
    grid.setItemDetailsRenderer(new ComponentRenderer<>(performanceWork -> performanceWorkDetailView(performanceWork)));
    grid.setItems(reactor.performanceWorks);
    return grid;
  }

  private VerticalLayout performanceWorkDetailView(PerformanceWork performanceWork) {
    VerticalLayout layout = new VerticalLayout();
    layout.add(propertyGrid(performanceWork.getProperties()));

    layout.add(new Label("Performing Arts Production"));
    layout.add(propertyGrid(performanceWork.performingArtsProduction.getProperties()));

    layout.add(new Label("Performances"));
    List<Performance> performances = performanceWork.performingArtsProduction.getPerformances();
    performances.forEach(performance -> layout.add(propertyGrid(performance.getProperties())));

    return layout;
  }

  private Grid<Property> propertyGrid(List<Property> properties) {
    Grid<Property> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.setItems(properties);

    grid.addColumn(property -> property.pIdentifier);
    grid.addColumn(property -> property.label);
    grid.addColumn(property -> property.value);

    UiUtils.streamlineColumns(grid.getColumns());
    return grid;
  }

}
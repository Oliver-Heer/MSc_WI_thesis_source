package ch.mscwi.wikidata.pipeline.view;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.IWikidataObject;
import ch.mscwi.wikidata.pipeline.model.wikidata.Property;

public class PreparationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();

  public PreparationView() {
    addClassName("preparationView");
    setSizeFull();

    Grid<IWikidataObject> workGrid = performanceWorkGrid();

    add(workGrid);
  }

  private Grid<IWikidataObject> performanceWorkGrid() {
    Grid<IWikidataObject> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.addColumn(work -> work.getQIdentifier());
    grid.setItemDetailsRenderer(new ComponentRenderer<>(work -> workDetailView(work)));
    grid.setItems(reactor.works);
    return grid;
  }

  private VerticalLayout workDetailView(IWikidataObject work) {
    VerticalLayout layout = new VerticalLayout();
    layout.add(new Label("Work"));
    layout.add(propertyGrid(work.getProperties()));

    layout.add(new Label("Performing Arts Production"));
    work.getChildren().stream().forEach(child -> layout.add(propertyGrid(child.getProperties())));

    return layout;
  }

  private Grid<Property> propertyGrid(List<Property> properties) {
    Grid<Property> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.setItems(properties);

    grid.addColumn(property -> property.pIdentifier);
    grid.addColumn(property -> property.description);
    grid.addColumn(property -> property.value);

    UiUtils.streamlineColumns(grid.getColumns());
    return grid;
  }

}
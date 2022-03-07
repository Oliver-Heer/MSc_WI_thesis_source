package ch.mscwi.wikidata.pipeline.view;

import java.net.URL;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

public class ReconciliationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();

  public ReconciliationView() {
    addClassName("reconciliationView");
    setSizeFull();

    Button reconcileButton = new Button("Reconcile");
    reconcileButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Grid<URL> urlGrid = urlGrid();
    urlGrid.setItems(reactor.openRefineURLs);

    reconcileButton.addClickListener(click -> {
      reactor.reconcile();
      urlGrid.getDataProvider().refreshAll();
    });

    add(reconcileButton, urlGrid);
  }

  private Grid<URL> urlGrid() {
    Grid<URL> grid = new Grid<>();
    grid.setAllRowsVisible(true);

    grid.addComponentColumn(url -> {
      String urlString = url.toString();
      Anchor anchor = new Anchor();
      anchor.setText(urlString);
      anchor.setHref(urlString);
      anchor.setTarget("_blank");
      return anchor;
    });

    return grid;
  }

}

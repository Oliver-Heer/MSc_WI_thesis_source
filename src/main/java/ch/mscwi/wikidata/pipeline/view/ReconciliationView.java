package ch.mscwi.wikidata.pipeline.view;

import java.net.URL;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

public class ReconciliationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();

  public ReconciliationView() {
    addClassName("reconciliationView");
    setSizeFull();

    TextField openRefineUrl = new TextField();
    openRefineUrl.setClearButtonVisible(true);
    openRefineUrl.setValue("http://localhost:3333");
    openRefineUrl.setWidth("25%");
    openRefineUrl.setReadOnly(true);

    Button reconcileButton = new Button("Reconcile");
    reconcileButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Button refreshButton = new Button("Refresh");

    Grid<URL> urlGrid = urlGrid();
    urlGrid.setItems(reactor.openRefineURLs);

    reconcileButton.addClickListener(click -> {
      reactor.reconcile();
      urlGrid.getDataProvider().refreshAll();
    });

    refreshButton.addClickListener(click -> urlGrid.getDataProvider().refreshAll());

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.setWidthFull();
    actionLayout.add(openRefineUrl, reconcileButton, refreshButton);

    add(actionLayout, urlGrid);
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

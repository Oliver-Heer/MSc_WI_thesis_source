package ch.mscwi.wikidata.pipeline.view;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

public class ReconciliationView extends VerticalLayout {

  private Reactor reactor = Reactor.getReactor();

  public ReconciliationView() {
    addClassName("reconciliationView");
    setSizeFull();

    add(new Label("Reconciliation"));
  }

}
package ch.mscwi.wikidata.pipeline.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

public class ReconciliationView extends VerticalLayout {

  private Reactor reactor = Reactor.getReactor();

  public ReconciliationView() {
    addClassName("reconciliationView");
    setSizeFull();

    Button reconcileButton = new Button("Reconcile");
    reconcileButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    reconcileButton.addClickListener(click -> {
      reactor.reconcile();
    });

    add(new Label("Reconciliation"), reconcileButton);
  }

}

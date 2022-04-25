package ch.mscwi.wikidata.pipeline.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ch.mscwi.wikidata.pipeline.view.publication.PublicationView;
import ch.mscwi.wikidata.pipeline.view.reconciliation.ReconciliationView;

@Route(value = "")
@CssImport("./styles/global.css")
@PageTitle("Pipeline | Kulturzüri")
public class MainView extends VerticalLayout {

  private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

  public MainView() {
    setSizeFull();

    Tabs tabs = createTabs();
    tabs.setWidthFull();

    Div content = new Div();
    content.setWidthFull();

    tabs.addSelectedChangeListener(event -> {
      content.removeAll();
      content.add(tabComponentMap.get(event.getSelectedTab()));
    });
    content.add(tabComponentMap.get(tabs.getSelectedTab()));

    add(tabs, content);
  }

  private Tabs createTabs() {
    tabComponentMap.put(new Tab("Procurement"), new ProcurementView());
    tabComponentMap.put(new Tab("Reconciliation"), new ReconciliationView());
    tabComponentMap.put(new Tab("Publication"), new PublicationView());
    return new Tabs(tabComponentMap.keySet().toArray(new Tab[]{}));
  }

}

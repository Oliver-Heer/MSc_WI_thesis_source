package ch.mscwi.wikidata.pipeline.view.reconciliation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class ReconciliationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();
  private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

  public static String WIKIDATA_URL = "https://www.wikidata.org/wiki/";

  public ReconciliationView() {
    addClassName("reconciliationView");
    setSizeFull();

    Tabs tabs = createTabs();

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
    tabComponentMap.put(new Tab("Locations"), new ReconciliationLocationGrid(reactor.getLocationsForReconciliation()));
    tabComponentMap.put(new Tab("Genres"), new ReconciliationGenreGrid(reactor.getGenresForReconciliation()));
    tabComponentMap.put(new Tab("Actors"), new ReconciliationActorGrid(reactor.getActorsForReconciliation()));
    return new Tabs(tabComponentMap.keySet().toArray(new Tab[]{}));
  }

}
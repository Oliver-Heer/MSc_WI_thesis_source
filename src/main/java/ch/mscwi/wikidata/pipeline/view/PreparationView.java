package ch.mscwi.wikidata.pipeline.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.view.preparation.PreparationGenreGrid;

public class PreparationView extends VerticalLayout {

  private Reactor reactor = UiUtils.getReactor();
  private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

  public PreparationView() {
    addClassName("preparationView");
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
    tabComponentMap.put(new Tab("Genres"), new PreparationGenreGrid(reactor.getGenresForPreparation()));
    return new Tabs(tabComponentMap.keySet().toArray(new Tab[]{}));
  }

}
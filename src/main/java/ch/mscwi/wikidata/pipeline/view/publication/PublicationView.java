package ch.mscwi.wikidata.pipeline.view.publication;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class PublicationView extends VerticalLayout {

  public static String WIKIDATA_URL = "https://www.wikidata.org/wiki/";

  private Reactor reactor = UiUtils.getReactor();

  private PublicationActivityGrid activityGrid;

  public PublicationView() {
    addClassName("publicationView");
    setSizeFull();

    activityGrid = new PublicationActivityGrid(reactor.getActivitiesForPublication());

    add(createActionLayout(), activityGrid);
  }

  private HorizontalLayout createActionLayout() {
    Button refreshButton = new Button("Refresh");
    refreshButton.addClickListener(click -> activityGrid.updateItems(reactor.getActivitiesForPublication()));

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.add(refreshButton);
    return actionLayout;
  }

}
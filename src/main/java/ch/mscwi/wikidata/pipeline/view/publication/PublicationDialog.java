package ch.mscwi.wikidata.pipeline.view.publication;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;

public class PublicationDialog extends Dialog {

  public PublicationDialog(ActivityDTO activityDTO) {
    setWidth("50%");

    H3 title = new H3(activityDTO.getTitle());

    VerticalLayout dialogLayout = new VerticalLayout();
    dialogLayout.add(title);

    PublicationGenreGrid genreGrid = new PublicationGenreGrid(activityDTO.getGenres());
    PublicationActorGrid actorGrid = new PublicationActorGrid(activityDTO.getActors());

    add(dialogLayout,
        new H5("Genres"), genreGrid, 
        new HtmlComponent("br"),
        new H5("Actors"), actorGrid,
        createActionLayout());
  }

  private HorizontalLayout createActionLayout() {
    Button cancelButton = new Button("Cancel", click -> this.close());

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.add(cancelButton);
    return actionLayout;
  }

}

package ch.mscwi.wikidata.pipeline.view.publication;

import java.util.Collection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.textfield.TextField;

import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class PublicationGenreGrid extends Grid<GenreDTO> {

  public PublicationGenreGrid(Collection<GenreDTO> genres) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(genres);

    addColumn(entity -> entity.getName()).setHeader("Name");
    addColumn(entity -> entity.getState()).setHeader("State");

    TextField wikidataUid = new TextField();
    addComponentColumn(entity -> {
      Anchor anchor = new Anchor();
      anchor.setText(entity.getWikidataUid());
      anchor.setHref(PublicationView.WIKIDATA_URL + entity.getWikidataUid());
      anchor.setTarget("_blank");
      return anchor;
    }).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    UiUtils.streamlineColumns(getColumns());
  }

}

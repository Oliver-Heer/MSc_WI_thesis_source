package ch.mscwi.wikidata.pipeline.view.preparation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class PreparationGenreGrid extends Grid<GenreDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public PreparationGenreGrid(List<GenreDTO> genreDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(genreDTOs);

    addColumn(act -> act.getStringID()).setHeader("ID");
    addColumn(act -> act.getName()).setHeader("ID");
    addColumn(act -> act.getState()).setHeader("State");

    Binder<GenreDTO> binder = new Binder<>(GenreDTO.class);
    TextField wikidataUid = new TextField();
    binder.forField(wikidataUid)
        .asRequired("Wikidata UID")
        .withValidator(uid -> StringUtils.startsWith(uid, "Q"), "Invalid, has to start with Q")
        .bind(GenreDTO::getWikidataUid, GenreDTO::setWikidataUid);

    addColumn(GenreDTO::getWikidataUid).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<GenreDTO> editor = getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    Column<GenreDTO> editColumn = addComponentColumn(genre -> {
      Button editButton = new Button("Edit");
      editButton.addClickListener(e -> {
        if (editor.isOpen()) {
          editor.cancel();
        }
        getEditor().editItem(genre);
      });
      return editButton;
    });

    Button saveButton = new Button("Approve", e -> {
      GenreDTO editedGenre = editor.getItem();
      editor.save();
      reactor.approveAndSaveGenre(editedGenre);
    });
    saveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Button ignoreButton = new Button("Ignore", e -> {
      GenreDTO ignoredGenre = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveGenre(ignoredGenre);
    });

    Button cancelButton = new Button("Cancel", e -> editor.cancel());

    HorizontalLayout actionLayout = new HorizontalLayout(saveButton, ignoreButton, cancelButton);
    actionLayout.setPadding(false);
    editColumn.setEditorComponent(actionLayout);

    UiUtils.streamlineColumns(getColumns());
    editColumn.setAutoWidth(false);
    editColumn.setWidth("300px");
  }

}

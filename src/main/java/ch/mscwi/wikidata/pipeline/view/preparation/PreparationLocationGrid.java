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
import ch.mscwi.wikidata.pipeline.model.wikidata.LocationDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class PreparationLocationGrid extends Grid<LocationDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public PreparationLocationGrid(List<LocationDTO> locationDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(locationDTOs);

    addColumn(act -> act.getStringID()).setHeader("ID");
    addColumn(act -> act.getName()).setHeader("ID");
    addColumn(act -> act.getState()).setHeader("State");

    Binder<LocationDTO> binder = new Binder<>(LocationDTO.class);
    TextField wikidataUid = new TextField();
    binder.forField(wikidataUid)
        .asRequired("Wikidata UID")
        .withValidator(uid -> StringUtils.startsWith(uid, "Q"), "Invalid, has to start with Q")
        .bind(LocationDTO::getWikidataUid, LocationDTO::setWikidataUid);

    addColumn(LocationDTO::getWikidataUid).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<LocationDTO> editor = getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    Column<LocationDTO> editColumn = addComponentColumn(location -> {
      Button editButton = new Button("Edit");
      editButton.addClickListener(e -> {
        if (editor.isOpen()) {
          editor.cancel();
        }
        getEditor().editItem(location);
      });
      return editButton;
    });

    Button saveButton = new Button("Save", e -> {
      LocationDTO editedLocation = editor.getItem();
      editor.save();
      reactor.approveAndSaveLocation(editedLocation);
    });
    saveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Button ignoreButton = new Button("Ignore", e -> {
      LocationDTO ignoredLocation = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveLocation(ignoredLocation);
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

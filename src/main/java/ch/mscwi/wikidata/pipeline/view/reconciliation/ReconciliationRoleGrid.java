package ch.mscwi.wikidata.pipeline.view.reconciliation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.RoleDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class ReconciliationRoleGrid extends Grid<RoleDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public ReconciliationRoleGrid(List<RoleDTO> roleDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(roleDTOs);

    addColumn(entity -> entity.getOriginId()).setHeader("ID");
    addColumn(entity -> entity.getRole()).setHeader("Role");
    addColumn(entity -> entity.getState()).setHeader("State");

    Binder<RoleDTO> binder = new Binder<>(RoleDTO.class);
    TextField wikidataUid = new TextField();
    binder.forField(wikidataUid)
        .asRequired("Wikidata UID")
        .withValidator(uid -> StringUtils.startsWith(uid, "Q"), "Invalid, has to start with Q")
        .bind(RoleDTO::getWikidataUid, RoleDTO::setWikidataUid);

    addComponentColumn(entity -> {
      Anchor anchor = new Anchor();
      anchor.setText(entity.getWikidataUid());
      anchor.setHref(ReconciliationView.WIKIDATA_URL + entity.getWikidataUid());
      anchor.setTarget("_blank");
      return anchor;
    }).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<RoleDTO> editor = getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    Column<RoleDTO> editColumn = addComponentColumn(actor -> {
      Button editButton = new Button("Edit");
      editButton.addClickListener(e -> {
        if (editor.isOpen()) {
          editor.cancel();
        }
        getEditor().editItem(actor);
      });
      return editButton;
    });

    Button saveButton = new Button("Approve", e -> {
      RoleDTO editedActor = editor.getItem();
      editor.save();
      reactor.approveAndSaveRole(editedActor);
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

    Button ignoreButton = new Button("Ignore", e -> {
      RoleDTO ignoredActor = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveRole(ignoredActor);
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

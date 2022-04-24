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
import ch.mscwi.wikidata.pipeline.model.wikidata.ActorDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class ReconciliationActorGrid extends Grid<ActorDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public ReconciliationActorGrid(List<ActorDTO> actorDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(actorDTOs);

    addColumn(entity -> entity.getName()).setHeader("Name");
    addColumn(entity -> entity.getState()).setHeader("State");

    Binder<ActorDTO> binder = new Binder<>(ActorDTO.class);
    TextField wikidataUid = new TextField();
    binder.forField(wikidataUid)
        .asRequired("Wikidata UID")
        .withValidator(uid -> StringUtils.startsWith(uid, "Q"), "Invalid, has to start with Q")
        .bind(ActorDTO::getWikidataUid, ActorDTO::setWikidataUid);

    addComponentColumn(entity -> {
      Anchor anchor = new Anchor();
      anchor.setText(entity.getWikidataUid());
      anchor.setHref(ReconciliationView.WIKIDATA_URL + entity.getWikidataUid());
      anchor.setTarget("_blank");
      return anchor;
    }).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<ActorDTO> editor = getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    Column<ActorDTO> editColumn = addComponentColumn(actor -> {
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
      ActorDTO editedActor = editor.getItem();
      editor.save();
      reactor.approveAndSaveActor(editedActor);
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

    Button ignoreButton = new Button("Ignore", e -> {
      ActorDTO ignoredActor = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveActor(ignoredActor);
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

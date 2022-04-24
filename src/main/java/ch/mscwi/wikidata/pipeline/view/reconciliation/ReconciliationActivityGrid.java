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
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.view.UiUtils;

public class ReconciliationActivityGrid extends Grid<ActivityDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public ReconciliationActivityGrid(List<ActivityDTO> activityDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.NONE);
    setItems(activityDTOs);

    addColumn(entity -> entity.getStringID()).setHeader("ID");
    addColumn(entity -> entity.getTitle()).setHeader("Title");
    addColumn(entity -> entity.getSubTitle()).setHeader("Subtitle");
    addColumn(entity -> entity.getState()).setHeader("State");

    Binder<ActivityDTO> binder = new Binder<>(ActivityDTO.class);
    TextField wikidataUid = new TextField();
    binder.forField(wikidataUid)
        .asRequired("Wikidata UID")
        .withValidator(uid -> StringUtils.startsWith(uid, "Q"), "Invalid, has to start with Q")
        .bind(ActivityDTO::getWikidataUid, ActivityDTO::setWikidataUid);

    addComponentColumn(entity -> {
      Anchor anchor = new Anchor();
      anchor.setText(entity.getWikidataUid());
      anchor.setHref(ReconciliationView.WIKIDATA_URL + entity.getWikidataUid());
      anchor.setTarget("_blank");
      return anchor;
    }).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<ActivityDTO> editor = getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    Column<ActivityDTO> editColumn = addComponentColumn(activity -> {
      Button editButton = new Button("Edit");
      editButton.addClickListener(e -> {
        if (editor.isOpen()) {
          editor.cancel();
        }
        getEditor().editItem(activity);
      });
      return editButton;
    });

    Button saveButton = new Button("Approve", e -> {
      ActivityDTO editedActivity = editor.getItem();
      editor.save();
      reactor.approveAndSaveActivity(editedActivity);
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

    Button ignoreButton = new Button("Ignore", e -> {
      ActivityDTO ignoredActivity = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveActivity(ignoredActivity);
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

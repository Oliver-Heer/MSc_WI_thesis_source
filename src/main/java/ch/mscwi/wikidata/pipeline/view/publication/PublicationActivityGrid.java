package ch.mscwi.wikidata.pipeline.view.publication;

import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.view.ErrorNotification;
import ch.mscwi.wikidata.pipeline.view.UiUtils;
import ch.mscwi.wikidata.pipeline.view.reconciliation.ReconciliationView;

public class PublicationActivityGrid extends Grid<ActivityDTO> {

  private Reactor reactor = UiUtils.getReactor();

  public void updateItems(List<ActivityDTO> activityDTOs) {
    setItems(activityDTOs);
  }

  public PublicationActivityGrid(List<ActivityDTO> activityDTOs) {
    setAllRowsVisible(true);
    setSelectionMode(SelectionMode.SINGLE);
    setItems(activityDTOs);

    addColumn(entity -> entity.getStringID()).setHeader("ID");
    addColumn(entity -> entity.getTitle()).setHeader("Title");
    addColumn(entity -> entity.getSubTitle()).setHeader("Subtitle");
    addColumn(entity -> entity.getState()).setHeader("State");

    addComponentColumn(entity -> {
      Anchor anchor = new Anchor();
      anchor.setText(entity.getWikidataUid());
      anchor.setHref(ReconciliationView.WIKIDATA_URL + entity.getWikidataUid());
      anchor.setTarget("_blank");
      return anchor;
    }).setHeader("Wikidata UID");

    Editor<ActivityDTO> editor = getEditor();
    Binder<ActivityDTO> binder = new Binder<>(ActivityDTO.class);
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

    Button createButton = new Button("Create new", e -> {
      ActivityDTO editedActivity = editor.getItem();
      String newUid = reactor.createNewActivity(editedActivity);
      if (newUid == null) {
        new ErrorNotification();
        editor.cancel();
        return;
      }
      editedActivity.setWikidataUid(newUid);
      reactor.approveAndSaveActivity(editedActivity);
      editor.cancel();
    });

    Button ignoreButton = new Button("Ignore", e -> {
      ActivityDTO ignoredActivity = editor.getItem();
      editor.cancel();
      reactor.ignoreAndSaveActivity(ignoredActivity);
    });

    Button cancelButton = new Button("Cancel", e -> editor.cancel());

    HorizontalLayout actionLayout = new HorizontalLayout(createButton, ignoreButton, cancelButton);
    actionLayout.setPadding(false);
    editColumn.setEditorComponent(actionLayout);

    UiUtils.streamlineColumns(getColumns());
    editColumn.setAutoWidth(false);
    editColumn.setWidth("300px");

    addSelectionListener(event -> {
      Optional<ActivityDTO> selection = event.getFirstSelectedItem();
      if (selection.isPresent()) {
        new PublicationDialog(selection.get()).open();
      }
    });
  }

}

package ch.mscwi.wikidata.pipeline.view;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.GenreDTO;
import ch.mscwi.wikidata.pipeline.model.wikidata.ReconciliationState;

public class ReconciliationDialog extends Dialog {

  private Logger logger = LoggerFactory.getLogger(ReconciliationDialog.class);

  private Reactor reactor = UiUtils.getReactor();

  private Binder<ActivityDTO> binder = new Binder<>(ActivityDTO.class);

  public ReconciliationDialog(ActivityDTO activityDTO) {
    binder.setBean(activityDTO);

    H5 title = new H5(activityDTO.getTitle());
    TextField wikidataUid = new TextField("Wikidata UID");
    binder.forField(wikidataUid).bind(ActivityDTO::getWikidataUid, ActivityDTO::setWikidataUid);

    VerticalLayout dialogLayout = new VerticalLayout();
    dialogLayout.add(title, wikidataUid, genreGrid(activityDTO.getGenres()));

    add(dialogLayout, createActionLayout());
  }

  private Grid<GenreDTO> genreGrid(Collection<GenreDTO> genreDTOs) {
    Grid<GenreDTO> grid = new Grid<>();
    grid.setAllRowsVisible(true);
    grid.setSelectionMode(SelectionMode.NONE);
    grid.setItems(genreDTOs);

    grid.addColumn(GenreDTO::getStringID).setHeader("ID");
    grid.addColumn(GenreDTO::getName).setHeader("Name");
    grid.addColumn(GenreDTO::getState).setHeader("State");

    Binder<GenreDTO> genreBinder = new Binder<>(GenreDTO.class);
    TextField wikidataUid = new TextField();
    genreBinder.forField(wikidataUid)
            .asRequired("Wikidata UID")
            .bind(GenreDTO::getWikidataUid, GenreDTO::setWikidataUid);

    grid.addColumn(GenreDTO::getWikidataUid).setEditorComponent(wikidataUid).setHeader("Wikidata UID");

    Editor<GenreDTO> editor = grid.getEditor();
    editor.setBinder(genreBinder);
    editor.setBuffered(true);

    Column<GenreDTO> editColumn = grid.addComponentColumn(genre -> {
      Button editButton = new Button("Edit");
      editButton.addClickListener(e -> {
        if (editor.isOpen())
          editor.cancel();
        grid.getEditor().editItem(genre);
      });
      return editButton;
    });

    Button saveButton = new Button("Save", e -> editor.save()); // save?
    saveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Button removeButton = new Button("Remove", e -> editor.cancel()); // remove?

    Button cancelButton = new Button("Cancel", e -> editor.cancel()); // cancel?
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout actions = new HorizontalLayout(saveButton, removeButton, cancelButton);
    actions.setPadding(false);
    editColumn.setEditorComponent(actions);

    UiUtils.streamlineColumns(grid.getColumns());
    editColumn.setAutoWidth(false);
    editColumn.setWidth("300px");

    return grid;
  }

  private HorizontalLayout createActionLayout() {
    Button approveButton = new Button("Approve", click -> this.approve());
    approveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

    Button ignoreButton = new Button("Ignore", click -> this.ignore());

    Button cancelButton = new Button("Cancel", click -> this.close());

    HorizontalLayout actionLayout = new HorizontalLayout();
    actionLayout.add(approveButton, ignoreButton, cancelButton);
    return actionLayout;
  }

  private void approve() {
    // TODO Check dependent entities first, if any is still FOUND or NOT_FOUND return => check in REACTOR
    ActivityDTO activityDTO = binder.getBean();
    if (StringUtils.isBlank(activityDTO.getWikidataUid())) {
      return;
    }
    activityDTO.setState(ReconciliationState.APPROVED);
    logger.info("Approved entity " + activityDTO.getOriginId() + " " + activityDTO.getTitle() + " with Wikidata UID " + activityDTO.getWikidataUid());
    this.save();
  }

  private void ignore() {
    // TODO Check dependent entities first, if any is still FOUND or NOT_FOUND return
    ActivityDTO activityDTO = binder.getBean();
    activityDTO.setState(ReconciliationState.IGNORE);
    logger.info("Ignoring entity " + activityDTO.getOriginId() + " " + activityDTO.getTitle());
    this.save();
  }

  private void save() {
    ActivityDTO activityDTO = this.binder.getBean();
    reactor.saveActivity(activityDTO);
    this.close();
  }

}

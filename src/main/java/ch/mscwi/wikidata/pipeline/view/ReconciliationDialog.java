package ch.mscwi.wikidata.pipeline.view;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import ch.mscwi.wikidata.pipeline.controller.Reactor;
import ch.mscwi.wikidata.pipeline.model.wikidata.ActivityDTO;
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
    dialogLayout.add(title, wikidataUid);

    add(dialogLayout, createActionLayout());
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
    ActivityDTO activityDTO = binder.getBean();
    if (StringUtils.isBlank(activityDTO.getWikidataUid())) {
      return;
    }
    activityDTO.setState(ReconciliationState.APPROVED);
    logger.info("Approved entity " + activityDTO.getOriginId() + " " + activityDTO.getTitle() + " with Wikidata UID " + activityDTO.getWikidataUid());
    this.save();
  }

  private void ignore() {
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

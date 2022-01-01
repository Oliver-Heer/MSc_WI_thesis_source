package ch.mscwi.wikidata.pipeline.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import ch.mscwi.wikidata.pipeline.PipelineRunner;

@Route(value = "")
@PageTitle("Pipeline | Kulturzüri")
public class HomeView extends VerticalLayout {


	public HomeView() {
		addClassName("homeView");
		setSizeFull();
		
		Button runButton = new Button("Run");
		TextArea logTextArea = new TextArea();
		
		runButton.addClickListener(click -> {
			logTextArea.setValue(runPipeline());
		});
		
		add(runButton, logTextArea);
	}
	
	public String runPipeline() {
		try {
			return PipelineRunner.run();
		} catch (Exception e) {
			// TODO
			return e.getMessage();
		}
	}
}
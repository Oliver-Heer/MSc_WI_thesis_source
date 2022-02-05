package ch.mscwi.wikidata.pipeline.view;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@PWA(name = "Kulturzüri | Pipeline", shortName = "Pipeline")
@Theme(themeClass = Material.class, variant = Material.DARK)
public class AppShell implements AppShellConfigurator {

}

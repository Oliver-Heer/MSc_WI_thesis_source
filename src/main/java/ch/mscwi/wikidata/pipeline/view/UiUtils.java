package ch.mscwi.wikidata.pipeline.view;

import java.util.List;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.server.VaadinServlet;

import ch.mscwi.wikidata.pipeline.controller.Reactor;

public class UiUtils {

  private UiUtils() {}
  
  public static <T> void streamlineColumns(List<Column<T>> columns) {
    columns.stream().forEach(column -> column.setAutoWidth(true));
  }

  public static Reactor getReactor()
  {
    return WebApplicationContextUtils
        .getWebApplicationContext(VaadinServlet.getCurrent().getServletContext())
        .getBean(Reactor.class);
  }

}

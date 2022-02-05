package ch.mscwi.wikidata.pipeline.view;

import java.util.List;

import com.vaadin.flow.component.grid.Grid.Column;

public class UiUtils {

  private UiUtils() {}
  
  public static <T> void streamlineColumns(List<Column<T>> columns) {
    columns.stream().forEach(column -> column.setAutoWidth(true));
  }

}

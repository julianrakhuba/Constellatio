package file;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Region;

public abstract class NChart {
	public abstract Region getRegion();
	public abstract void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data);
}

package file;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public abstract class NChart {

	
	@SuppressWarnings("rawtypes")
	public abstract XYChart getChart();
	public abstract void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data);

}

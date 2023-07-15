package file;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })

public class NLineChart  extends NChart  {
	private CategoryAxis x = new CategoryAxis();
	private NumberAxis y = new NumberAxis();
	private LineChart chart;
	private NSheet nSheet;
	
	public NLineChart(NSheet nSheet) {
		this.nSheet = nSheet;
		chart = new LineChart(x, y);
		y.setMinorTickVisible(false);
		chart.setMinWidth(0);
		chart.setAnimated(false);
		chart.setVerticalGridLinesVisible(false);
		chart.setAlternativeRowFillVisible(true);		
//		chart.setOnMouseClicked(e -> nSheet.activateChart(this));
	}

	public XYChart getChart() {
		return chart;
	}
	
	public void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data) {
		x.getCategories().clear();
		x.setCategories(categories);
		chart.setData(data);
	}

}

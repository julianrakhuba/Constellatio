package file;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })

public class NLineChart  extends NChart  {
	private CategoryAxis x = new CategoryAxis();
	private NumberAxis y = new NumberAxis();
	private LineChart chart;
	private NSheet nSheet;
	
	private StackPane stackPane = new StackPane();
	
	public NLineChart(NSheet nSheet) {
		this.nSheet = nSheet;
		chart = new LineChart(x, y);
		stackPane.getChildren().add(chart);
		y.setMinorTickVisible(false);
		chart.setMinWidth(0);
		chart.setAnimated(false);
		chart.setVerticalGridLinesVisible(false);
		chart.setAlternativeRowFillVisible(true);		
//		chart.setOnMouseClicked(e -> nSheet.activateChart(this));
		stackPane.setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.05), 5, 0.4 , 2, 2);");

	}

	public Region getRegion() {
		return stackPane;
	}
	
	public void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data) {
		x.getCategories().clear();
		x.setCategories(categories);
		chart.setData(data);
	}

}

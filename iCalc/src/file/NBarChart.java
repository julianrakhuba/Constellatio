package file;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })

public class NBarChart extends NChart  {
	private CategoryAxis x = new CategoryAxis();
	private NumberAxis y = new NumberAxis();
	private BarChart chart;
	private NSheet nSheet;
//    private Timeline timeline = new Timeline();
	private StackPane stackPane = new StackPane();

	public NBarChart(NSheet nSheet) {
		this.nSheet = nSheet;
		chart = new BarChart(x, y);
		stackPane.getChildren().add(chart);

		y.setMinorTickVisible(false);
//		y.setVisible(false);
//		y.setTickLabelsVisible(false);
//		y.setOpacity(0.2);
		chart.setMinWidth(0);
		chart.setAnimated(false);
		chart.setVerticalGridLinesVisible(false);
//		chart.setHorizontalGridLinesVisible(false);
//		chart.setAlternativeRowFillVisible(true);	
//		chart.setVerticalZeroLineVisible(false);
//		chart.setHorizontalZeroLineVisible(true);
		
		//DIFFERENT
		chart.setBarGap(2);
		chart.setCategoryGap(10);
//		chart.setOnMouseClicked(e -> nSheet.activateChart(this));

//		y.setAutoRanging(false);
//		y.setUpperBound(300);
//		y.setTickUnit(50);
//		y.setLowerBound(0);

		
//        double yAxisWidth = 50; // Specify the desired width of the Y-axis
//        y.setPrefWidth(yAxisWidth);
		
		stackPane.setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.05), 5, 0.4 , 2, 2);");
		
		//			scrollPane.setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.05), 5, 0.4 , 2, 2); -fx-background-color: rgb(234, 236, 241); -fx-background-radius: 7;");
//				this.setStyle("-fx-effect:dropshadow(two-pass-box , white, 5, 0.4 , -2, -2); -fx-background-color: transparent; -fx-padding: 5 5 5 5;");
//			


		

	}


	public Region getRegion() {
		return stackPane;
	}


	public void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data) {

		x.getCategories().clear();
		x.setCategories(categories);
		
		
		chart.setData(data);
//		timeline.setOnFinished(e ->{
//			chart.layout();
//			
//
//		});
//		timeline.play();
		chart.layout();

		
		data.forEach(ser ->{
			ser.getData().forEach(dt ->{
				if(dt.getNode() != null) {

					Pane pn = (Pane) dt.getNode();
					pn.setOnMouseEntered( ent ->{
						dt.getNode().setStyle("-fx-effect: dropshadow(two-pass-box , #1E90FF, 2, 0.8 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 70.0%) 45.0%, derive(-fx-bar-fill, 0.3) 50.0%);");
					});
					
					pn.setOnMouseExited( ent ->{
						dt.getNode().setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 5, 0.0 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 40.0%) 45.0%, -fx-bar-fill 50.0%);");
					});
//					dt.getNode().setOnMouseEntered( ent ->{
//						dt.getNode().setStyle("-fx-effect: dropshadow(two-pass-box , #1E90FF, 5, 0.0 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 70.0%) 45.0%, derive(-fx-bar-fill, 30.0%) 50.0%);");
//					});
//					
//					dt.getNode().setOnMouseExited( ent ->{
//						dt.getNode().setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 5, 0.0 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 40.0%) 45.0%, -fx-bar-fill 50.0%);");
//					});
					
					
				}else {
					
				}
				
				
			});
		});
	}

}

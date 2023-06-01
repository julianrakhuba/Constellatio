package generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import logic.Field;
import pivot.PivotColumn;


public class NSheet extends Tab {
	private TableView<OpenBO> tableView = new TableView<OpenBO>();
	private HBox hbox = new HBox();
//	private SplitPane sp = new SplitPane();
	
	private BarChart<String, Number> chart;
	private LAY lay;
	private boolean calculateCells = false;
	
	public void setCalculateCells(boolean calculateCells) {
		this.calculateCells = calculateCells;
		if(!calculateCells) {
			lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
			lay.nnode.nmap.napp.getBottomBar().getCountLabel().clear();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public NSheet(LAY lay) {
		this.lay = lay;	
	    this.setText(lay.nnode.getTable() + " ");
		this.setOnClosed(e ->{
			lay.clearPopulation();
		});
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.setTableMenuButtonVisible(true);
		
		HBox.setHgrow(tableView, Priority.ALWAYS);
		
		hbox.getChildren().add(tableView);
//		sp.getItems().add(tableView);
		
		this.addChart();
		
//		this.addChardColor();
		this.setContent(hbox);	

		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {			
			
			if(calculateCells) {
				FilteredList<TablePosition> list = tableView.getSelectionModel().getSelectedCells().filtered(p ->  p.getTableColumn().getCellObservableValue(p.getRow()).getValue() instanceof Number);
				if(list.size() > 1 ) {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
					list.forEach(e -> lay.nnode.nmap.napp.getBottomBar().getSumLabel().add((Number) e.getTableColumn().getCellObservableValue(e.getRow()).getValue()));
				}else {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
				}
				int countSize = tableView.getSelectionModel().getSelectedCells().size();
				if(countSize > 1) {
					lay.nnode.nmap.napp.getBottomBar().getCountLabel().setCountValue(countSize);
				}else {
					lay.nnode.nmap.napp.getBottomBar().getCountLabel().clear();
				}
			}
			//does this belong here?? create method in column for usedData replacement
			if(tableView.getSelectionModel().getSelectedCells().size() == 1) {
				PivotColumn version =  (PivotColumn) tableView.getSelectionModel().getSelectedCells().get(0).getTableColumn().getUserData();
				if(version != null) {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().setText(version.getTip());
					version.pulseLay();
					tableView.getSelectionModel().getSelectedItem();
				}
			}
		});	
		
		tableView.getColumns().addListener(new ListChangeListener<TableColumn<OpenBO,?>>() {
		     public void onChanged(Change<? extends TableColumn<OpenBO,?>> change) {
		    	 change.next();
		    	 ArrayList<Field> fldz = new  ArrayList<Field>();
		    	 change.getAddedSubList().forEach((tc) ->{		    		 
		    		 PivotColumn version =  (PivotColumn) tc.getUserData();
		    		 if(version != null) {
						if(!fldz.contains(version.getField())) {
							fldz.add(version.getField());
						}
		    		 }
		    	 });
		    	 Collections.sort(lay.getSelectedFields(), Comparator.comparing(item -> fldz.indexOf(item)));
		     }
		});
	}
	
	

	public TableView<OpenBO> getTableView() {
        return tableView;
	}
	
	public LAY getLay() {
		return lay;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addChart() {
	        CategoryAxis xAxis = new CategoryAxis();
	        NumberAxis yAxis = new NumberAxis();
	        chart = new BarChart(xAxis, yAxis);

	        Series<String, Number> apples = new Series<>();
	        apples.setName("apples");
	        apples.getData().add(new Data("a", 567));
	        apples.getData().add(new Data("b", 1292));
	        apples.getData().add(new Data("c", 1200));
	        
	        Series<String, Number> lemons = new Series<>();
	        lemons.setName("lemons");
	        lemons.getData().add(new Data("a", 267));
	        lemons.getData().add(new Data("b", 1592));
	        lemons.getData().add(new Data("c", 1800));
	        
	        
	        Series<String, Number> oranges = new Series<>();
	        oranges.setName("oranges");
	        oranges.getData().add(new Data("a", 167));
	        oranges.getData().add(new Data("b", 1542));
	        oranges.getData().add(new Data("c", 1400));
	        
	        chart.getData().addAll(apples, lemons, oranges);
	        
//	        BarChart chart = new BarChart(xAxis, yAxis, barChartData);
	        chart.setAnimated(true);
	        chart.setBarGap(2);
	        chart.setCategoryGap(10);
//	        ScrollPane sp = new ScrollPane( chart);
	        
	        
	        hbox.getChildren().add(chart);
	        
	        
	        
//	        yAxis.getStyleableNode().setEffect(new GaussianBlur() );
//	        chart.
	        
//	        apples.getNode().setStyle("-fx-opacity: 0.2;");
	        
//	        apples.getData().forEach(data ->  {
////	        	data.getNode().setStyle("-fx-bar-fill: green;");
////	        	DropShadow shadow = new DropShadow();
////	        	shadow.setColor(Color.rgb(66, 108, 161, 1));//rgb(66, 108, 161)
//////	        	
////	        	shadow.setBlurType(BlurType.GAUSSIAN);
////	        	
//////	        	shadow.setInput(new GaussianBlur());
////	        	
////	        	
////	        	data.getNode().setEffect(shadow);
////	        	data.getNode().setStyle("-fx-bar-fill: linear-gradient(#fff, #61c2ff), radial-gradient(center 50% -40%, radius 200%, #dbf1ff 45%, #ade0ff 50%);");
//	        	
//	        	
////	        	data.getNode().set//.setParent(new Button("jj"));
//	        });
	        
	        // Set custom axis colors
//	        chart.getXAxis().setTickLabelFill(Paint.);
	        
	        // Create a gradient fill
//	        Stop[] stops = new Stop[] {   new Stop(0, Color.BLUE),  new Stop(1, Color.RED)  };
//	        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
//
//	        // Apply the gradient fill to the background of the stack pane
//	        stackPane.setBackground(new Background(new BackgroundFill(gradient, null, null)));

//	        chart.getXAxis().setTickLabelFill(gradient);
	        
//	        chart.getXAxis().setBackground(new Background(new BackgroundFill(gradient, null, null)));
	        chart.getStylesheets().add(getClass().getResource("/charts.css").toExternalForm());
//			this.getStylesheets().add(getClass().getResource("/Graph.css").toExternalForm());

	        
//	        chart.getl
//	        
//	        for (Legend.LegendItem legendItem : chart.getLegend().getItems()) {
//	            StackPane legendNode = (StackPane) legendItem.getSymbol();
//	            Rectangle legendSymbol = (Rectangle) legendNode.getChildren().get(0);
//	            Text legendText = (Text) legendNode.getChildren().get(1);
//
//	            legendSymbol.setFill(Color.RED); // Set the background color of the legend marker
//	            legendText.setFill(Color.BLUE); // Set the text color of the legend label
//	        }
//	        
//	        chart.
	       
//	        lemons.getData().forEach(data ->  data.getNode().setStyle("-fx-bar-fill: yellow;"));
//	        oranges.getData().forEach(data ->  data.getNode().setStyle("-fx-bar-fill: orange;"));

	}

	
}

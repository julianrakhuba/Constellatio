package generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
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
		
		
		this.addChart();
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
		  String[] years = {"2007", "2008", "2009"};
	        CategoryAxis xAxis = new CategoryAxis();
	        xAxis.setCategories(FXCollections.<String>observableArrayList(years));
	        NumberAxis yAxis = new NumberAxis("Units Sold", 0.0d, 3000.0d, 1000.0d);
	        
	        ObservableList<BarChart.Series> barChartData = FXCollections.observableArrayList(
	            new BarChart.Series("Apples", FXCollections.observableArrayList(
	               new BarChart.Data(years[0], 567d),
	               new BarChart.Data(years[1], 1292d),
	               new BarChart.Data(years[2], 1292d)
	            )),
	            new BarChart.Series("Lemons", FXCollections.observableArrayList(
	               new BarChart.Data(years[0], 956),
	               new BarChart.Data(years[1], 1665),
	               new BarChart.Data(years[2], 2559)
	            )),
	            new BarChart.Series("Oranges", FXCollections.observableArrayList(
	               new BarChart.Data(years[0], 1154),
	               new BarChart.Data(years[1], 1927),
	               new BarChart.Data(years[2], 2774)
	            ))
	        );
	        
	        BarChart chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
	        
	        hbox.getChildren().add(chart);
	}
	
}

package generic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import logic.Field;
import pivot.PivotColumn;


@SuppressWarnings({ "unchecked", "rawtypes" })

public class NSheet extends Tab {
	private TableView<OpenBO> tableView = new TableView<OpenBO>();
//	private HBox hbox = new HBox(5);
	private SplitPane splitPane = new SplitPane();
	private BarChart<String, Number> chart;
	private CategoryAxis xAxis = new CategoryAxis();
	private NumberAxis yAxis = new NumberAxis();
	private ObservableList<String> category = FXCollections.observableArrayList();
	private LAY lay;
	private boolean calculateCells = false;

	public void setCalculateCells(boolean calculateCells) {
		this.calculateCells = calculateCells;
		if (!calculateCells) {
			lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
			lay.nnode.nmap.napp.getBottomBar().getCountLabel().clear();
		}
	}

	public NSheet(LAY lay) {
		this.lay = lay;
		this.setText(lay.nnode.getTable() + " ");
		this.setOnClosed(e -> {
			lay.clearPopulation();
		});
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.setTableMenuButtonVisible(true);
		tableView.getStylesheets().add(getClass().getResource("/table.css").toExternalForm());
		
		HBox.setHgrow(tableView, Priority.ALWAYS);

		Pane backPane = new Pane();
		backPane.setStyle("-fx-background-radius: 7; -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 5, 0.0 , 0, 0); -fx-background-color: white;");
		StackPane stackPane = new StackPane(backPane, tableView);
		
//		this.setStyle("-fx-background-color: transparent;");
//		splitPane.setStyle("-fx-background-color: transparent; -fx-background-radius: 0 10 0 0;");
//		stackPane.setStyle("-fx-background-color: transparent;");

		stackPane.setPadding(new Insets(5));
		splitPane.getItems().add(stackPane);
		StackPane.setMargin(tableView, new Insets(10));
		
		stackPane.setMinWidth(0);
		
		this.createChart();
				
		splitPane.setPadding(new Insets(2));
		this.setContent(splitPane);

		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {

			if (calculateCells) {
				FilteredList<TablePosition> list = tableView.getSelectionModel().getSelectedCells().filtered(
						p -> p.getTableColumn().getCellObservableValue(p.getRow()).getValue() instanceof Number);
				if (list.size() > 1) {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
					list.forEach(e -> lay.nnode.nmap.napp.getBottomBar().getSumLabel()
							.add((Number) e.getTableColumn().getCellObservableValue(e.getRow()).getValue()));
				} else {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
				}
				int countSize = tableView.getSelectionModel().getSelectedCells().size();
				if (countSize > 1) {
					lay.nnode.nmap.napp.getBottomBar().getCountLabel().setCountValue(countSize);
				} else {
					lay.nnode.nmap.napp.getBottomBar().getCountLabel().clear();
				}
			}
			// does this belong here?? create method in column for usedData replacement
			if (tableView.getSelectionModel().getSelectedCells().size() == 1) {
				PivotColumn version = (PivotColumn) tableView.getSelectionModel().getSelectedCells().get(0)
						.getTableColumn().getUserData();
				if (version != null) {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().setText(version.getTip());
					version.pulseLay();
					tableView.getSelectionModel().getSelectedItem();
				}
			}
		});

		tableView.getColumns().addListener(new ListChangeListener<TableColumn<OpenBO, ?>>() {
			public void onChanged(Change<? extends TableColumn<OpenBO, ?>> change) {
				change.next();
				ArrayList<Field> fldz = new ArrayList<Field>();
				change.getAddedSubList().forEach((tc) -> {
					PivotColumn version = (PivotColumn) tc.getUserData();
					if (version != null) {
						if (!fldz.contains(version.getField())) {
							fldz.add(version.getField());
						}
					}
				});
				Collections.sort(lay.getSelectedFields(), Comparator.comparing(item -> fldz.indexOf(item)));
			}
		});
	}
	
	// CSV Export
	public void exportToCsv() {
		final FileChooser fileChooser = new FileChooser();		
		fileChooser.setTitle("Save CSV");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setInitialFileName(lay.nnode.getTableNameWUnderScr() + ".csv");
		File file = fileChooser.showSaveDialog(lay.nnode.nmap.napp.getStage());
		if (file != null) {
			StringBuilder fileString = new StringBuilder();
			ArrayList<PivotColumn>  vers = new ArrayList<PivotColumn>(lay.getVersions());
	        if(vers.size() > 0) {
	        	vers.forEach(col -> fileString.append(col.getLabel() + (vers.indexOf(col) < (vers.size()-1) ? "," : "\n")));
	        	tableView.getItems().forEach(bo -> {
	        		vers.forEach(version -> {
	        			SimpleObjectProperty<?> property = bo.getProperty(version);
	        			fileString.append((property.get() == null ? "null," : property.get()) + (vers.indexOf(version) < (vers.size()-1) ? "," : "\n"));
	        		});
	        	});
	        }
			try {
				Writer writer = new BufferedWriter(new FileWriter(file));
				writer.write(fileString.toString());
				writer.flush();
				writer.close();
			} catch (Exception ex) { ex.printStackTrace(); } 
		}
	}

	public TableView<OpenBO> getTableView() {
		return tableView;
	}
	
	public void createColumns() {
		ArrayList<TableColumn<OpenBO,?>> columns = new ArrayList<TableColumn<OpenBO,?>>();
		lay.getVersions().forEach(version -> columns.addAll(createColumn(version)));
		tableView.getColumns().addAll(columns);
	}
	private ArrayList<TableColumn<OpenBO,?>> createColumn(PivotColumn version) {
		ArrayList<TableColumn<OpenBO,?>> columns = new ArrayList<TableColumn<OpenBO,?>>();		
		if(version.getField().isString() || version.getField().isExcludedType()) {
			TableColumn<OpenBO, String> column;
			column = new TableColumn<OpenBO, String>(version.getLabel());
			column.setCellValueFactory(cell -> cell.getValue().getString(version.getAliase()));
			columns.add(column);
			column.setUserData(version);
			
		}else if(version.getField().isTime()) {
			TableColumn<OpenBO, Time> column = new TableColumn<OpenBO, Time>(version.getLabel());
			column.setCellValueFactory(cell -> cell.getValue().getTime(version.getAliase()));
			columns.add(column);
			column.setUserData(version);
		}else if(version.getField().isDate()) {
			TableColumn<OpenBO, Date> column = new TableColumn<OpenBO, Date>(version.getLabel());
			column.setCellValueFactory(cell -> cell.getValue().getDate(version.getAliase()));
			columns.add(column);
			column.setUserData(version);
		}else if(version.getField().isTimestamp()) {
			TableColumn<OpenBO, Timestamp> column = new TableColumn<OpenBO, Timestamp>(version.getLabel());
			column.setCellValueFactory(cell -> cell.getValue().getTimestamp(version.getAliase()));
			columns.add(column);
			column.setUserData(version);
		}else if(version.getField().isNumber()) {   
			TableColumn<OpenBO, Number> column = new TableColumn<OpenBO, Number>(this.formatColumnName(version));
			column.setCellValueFactory(cell -> cell.getValue().getNumber(version.getAliase()));
			column.setCellFactory(c -> new TableCellNumber(version));
			columns.add(column);
			column.setUserData(version);
		}
		return columns;
	}
	
	private String formatColumnName(PivotColumn version) {
		if(version.getPivotField() != null && version.getPivotField().isInt() && version.getPivotField().getFormat().getId().equals("month") && Integer.valueOf(version.getLabel()) > 0 && Integer.valueOf(version.getLabel()) <= 12) {			  
		return new DateFormatSymbols().getShortMonths()[Integer.valueOf(version.getLabel()) -1];
	}else if(version.getPivotField() != null && version.getPivotField().isInt() && version.getPivotField().getFormat().getId().equals("weekday") && Integer.valueOf(version.getLabel()) >=0 && Integer.valueOf(version.getLabel()) <=6) { 
		return new DateFormatSymbols().getShortWeekdays()[Integer.valueOf(version.getLabel()) +1];
	}else  {
		return version.getLabel();
	}
}

	public LAY getLay() {
		return lay;
	}


	private void createChart() {
	
		chart = new BarChart(xAxis, yAxis);
		chart.setMinWidth(0);
		xAxis.setCategories(category);
		chart.setAnimated(false);
		chart.setAlternativeRowFillVisible(true);
		chart.setBarGap(2);
		chart.setCategoryGap(10);
		splitPane.getItems().add(chart);
		chart.getStylesheets().add(getClass().getResource("/charts.css").toExternalForm());
		chart.setOnMouseClicked(e ->{
			this.refreshChart();
		});
		

	}

	public void refreshChart() {
		category.clear();
		chart.getData().clear();

		FilteredList<Field> pivotflds = lay.getSelectedFields().filtered(f -> f.isPivot());
		FilteredList<Field> groupflds = lay.getSelectedFields().filtered(f -> f.isGroupBy());
		FilteredList<Field> valueflds = lay.getSelectedFields().filtered(f -> f.isAgrigated());

		if(pivotflds.size() == 1 && groupflds.size() == 1 && valueflds.size() == 1) {//only allow chart for one pivot, one group and one aggregate
			category.addAll(pivotflds.get(0).getPivotCache());
//			System.out.println("CHACHE: " + pivotflds.get(0).getAliase() + "  " + pivotflds.get(0).getPivotCache());
			tableView.getItems().forEach(bo -> {
				chart.getData().addAll(bo.getSeries(lay));
			});
		}
		
	}

	public void clearPopulation() {
		tableView.getColumns().clear();
		tableView.getItems().clear();
	}

}

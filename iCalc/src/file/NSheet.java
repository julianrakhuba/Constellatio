package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import generic.LAY;
import generic.OpenBO;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.chart.Chart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
import javafx.stage.StageStyle;
import logic.Field;
import pivot.PivotColumn;
import status.VersionType;
import status.VisualStatus;

@SuppressWarnings({ "rawtypes" })

public class NSheet extends Tab {
	private TableView<OpenBO> tableView = new TableView<OpenBO>();
	private SplitPane splitPane = new SplitPane();
//	private ArrayList<NChart> charts = new ArrayList<NChart>();
	private NChart chart;
	private Property<VisualStatus> showChart = new SimpleObjectProperty<VisualStatus>(VisualStatus.UNAVALIBLE);


	
	private LAY lay;
	private boolean calculateCells = false;

	public NSheet(LAY lay) {
		this.lay = lay;
		this.setText(lay.nnode.getTable() + " ");
		this.setOnClosed(e -> {
			lay.clearPopulation();
		});
		
		HBox.setHgrow(tableView, Priority.ALWAYS);
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.setTableMenuButtonVisible(true);
		StackPane.setMargin(tableView, new Insets(10));
		
		Pane tableP = new Pane();
		StackPane tableSP = new StackPane(tableP, tableView);		
		if(lay.nnode.nmap.napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
			tableP.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.5);-fx-border-width: 0.5;-fx-border-color: derive(#1E90FF, 50%);-fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 8, 0.2, 0.0, 0.0);-fx-background-radius: 7;-fx-border-radius: 7;");
			tableSP.setStyle("-fx-padding: 5 0 5 0; -fx-min-width:0;");
		}else {
			tableP.setStyle("-fx-background-radius: 7; -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 5, 0.0 , 0, 0); -fx-background-color: white;");
			tableSP.setStyle("-fx-padding: 5 5 5 5; -fx-min-width:0;");
		}

		splitPane.getItems().addAll(tableSP);
		splitPane.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
		
//		scheetSplitPane.getDividers().get(0).setPosition(0.7);
		
		this.setContent(splitPane);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {

			if (calculateCells) {
				FilteredList<TablePosition> list = tableView.getSelectionModel().getSelectedCells().filtered(p -> p.getTableColumn().getCellObservableValue(p.getRow()).getValue() instanceof Number);
				if (list.size() > 1) {
					lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
					list.forEach(e -> lay.nnode.nmap.napp.getBottomBar().getSumLabel().add((Number) e.getTableColumn().getCellObservableValue(e.getRow()).getValue()));
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
				PivotColumn version = (PivotColumn) tableView.getSelectionModel().getSelectedCells().get(0).getTableColumn().getUserData();
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

		tableView.getSortOrder().addListener((ListChangeListener<? super TableColumn<?, ?>>) c -> {
			if (c.getList().size() > 0) {
				this.refreshChart();
			}
		});
	
		
		chart = new NBarChart(this);
//		charts.add(new NBarChart(this));
//		charts.add(new NLineChart(this));
	}

	public void setCalculateCells(boolean calculateCells) {
		this.calculateCells = calculateCells;
		if (!calculateCells) {
			lay.nnode.nmap.napp.getBottomBar().getSumLabel().clear();
			lay.nnode.nmap.napp.getBottomBar().getCountLabel().clear();
		}
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
			ArrayList<PivotColumn> vers = new ArrayList<PivotColumn>(lay.getVersions());
			if (vers.size() > 0) {
				vers.forEach(col -> fileString
						.append(col.getLabel() + (vers.indexOf(col) < (vers.size() - 1) ? "," : "\n")));
				tableView.getItems().forEach(bo -> {
					vers.forEach(version -> {
						SimpleObjectProperty<?> property = bo.getProperty(version);
						fileString.append((property.get() == null ? "null," : property.get())
								+ (vers.indexOf(version) < (vers.size() - 1) ? "," : "\n"));
					});
				});
			}
			try {
				Writer writer = new BufferedWriter(new FileWriter(file));
				writer.write(fileString.toString());
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public TableView<OpenBO> getTableView() {
		return tableView;
	}

	public void createColumns() {
		lay.getVersions().forEach(version -> {
			TableColumn<OpenBO, ?> col = version.getTableColumn();
			col.visibleProperty().addListener((a, b, c) -> {
				this.refreshChart();
			});
			
			col.sortTypeProperty().addListener((x, y, z) -> {
				this.refreshChart();
			});
			tableView.getColumns().add(col);
		});
	}

	public LAY getLay() {
		return lay;
	}
	
	public void clearPopulation() {
		tableView.getColumns().clear();
		tableView.getItems().clear();
	}
	
	
	//**************************************************
	//*				       Charts	   			       *
	//**************************************************
	
	
//	public void activateChart() {	
//		if(activechart == null) {
//			System.out.println("activate new chart");
//			activechart = ch;
//			splitPane.getItems().add(ch.getChart());//••
//		}
//		else if(activechart != ch){//will it ever be here?
//			System.out.println("activate different chart -> " + activechart + "  -> " + ch);
//			splitPane.getItems().removeIf(it ->it instanceof Chart);
//			splitPane.getItems().add(ch.getChart());
//			activechart = ch;			
//		}
////		else if(activechart == ch) {
////			NChart selChart = null;
////			System.out.println("next chart");
////			splitPane.getItems().removeIf(it ->it instanceof Chart);
////
////			if(charts.indexOf(ch) == 0) {
////				selChart = charts.get(1);
////			}else {
////				selChart = charts.get(0);
////			}
////			splitPane.getItems().add(selChart.getChart());
////			activechart = selChart;
////		}
//	}
	
//	public void toggleChartClick() {
//		
//		
//		
//		if(showChart.getValue() == VisualStatus.SHOW ) {
//			quadSplit.setBottomRight(null);
//			showChart.setValue(VisualStatus.HIDE);
//		}else {			
//			quadSplit.setBottomRight(testPane);
//			showChart.setValue(VisualStatus.SHOW);
//		}
//	}
	
	public void makeAvaliable() {
//		splitPane.getItems().add(chart.getChart());
//		this.lay.nnode.nmap.getNFile().getQuadSplit().setBottomRight(chart.getChart());
//		showChart.setValue(VisualStatus.SHOW);
		
		if(lay.isChartValid() && !splitPane.getItems().contains(chart.getChart())) {
			splitPane.getItems().add(chart.getChart());
		}
		
	}
	
	public void makeUnavaliable() {
		splitPane.getItems().removeIf(it ->it instanceof Chart);
//		showChart.setValue(VisualStatus.UNAVALIBLE);
//		this.lay.nnode.nmap.getNFile().getQuadSplit().setBottomRight(null);

	}
	
	public void refreshChart() { 
		if (lay.isChartValid()) {//Refresh
			chart.refresh(this.getCategories(), this.getData());
//			this.makeAvaliable();
//			show if not hidden not manualy hidden
		}else {//ROMOVE CHART
			this.makeUnavaliable();
		}
	}
	
	private ObservableList<String> getCategories() {
		ObservableList<String> categoryList = FXCollections.observableArrayList();
		tableView.getColumns().forEach(col -> {
			PivotColumn ver = (PivotColumn) col.getUserData();
			if (ver.getTableColumn().isVisible() &&(ver.getVersionType() == VersionType.PIVOT || !lay.isPivotLay())) {
				categoryList.add(col.getText());
			}
		});
		return categoryList;
	}

	private ObservableList<Series<String, Number>> getData() {
		ObservableList<Series<String, Number>> boSeries = FXCollections.observableArrayList();
		tableView.getItems().forEach(bo -> {
			Series<String, Number> series = new Series<String, Number>();
			// Get first GROUPBY column
			series.setName("" + bo.getProperty(lay.getVersions().get(0)).get());
			// Get values
			lay.getVersions().subList(1, lay.getVersions().size()).forEach(ver -> {
				//visible and (lay pivot zero or fied is pivot)
				if (ver.getTableColumn().isVisible() &&(ver.getVersionType() == VersionType.PIVOT || !lay.isPivotLay())) {
//					Data<String, Number> dt = new Data<String, Number>(ver.getLabelFarmated(), (Number) bo.getProperty(ver).get());
					Data<String, Number> dt = new Data<String, Number>();
					dt.setXValue(ver.getLabelFarmated());
					dt.setYValue((Number) bo.getProperty(ver).get());
					dt.setExtraValue((Number) bo.getProperty(ver).get());
//					Pane ndpane = new Pane();
//					ndpane.setOnMouseClicked(e ->{
//						System.out.println("node click: " + dt.getXValue()+": " + dt.getYValue());
//						e.consume();
//					});
//					
//					ndpane.setOnMouseEntered( ent ->{
//						ndpane.setScaleY(1.2);
//						ndpane.setStyle("-fx-effect: dropshadow(two-pass-box , #1E90FF, 5, 0.0 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 70.0%) 45.0%, derive(-fx-bar-fill, 30.0%) 50.0%);");
//					});
//					
//					ndpane.setOnMouseExited( ent ->{
//						ndpane.setScaleY(1);
//
//						ndpane.setStyle("-fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 5, 0.0 , 0, 0); -fx-background-color:  radial-gradient(center 50.0% -40.0%, radius 200.0%,  derive(-fx-bar-fill, 40.0%) 45.0%, -fx-bar-fill 50.0%);");
//					});
//					
//					
//					dt.setNode(ndpane);
					series.getData().add(dt);
				}
			});
			boSeries.add(series);
		});
		return boSeries;
	}

}

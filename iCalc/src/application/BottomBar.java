package application;

import file.NFile;
import file.NSheet;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class BottomBar extends ToolBar {
	private InfoLabel rowsCount = new InfoLabel("rows:");
	private InfoLabel sumLabel = new InfoLabel("sum:");
	private InfoLabel countLabel = new InfoLabel("count:");
	private Pane spacerA = new Pane();
	private HBox centerBar = new HBox();
	private Pane spacerB = new Pane();
	private ConnectionLight light = new ConnectionLight();
	
	private HBox centerBarA = new HBox();
	private HBox formaters = new HBox();
	
	private BottomButton chartBtn;
	private BottomButton chartTgl;

	private BottomButton consoleBtn;
	private BottomButton listBtn;
	private BottomButton gridBtn;
	
	
	
//	private Button addTest = new Button("+");
//	private Button removeTest = new Button("-");
//	private Button addTest = new Button("test");


	
	public BottomBar(Constellatio constellatio) {
		HBox.setHgrow(spacerA, Priority.SOMETIMES);
		HBox.setHgrow(spacerB, Priority.SOMETIMES);
		
		chartBtn = new BottomButton(constellatio,"chartButton");
		chartTgl = new BottomButton(constellatio,"chartToggleButton");
		consoleBtn = new BottomButton(constellatio,"consoleButton");
		listBtn = new BottomButton(constellatio,"listButton");
		gridBtn = new BottomButton(constellatio,"gridButton");
		
//		Separator sp = new Separator();
		this.getItems().addAll(spacerA, centerBar, spacerB,gridBtn,chartBtn,chartTgl,  new Separator(),  consoleBtn, listBtn, light);
		centerBar.setSpacing(3.0);		
		centerBarA.setSpacing(3.0);
		centerBarA.setAlignment(Pos.CENTER_LEFT);
		centerBarA.getChildren().addAll(rowsCount, sumLabel, countLabel);
		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);
		
		consoleBtn.setOnMouseClicked(e ->{
			constellatio.toggleConsole();
		});
		
		gridBtn.setOnMouseClicked(e ->{
			NFile f = constellatio.getFilemanager().getActiveNFile();
			if(f !=null) f.getTabManager().buttonClick();
		});
		
		chartBtn.setOnMouseClicked(e ->{
			NFile f = constellatio.getFilemanager().getActiveNFile();
			if(f !=null) {
			Tab currentTab = f.getTabManager().getSelectionModel().getSelectedItem();
				if(currentTab != null) {
					((NSheet) currentTab).showHideChart();
				}
			}
		});
		
		chartTgl.setOnMouseClicked(e ->{
			NFile f = constellatio.getFilemanager().getActiveNFile();
			if(f !=null) {
			Tab currentTab = f.getTabManager().getSelectionModel().getSelectedItem();
				if(currentTab != null) {
					((NSheet) currentTab).toggleChart();
				}
			}
		});
		
		listBtn.setOnMouseClicked(e ->{
			NFile f = constellatio.getFilemanager().getActiveNFile();
			if(f !=null) f.getSidePane().buttonClick();
		});
		
		
//		addTest.setOnMouseClicked(e ->{
//			if(constellatio.getFilemanager().getActiveNFile() != null) {
//				LAY lay = constellatio.getFilemanager().getActiveNFile().getActivity().getActiveLayer();
//				if(lay != null) {
//					lay.monitorSQL();
//				}
//			}			
//		});
//		
//		removeTest.setOnMouseClicked(e ->{
//			LAY lay = constellatio.getFilemanager().getActiveNFile().getActivity().getActiveLayer();
//			if(lay != null && constellatio.getFilemanager().getActiveNFile().getActivity() instanceof Edit) {
//				lay.getRootLevel().removeActiveArc();
//			}
//		});

		
		
		

	}
	
	public InfoLabel getSumLabel() {
		return sumLabel;
	}

	public InfoLabel getCountLabel() {
		return countLabel;
	}

	public InfoLabel getRowsCount() {
		return rowsCount;
	}

	public ConnectionLight getLight() {
		return light;
	}

	public HBox getFormaters() {
		return formaters;
	}

}

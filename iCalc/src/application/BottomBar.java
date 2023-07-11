package application;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
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
	
//	private Button showHideInfo = new Button();
//	private Button showHideBottom = new Button();
//	private Button showHideChart = new Button();
	
	
	public BottomBar(Constellatio constellatio) {
		HBox.setHgrow(spacerA, Priority.SOMETIMES);
		HBox.setHgrow(spacerB, Priority.SOMETIMES);
		QuadButton gridButton = new QuadButton(constellatio);
		this.getItems().addAll(spacerA, centerBar, spacerB, gridButton, new Separator(), light);
		centerBar.setSpacing(3.0);		
		centerBarA.setSpacing(3.0);
		centerBarA.setAlignment(Pos.CENTER_LEFT);
		centerBarA.getChildren().addAll(rowsCount, sumLabel, countLabel);
		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);
				
//		showHideInfo.getStyleClass().add("seachLightBlue");
//		showHideBottom.getStyleClass().add("gridLightBlue");
//		showHideChart.getStyleClass().add("gridLightBlue");
		
//		showHideInfo.setOnMouseClicked(e ->{
//			NFile f = constellatio.getFilemanager().getActiveNFile();
//			if(f !=null) f.infoPaneManager.buttonClick();
//		});
//		showHideBottom.setOnMouseClicked(e ->{
//			NFile f = constellatio.getFilemanager().getActiveNFile();
//			if(f !=null) f.tabManager.buttonClick();
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

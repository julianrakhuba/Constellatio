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
	private HBox bottomHideShowButtons = new HBox();
	private ConnectionLight light = new ConnectionLight();
	
	private HBox centerBarA = new HBox();
	private HBox formaters = new HBox();
	
	public BottomBar(Constellatio constellatio) {
		HBox.setHgrow(spacerA, Priority.SOMETIMES);
		HBox.setHgrow(spacerB, Priority.SOMETIMES);
		
		this.getItems().addAll(spacerA, centerBar, spacerB, new Separator(), bottomHideShowButtons, new Separator(), light);
		centerBar.setSpacing(3.0);		
		centerBarA.setSpacing(3.0);
		centerBarA.setAlignment(Pos.CENTER_LEFT);
		centerBarA.getChildren().addAll(rowsCount, sumLabel, countLabel);
		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);
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

	public HBox getBottomHideShowButtons() {
		return bottomHideShowButtons;
	}

	public HBox getFormaters() {
		return formaters;
	}

}

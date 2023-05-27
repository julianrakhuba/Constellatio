package application;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BottomToolBar extends ToolBar {
	private InfoLabel rowsCount = new InfoLabel("rows:");
	private InfoLabel sumLabel = new InfoLabel("sum:");
	private InfoLabel countLabel = new InfoLabel("count:");
	private Pane spacerA = new Pane();
	private HBox centerBar = new HBox();
	private Pane spacerB = new Pane();
	private HBox bottomHideShowButtons = new HBox();
	private ConnectionLight light = new ConnectionLight();
	
	private HBox centerBarA = new HBox();

	
	public BottomToolBar() {
		
	}

}

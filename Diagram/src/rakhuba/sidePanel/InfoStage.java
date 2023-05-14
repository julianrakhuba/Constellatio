package rakhuba.sidePanel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InfoStage extends Stage {
	private VBox stageVBox = new VBox();
	private Scene scene = new Scene(stageVBox, 220, 800);
	
	public InfoStage(Stage primaryStage) {
		this.setTitle("Layer Details");
		this.setAlwaysOnTop(false);
		this.setWidth(Screen.getPrimary().getVisualBounds().getWidth() * 0.16); 
		this.setHeight(primaryStage.getHeight());
		this.initStyle(StageStyle.DECORATED);
		this.initModality(Modality.WINDOW_MODAL);// to lock parent stage
		this.setScene(scene);
		this.centerOnScreen();
		this.getScene().getStylesheets().add(getClass().getResource("/Graph.css").toExternalForm());
		stageVBox.setPadding(new Insets(0, 5,5,10));
		stageVBox.setAlignment(Pos.TOP_CENTER);
    	this.setX(primaryStage.getX() + primaryStage.getWidth());    	
	}

	public VBox getRootPane() {
		return stageVBox;
	}
}

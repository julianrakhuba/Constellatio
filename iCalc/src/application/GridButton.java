package application;

import file.NFile;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GridButton extends VBox {

	private HBox upper = new HBox(2);
	private HBox lower = new HBox(2);
	
	private Pane map = new Pane();
	private Pane showHideInfo = new Pane();
	private Pane showHideBottom = new Pane();
	private Pane showHideChart = new Pane();
	
	public GridButton(Constellatio app) {
		super(2);
		this.getChildren().addAll(upper, lower);;
		upper.getChildren().addAll(map, showHideInfo);
		lower.getChildren().addAll(showHideBottom, showHideChart);

		//
		showHideInfo.setOnMouseClicked(e ->{
			NFile f = app.getFilemanager().getActiveNFile();
			if(f !=null) f.infoPaneManager.buttonClick();
		});
		showHideBottom.setOnMouseClicked(e ->{
			NFile f = app.getFilemanager().getActiveNFile();
			if(f !=null) f.tabManager.buttonClick();
		});
		//style
		map.getStyleClass().add("gridButtonGray");
		showHideInfo.getStyleClass().add("gridButtonBlue");
		showHideBottom.getStyleClass().add("gridButtonBlue");
		showHideChart.getStyleClass().add("gridButtonBlue");
		
		
	}
}

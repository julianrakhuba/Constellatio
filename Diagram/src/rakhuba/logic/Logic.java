package rakhuba.logic;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rakhuba.generic.LAY;

public class Logic extends VBox {
	private LAY lay;
	
	private Pane pane = new Pane(this);
	
	public Logic(LAY lay) {
		this.lay = lay;
		this.setMinSize(15, 15);
		this.setSpacing(3.0);
		this.setOpacity(0.9);
		this.getStyleClass().add("logic");
		this.setLayoutY(10);
		pane.setPickOnBounds(false);
	}
	
	public void hide() {
		lay.nnode.nmap.getNFile().stackPane.getChildren().remove(pane);
	}
	
	public void show() {
		 lay.nnode.nmap.getNFile().stackPane.getChildren().add(pane);
	}

}

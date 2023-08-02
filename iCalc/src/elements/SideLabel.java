package elements;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SideLabel extends Label {
	private Pane pane = new Pane();
	

	public SideLabel() {
		super();
		this.setStyle(" -fx-font-size: 12; -fx-text-fill: #525e6b;");
		this.setGraphic(pane);
		pane.setMinSize(14, 14);
		pane.setMaxSize(14, 14);		
	}

	public Pane getPane() {
		return pane;
	}
	
    public void styleSelected() {
    	pane.setId("list-Item-Blue");
    }
    
    public void styleUnselected() {
    	pane.setId("list-Item-Gray");
    }

}

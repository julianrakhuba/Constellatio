package sidePanel;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import search.NVal;

public class ValueCell extends ListCell<NVal> {
	
    public ValueCell() {
    	this.setPadding(new Insets(6,4,6,4));
	}

    public void updateItem(NVal value, boolean empty) {
        super.updateItem(value, empty);        
        this.setOnMouseClicked(e -> {
        	if(value != null) {
        		value.click(e);
        	}
        });
        
        if (empty) {
        	this.setText(null);
            this.setId(null);
            this.setGraphic(null);
        } else {
        	this.setGraphic(value.getLabel());
        }
    }
	
}
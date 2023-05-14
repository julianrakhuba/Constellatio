package rakhuba.configure;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;

public class NKeyCell extends ListCell<NLink> {
	
    public NKeyCell() {
    	this.setPadding(new Insets(6,4,6,4));
	}

    public void updateItem(NLink value, boolean empty) {
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
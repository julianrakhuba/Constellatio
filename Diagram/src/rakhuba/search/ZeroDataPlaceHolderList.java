package rakhuba.search;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ZeroDataPlaceHolderList extends ListView<String> {

	public ZeroDataPlaceHolderList() {
		this.setEditable(true);
		this.setFocusTraversable(false);
		this.getItems().addAll(" ");
		this.setCellFactory(param -> new MyCell());
		this.setStyle("-fx-background-insets: 0 ;");
    	this.setStyle("-fx-padding: 0px;");
    	
		this.setPrefHeight(200);
	}
	
	private class MyCell extends ListCell<String> {
		
	    public MyCell() {
	    	this.setPadding(new Insets(6,4,6,4));
	    	this.setStyle(" -fx-font-size: 10;");
		}

	    public void updateItem(String txt, boolean empty) {
	        super.updateItem(txt, empty);
	        if (empty) {
	        	this.setText(null);
	            this.setId(null);
	            this.setGraphic(null);
	        } else {
	        	this.setText(txt);
	        }
	    }
	    
	}
}

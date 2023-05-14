package rakhuba.application;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class JainLabel extends Label {
	private JoinLine joinLine;
	private Pane pane = new Pane();
	private String relationship;

	
	public JainLabel(JoinLine joinLine, String relationship) {
		this.joinLine = joinLine;
		this.relationship = relationship;
		pane.setMinSize(17,17);
		pane.setMaxSize(17,17);
		pane.setOnMouseClicked(e -> joinLine.joinClick());  
    	this.setGraphic(pane);
    	
    	if(relationship.equals("parent")) {
    		this.setStyle("-fx-font-size: 10; -fx-text-fill: #525e6b;");
        	this.setTooltip(new Tooltip(joinLine.getFromLay().getAliase()));
        	this.setText(" " + joinLine.getFromLay().nnode.getTable());
    	}else {
    		this.setStyle(" -fx-font-size: 10; -fx-padding: 0 5 0 0; -fx-background-radius: 15 15 15 15;  -fx-text-fill: #ababab;");
        	this.setTooltip(new Tooltip(joinLine.getToLay().getAliase()));
        	this.setText(" " + joinLine.getToLay().nnode.getTable());
    	}
	}
	
	public JoinLine getJoinLine() {
		return joinLine;
	}

	public Pane getPane() {
		return pane;
	}

	public void styleAsParent() {
		pane.getStyleClass().clear();
		pane.getStyleClass().add("joinBase");
    	pane.getStyleClass().add(joinLine.getFromLay().getSqlType().toString() + "_" +joinLine.getJoinType().toString());
	}

	public void styleAsChild() {		
		pane.getStyleClass().clear();
		pane.getStyleClass().add("joinBase");
    	pane.getStyleClass().add(joinLine.getToLay().getSqlType().toString() + "_" +joinLine.getJoinType().toString());
	}

	public String getRelationship() {
		return relationship;
	}
}

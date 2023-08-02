package application;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
//import rakhuba.application.JoinLine;
import javafx.stage.StageStyle;

public class JainLabel extends Label {
	private JoinLine joinLine;
	private Pane pane = new Pane();
	private String relationship;

	
	public JainLabel(JoinLine joinLine, String relationship) {
		this.joinLine = joinLine;
		this.relationship = relationship;
		pane.setMinSize(14,14);
		pane.setMaxSize(14, 14);
		pane.setOnMouseClicked(e -> joinLine.joinClick(e));  
    	this.setGraphic(pane);
    	 
    	if(relationship.equals("parent")) {
    		
//    		 -fx-text-fill: #9DA1A1;
    		 
    		this.setStyle("-fx-font-size: 12; -fx-text-fill: #525e6b;");
        	this.setTooltip(new Tooltip(joinLine.getFromLay().getAliase()));
        	this.setText(" " + joinLine.getFromLay().nnode.getTable());
    	}else {
    		this.setStyle(" -fx-font-size: 12; -fx-padding: 0 5 0 0; -fx-background-radius: 15 15 15 15;  -fx-text-fill: #525e6b; ;");
        	this.setTooltip(new Tooltip(joinLine.getToLay().getAliase()));
        	this.setText(" « " + joinLine.getToLay().nnode.getTable());
    	}
    	
    	//NEED MORE WORK TEMP FIX
    	if(joinLine.getFromLay().nnode.nmap.napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
    		this.setStyle(" -fx-font-size: 12; -fx-padding: 0 5 0 0; -fx-background-radius: 15 15 15 15;  -fx-text-fill: #9DA1A1;");
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

/*******************************************************************************
 * /*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *  *******************************************************************************/
 *******************************************************************************/
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
        	this.setText(" " + joinLine.getFromLay().getNnode().getTable());
    	}else {
    		this.setStyle(" -fx-font-size: 12; -fx-padding: 0 5 0 0; -fx-background-radius: 15 15 15 15;  -fx-text-fill: #525e6b; ;");
        	this.setTooltip(new Tooltip(joinLine.getToLay().getAliase()));
        	this.setText(" « " + joinLine.getToLay().getNnode().getTable());
    	}
    	
    	//NEED MORE WORK TEMP FIX
    	if(joinLine.getFromLay().getNnode().getNmap().getNapp().getStage().getStyle() == StageStyle.TRANSPARENT) {
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

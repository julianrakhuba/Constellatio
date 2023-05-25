package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.CubicCurve;
//import rakhuba.application.Nnode;

public class NnodeLine extends CubicCurve {
	private Nnode startNnode;
	private Nnode endNnode;
	
	public NnodeLine(Nnode startNnode, Nnode endNnode) {
		this.startNnode = startNnode; 
		this.endNnode = endNnode;
		updateLayout();
		this.getStyleClass().add("dotedLine");
	}

	// ••••••••••••••••••••••••••
	public void updateLayout() {
		double startX = startNnode.getCenterX();
		double startY = startNnode.getCenterY();
		double endX =  endNnode.getCenterX();
		double endY =  endNnode.getCenterY();
		setStartX(startX);
		setStartY(startY);
		setEndX(endX);
		setEndY(endY);
		setControlY1(startY);
		setControlY2(endY);
		
		//Curved lines
		setControlX1((startX + endX)/2); 
		setControlX2((startX + endX)/2);
		
		//Flat lines
//		setControlX1(startX); 
//		setControlX2(endX);
	}
	
	public void createGredient(String fromColor, String toColor) {
		setStroke(new LinearGradient(this.getStartX(), this.getStartY(), this.getEndX(), this.getEndY(),  false, CycleMethod.NO_CYCLE,new Stop(1,Color.valueOf(fromColor)), new Stop(0.1,Color.valueOf(toColor))));// Light to Dark
	}
}
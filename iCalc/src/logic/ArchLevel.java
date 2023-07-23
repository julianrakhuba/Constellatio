package logic;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;

public class ArchLevel extends Group {
	ArrayList<Arc> arcs = new ArrayList<Arc>();
	
	public ArchLevel(double x, double y, int rad, int slices) {
		double anglePerSlice = 360.0 / slices;
//	  	Circle cir = new Circle(x, y, 5);
//    	cir.setFill(Color.WHITESMOKE);
//		cir.setStyle("-fx-stroke: #1E90FF; -fx-stroke-width: 1px; -fx-fill: rgba(255, 255, 255, 1); -fx-effect: innershadow(gaussian, derive(#1E90FF, 15%) , 1, 0.4, 0.0, 0.0);");
		// Create and add the slices to the root group
		for (int i = 0; i < slices; i++) {
			double startAngle = i * anglePerSlice - anglePerSlice / 2;
			double length = anglePerSlice * ((slices == 1)? 1 : 0.85); // Length of each slice (90% of anglePerSlice)
			Arc slice = new Arc(x, y, rad, rad, startAngle, length);
			slice.setType(ArcType.OPEN);
			slice.setStrokeLineCap(StrokeLineCap.BUTT);
			
			slice.setPickOnBounds(false);

			
//			slice.setStyle("-fx-stroke: #1E90FF; -fx-stroke-width: 0px; -fx-fill: rgba(255, 255, 255, 1); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 5%) , 3, 0.4, 0.0, 0.0);");
			slice.setStyle("-fx-stroke: rgba(255, 255, 255, 0.9); -fx-stroke-width: 5px; -fx-fill: transparent; -fx-effect: dropshadow(gaussian, derive(#1E90FF, 60%) , 3, 0.2, 0.0, 0.0);");
			this.getChildren().add(slice);
			arcs.add(slice);
		}		
	}

	public ArrayList<Arc> getArcs() {
		return arcs;
	}
}

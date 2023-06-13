package application;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class NCircle extends Circle {
	private Nnode nnode;
	private Timeline showTimeLine;
	private Timeline hideTimeLine;


	public NCircle(Nnode nnode, String color, Number radius) {
		this.nnode = nnode;
		this.layoutXProperty().bind(nnode.layoutXProperty().add(10));
		this.layoutYProperty().bind(nnode.layoutYProperty().add(10));
		this.setOpacity(0);
		this.setScaleX(0.5);
		this.setScaleY(0.5);
		this.setStroke(Color.WHITE);
		this.setFill(null);
		if (nnode.nmap.napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			this.setStyle("-fx-effect: dropshadow(gaussian, derive(" + color + ", 5%) , 10, 0.8, 0.0, 0.0);");
			this.setRadius(radius.doubleValue());
		} else {
			this.setStyle("-fx-effect: dropshadow(gaussian, derive(" + color + ", 5%) , 7, 0.4, 0.0, 0.0);");
			this.setRadius(radius.doubleValue());
		}
	}

	public void show() {
		//check if timeline form is
		System.out.println("•[SHOW]");
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();
		
		
		if (nnode.nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected()) {
			System.out.println("••[SHOW PLAY]");
			if(!nnode.nmap.contains(this)) nnode.nmap.add(this);
//			this.setOpacity(0);
//			this.setScaleX(0.5);
//			this.setScaleY(0.5);

			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleXProperty(), 1));
			KeyFrame kf3 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleYProperty(), 1));

			showTimeLine = new Timeline();
			showTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);
		    showTimeLine.setCycleCount(1);
		    showTimeLine.play();
		}
	}

	public void hide() {
		System.out.println("•[HIDE]");

		if (nnode.nmap.contains(this)) {
			System.out.println("••[HIDE PLAY]");

			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 0));
			KeyFrame kf2 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleXProperty(), 0.5));
			KeyFrame kf3 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleYProperty(), 0.5));

		    hideTimeLine = new Timeline();
		    hideTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);
		    hideTimeLine.setCycleCount(1);
		    hideTimeLine.setOnFinished(e -> {
				nnode.nmap.remove(this);
			});
		    hideTimeLine.play();
		}
	}

}






//package application;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.KeyValue;
//import javafx.animation.Timeline;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.util.Duration;
//
//public class NCircle extends Circle {
//	private Nnode nnode;
//
//	public NCircle(Nnode nnode, String color, Number radius) {
//		this.nnode = nnode;
//		this.layoutXProperty().bind(nnode.layoutXProperty().add(10));
//		this.layoutYProperty().bind(nnode.layoutYProperty().add(10));
//
//		this.setStroke(Color.WHITE);
//		this.setFill(null);
//		if (nnode.nmap.napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
//			this.setStyle("-fx-effect: dropshadow(gaussian, derive(" + color + ", 5%) , 10, 0.8, 0.0, 0.0);");
//			this.setRadius(radius.doubleValue());
//		} else {
//			this.setStyle("-fx-effect: dropshadow(gaussian, derive(" + color + ", 5%) , 7, 0.4, 0.0, 0.0);");
//			this.setRadius(radius.doubleValue());
//		}
//	}
//
//	public void show() {
//		//check if timeline form is
//		if (!nnode.nmap.contains(this) && nnode.nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected()) {
//			this.setOpacity(0);
//			this.setScaleX(0.5);
//			this.setScaleY(0.5);
//
//			nnode.nmap.add(this);
//			
//			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 1));
//			KeyFrame kf2 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleXProperty(), 1));
//			KeyFrame kf3 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleYProperty(), 1));
//
//			Timeline timeline = new Timeline();
//			timeline.getKeyFrames().addAll(kf1, kf2, kf3);
//		    timeline.setCycleCount(1);
//		    timeline.play();
//		}
//	}
//
//	public void hide() {
//		if (nnode.nmap.contains(this)) {
//			
//			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 0));
//			KeyFrame kf2 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleXProperty(), 0.5));
//			KeyFrame kf3 = new KeyFrame(Duration.millis(600), new KeyValue(this.scaleYProperty(), 0.5));
//
//		    Timeline timeline = new Timeline();
//		    timeline.getKeyFrames().addAll(kf1, kf2, kf3);
//		    
//		    timeline.setCycleCount(1);
//		    timeline.setOnFinished(e -> {
//				nnode.nmap.remove(this);
//			});
//		    timeline.play();
////		    timeline.getStatus().
//		}
//	}
//
//}

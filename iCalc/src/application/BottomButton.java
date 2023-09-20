package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BottomButton extends Pane {

	
	private Timeline showTimeLine;
	private Timeline hideTimeLine;
	private Constellatio constellatio;
	
	
	public BottomButton(Constellatio constellatio, String style) {
		this.constellatio = constellatio;
		this.getStyleClass().add(style);		
		this.setOnMouseEntered(e -> this.zoomIn());
		this.setOnMouseExited(e -> this.zoomOut());
	}
	
	public void zoomIn() {
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();		
		if (constellatio.getMenu().getViewMenu().getAnimationMenuItem().isSelected()) {
			KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleXProperty(), 1.3));
			KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleYProperty(), 1.3));
			showTimeLine = new Timeline();
			showTimeLine.getKeyFrames().addAll(kf2, kf3);
		    showTimeLine.setCycleCount(1);
		    showTimeLine.play();
		}else {
			this.setScaleX(1.3);
			this.setScaleY(1.3);
		}	
	}

	public void zoomOut() {
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();
		if (constellatio.getMenu().getViewMenu().getAnimationMenuItem().isSelected()) {
			KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleXProperty(), 1));
			KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleYProperty(), 1));
		    hideTimeLine = new Timeline();
		    hideTimeLine.getKeyFrames().addAll(kf2, kf3);
		    hideTimeLine.setCycleCount(1);
		    hideTimeLine.play();
		}else {
			this.setScaleX(1);
			this.setScaleY(1);
		}	
	}

}

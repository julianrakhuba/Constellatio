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
	
	public BottomButton(String style) {
		this.getStyleClass().add(style);		
		this.setOnMouseEntered(e ->{
//			this.setScaleX(1.3);
//			this.setScaleY(1.3);
			this.show();
		});
		this.setOnMouseExited(e ->{
//			this.setScaleX(1);
//			this.setScaleY(1);
			this.hide();
		});
	}
	
	public void show() {
		//check if timeline form is
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();		
//			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleXProperty(), 1.3));
			KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleYProperty(), 1.3));

			showTimeLine = new Timeline();
			showTimeLine.getKeyFrames().addAll(kf2, kf3);
		    showTimeLine.setCycleCount(1);
		    showTimeLine.play();
	}

	public void hide() {
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();		
//			KeyFrame kf1 = new KeyFrame(Duration.millis(600), new KeyValue(this.opacityProperty(), 0));
			KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleXProperty(), 1));
			KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(this.scaleYProperty(), 1));

		    hideTimeLine = new Timeline();
		    hideTimeLine.getKeyFrames().addAll(kf2, kf3);
		    hideTimeLine.setCycleCount(1);
//		    hideTimeLine.setOnFinished(e -> {
//				nnode.nmap.remove(this);
//			});
		    hideTimeLine.play();
	}
	

}

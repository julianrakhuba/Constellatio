package file;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class CenterMessage extends Label {
	private Timeline showTimeLine;
	private SequentialTransition hideTimeLine;// = new SequentialTransition();

	private Timeline hideTimeLineChild;

//	private NFile nFile; 
	
	public CenterMessage(NFile nFile) {
//		this.nFile = nFile;
		
		StackPane.setAlignment(this, Pos.TOP_CENTER);
	    StackPane.setMargin(this, new Insets(0, 0, 0, 0));	    
	    this.setAlignment(Pos.BASELINE_CENTER);
	    this.prefWidthProperty().bind(nFile.logicGlassSP.widthProperty());
	    this.setOpacity(0);
	    
		if(nFile.getFileManager().napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
		    this.setStyle("-fx-text-fill: rgba(255,255,255, 1); -fx-border-width: 0 0 0.5 0; -fx-border-color: derive(#1E90FF, 50%);  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 4, 0.2, 0.0, 0.0); -fx-background-radius: 0; -fx-border-radius: 0;");
		}else {
		    this.setStyle("-fx-text-fill: rgba(0,0,0, 0.5); -fx-border-width: 1 0 1 0; -fx-border-color: white;  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.8);  -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.1), 5, 0.0 , 0, 0); -fx-background-radius: 7 7 0 0; -fx-border-radius: 7 7 0 0;");
		}
	}
	
	
	public void setMessage(String string) {
		if(string == null) {
			this.hide();
		}else {
			this.show(string);
		}		
	}
	public void show(String string) {
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();
		this.setText(string);
		KeyFrame kf1 = new KeyFrame(Duration.millis(100), new KeyValue(this.opacityProperty(), 1));
		showTimeLine = new Timeline();
		showTimeLine.getKeyFrames().addAll(kf1);
	    showTimeLine.setCycleCount(1);
	    showTimeLine.play();
	}

	public void hide() {
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();		
		hideTimeLine = new SequentialTransition();
		hideTimeLineChild = new Timeline();
		hideTimeLineChild.getKeyFrames().addAll(new KeyFrame(Duration.seconds(3), new KeyValue(this.opacityProperty(), 0)));		    
		hideTimeLine.getChildren().addAll(new PauseTransition(Duration.seconds(0.5)), hideTimeLineChild);
	    hideTimeLine.setCycleCount(1);
	    hideTimeLine.setOnFinished(e -> {
			this.setText(null);
		});
	    hideTimeLine.play();
	}
}

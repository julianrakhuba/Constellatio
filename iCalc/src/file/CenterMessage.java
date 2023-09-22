package file;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import application.Nnode;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class CenterMessage extends Pane {
	private Timeline showTimeLine;
	private SequentialTransition hideTimeLine;
	private Timeline hideTimeLineChild;
	private Label label = new Label();
	
	public CenterMessage(NFile nFile) {		
		StackPane.setAlignment(this, Pos.TOP_CENTER);
	    StackPane.setMargin(this, new Insets(0, 0, 0, 0));	    
	    label.setAlignment(Pos.BASELINE_CENTER);
	    this.setPickOnBounds(false);
	    this.setOpacity(0);
	    this.setPadding(new Insets(0, 0, 0, 0));
	    this.getChildren().add(label);
	    label.setLayoutX(0);
	    label.setLayoutY(0);
	    label.setWrapText(true); // Set wrapText to true to enable text wrapping
		if(nFile.getFileManager().napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
			label.setStyle("-fx-text-fill: rgba(255,255,255, 1); -fx-border-width: 0.5; -fx-border-color: derive(#1E90FF, 50%);  -fx-padding: 2 10 2 10; -fx-background-color: rgba(1,1,1, 0.4); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 4, 0.2, 0.0, 0.0); -fx-background-radius: 0 0 4 4; -fx-border-radius: 0 0 4 4;");		
		}else {
			label.setStyle("-fx-text-fill: rgba(0,0,0, 0.5); -fx-border-width: 1; -fx-border-color: white;  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.8);  -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.1), 5, 0.0 , 0, 0); -fx-background-radius: 0 0 4 4; -fx-border-radius: 0 0 4 4;");
		}
	}
	
	
	public void setMessage(Nnode nnode, String string) {
		if(string == null) {
			this.hide();
		}else {
			this.show(nnode, string);
		}		
	}
	
	public void show(Nnode nnode, String string) {
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();
		label.setText(string);
		showTimeLine = new Timeline();
		
		
		showTimeLine.getKeyFrames().addAll(new KeyFrame(Duration.millis(100), new KeyValue(this.opacityProperty(), 1)));
		//TODO add max left size of scroll area
		showTimeLine.getKeyFrames().addAll(new KeyFrame(Duration.millis(400), new KeyValue(label.layoutXProperty(), nnode.getLayoutX(), Interpolator.EASE_BOTH)));		    
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
	    	label.setText(null);
		});
	    hideTimeLine.play();
	}
}

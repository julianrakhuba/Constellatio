package logic;

import generic.LAY;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.animation.Animation.Status;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Logic extends VBox {
	private LAY lay;
	private Pane pane = new Pane(this);
	private Timeline showTimeLine;
	private Timeline hideTimeLine;
	
	public Logic(LAY lay) {
		this.lay = lay;
//		this.setMinSize(20, 20);
		this.setMinSize(55, 18);

		this.setSpacing(4.0);
		this.getStyleClass().add("logic");
		this.setLayoutY(6);
		pane.setPickOnBounds(false);
		
		this.setOpacity(0);
		this.setScaleX(0.5);
		this.setScaleY(0.5);
	}
	
	public void show() {
		ObservableList<Node> mapGlass = lay.nnode.nmap.getNFile().logicStackPane.getChildren();
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();		
		if(!mapGlass.contains(pane)) mapGlass.add(pane);
		
		KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 1));
		KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 1));
		KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 1));

		showTimeLine = new Timeline();
		showTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);
	    showTimeLine.setCycleCount(1);
	    showTimeLine.play();
		
	}
	
	public void hide() {
		ObservableList<Node> mapGlass = lay.nnode.nmap.getNFile().logicStackPane.getChildren();
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();		
		if(mapGlass.contains(pane)) {
			KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 0));
			KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 0.5));
			KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 0.5));
		    hideTimeLine = new Timeline();
		    hideTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);
		    hideTimeLine.setCycleCount(1);
		    hideTimeLine.setOnFinished(e -> {
				mapGlass.remove(pane);
			});
		    hideTimeLine.play();			
		}
	}
}

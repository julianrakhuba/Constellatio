package logic;

import application.NMap;
import generic.LAY;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Logic extends VBox {
	private LAY lay;
	private Pane logicPane = new Pane(this);
	private Timeline showTimeLine;
	private Timeline hideTimeLine;

	public Logic(LAY lay) {
		this.lay = lay;
		this.setMinSize(52, 18);
		this.setSpacing(1.0);
		this.getStyleClass().add("logic");
		this.setLayoutY(26);
		logicPane.setPickOnBounds(false);
		this.setOpacity(0);
		this.setScaleX(0.5);
		this.setScaleY(0.5);
	}
	
	public void show() {
		NMap nmap = lay.nnode.nmap;
		ObservableList<Node> logicGlass = nmap.getNFile().logicGlassSP.getChildren();
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();
		
		if(!lay.nnode.nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected()) {
			if(!logicGlass.contains(logicPane)) logicGlass.add(logicPane);
		}

		showTimeLine = new Timeline();
		KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 1));
		KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 1));
		KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 1));
		showTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);

		//move group arcs
		lay.getRootLevel().getGroupsAll().forEach(gr ->{
			gr.getArc().setOpacity(0);
			KeyFrame kf4 = new KeyFrame(Duration.millis(300), new KeyValue(gr.getArc().opacityProperty(), 1));
			showTimeLine.getKeyFrames().addAll(kf4);
		});
		
	    showTimeLine.setCycleCount(1);
	    showTimeLine.play();		
	}
	
	public void hide() {
		ObservableList<Node> logicGlass = lay.nnode.nmap.getNFile().logicGlassSP.getChildren();
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();		
	    hideTimeLine = new Timeline();

	    KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 0));
		KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 0.5));
		KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 0.5));
	    hideTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);
	    
		lay.getRootLevel().getGroupsAll().forEach(gr ->{
			hideTimeLine.getKeyFrames().addAll(new KeyFrame(Duration.millis(300), new KeyValue(gr.getArc().opacityProperty(), 0)));
		});
		
	    hideTimeLine.setCycleCount(1);
	    hideTimeLine.setOnFinished(e -> {
			logicGlass.removeAll(logicPane);//OLD
			lay.getRootLevel().hide();
		});
	    hideTimeLine.play();			
	}
}

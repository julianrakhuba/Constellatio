package logic;

import java.util.ArrayList;

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
//	private Button textbtn = new Button("Test");
	private Timeline showTimeLine;
	private Timeline hideTimeLine;
	private ArrayList<ArchLevel> newLevels = new ArrayList<ArchLevel>();
	private ArchLevel level;
//	private LogicArcs level2;

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
		
//        StackPane.setAlignment(textbtn, Pos.BOTTOM_CENTER);
//        StackPane.setMargin(textbtn, new Insets(0, 0, 10, 0));
	}
	
	public void show() {
		if(level == null
//				&& level2 == null
				) {	
			double x = lay.getCenterX();
			double y = lay.getCenterY();// +1;
			level = new ArchLevel(x, y, 15, 1);
//	    	level2 = new LogicArcs(x, y, 22, 2);
//	    	newLevels.add(level2);
	    	newLevels.add(level);
//	    	pane.getChildren().addAll(newLevels);
		}
		
		NMap nmap = lay.nnode.nmap;
//		if(!nmap.contains(level2)) nmap.add(level2);
		if(!nmap.contains(level)) nmap.add(level);
		
		ObservableList<Node> logicGlass = nmap.getNFile().logicGlassSP.getChildren();
		if(hideTimeLine != null && hideTimeLine.getStatus() == Status.RUNNING) hideTimeLine.stop();
		if(!logicGlass.contains(logicPane)) logicGlass.add(logicPane);
//		if(!logicGlass.contains(textbtn)) logicGlass.add(textbtn);

		
		showTimeLine = new Timeline();

		KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 1));
		KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 1));
		KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 1));
		showTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);

		newLevels.forEach(arc ->{
			arc.setOpacity(0);
			arc.setScaleX(0.5);
			arc.setScaleY(0.5);
			KeyFrame kf4 = new KeyFrame(Duration.millis(300), new KeyValue(arc.opacityProperty(), 1));
			KeyFrame kf5 = new KeyFrame(Duration.millis(300), new KeyValue(arc.scaleXProperty(), 1));
			KeyFrame kf6 = new KeyFrame(Duration.millis(300), new KeyValue(arc.scaleYProperty(), 1));
			showTimeLine.getKeyFrames().addAll(kf4, kf5, kf6);
		});
		 
	    showTimeLine.setCycleCount(1);
	    showTimeLine.play();		
	}
	
	public void hide() {
		ObservableList<Node> logicGlass = lay.nnode.nmap.getNFile().logicGlassSP.getChildren();
		if(showTimeLine != null && showTimeLine.getStatus() == Status.RUNNING) showTimeLine.stop();		
		if(logicGlass.contains(logicPane)) {
		    hideTimeLine = new Timeline();

		    KeyFrame kf1 = new KeyFrame(Duration.millis(300), new KeyValue(this.opacityProperty(), 0));
			KeyFrame kf2 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleXProperty(), 0.5));
			KeyFrame kf3 = new KeyFrame(Duration.millis(300), new KeyValue(this.scaleYProperty(), 0.5));
		    hideTimeLine.getKeyFrames().addAll(kf1, kf2, kf3);

		    newLevels.forEach(arc ->{
				KeyFrame kf4 = new KeyFrame(Duration.millis(300), new KeyValue(arc.opacityProperty(), 0));
				KeyFrame kf5 = new KeyFrame(Duration.millis(300), new KeyValue(arc.scaleXProperty(), 0.5));
				KeyFrame kf6 = new KeyFrame(Duration.millis(300), new KeyValue(arc.scaleYProperty(), 0.5));
				hideTimeLine.getKeyFrames().addAll(kf4, kf5, kf6);
			});
		    
		    hideTimeLine.setCycleCount(1);
		    hideTimeLine.setOnFinished(e -> {
				logicGlass.removeAll(logicPane);
//				lay.nnode.nmap.remove(level2);
		    	lay.nnode.nmap.remove(level);
//				if(logicGlass.contains(textbtn)) logicGlass.remove(textbtn);
			});
		    hideTimeLine.play();			
		}
	}

	public ArrayList<ArchLevel> getLevels() {
		return newLevels;
	}
}

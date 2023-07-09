package file;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class QuadSplit extends SplitPane {
	private SplitPane upper = new SplitPane();
	private SplitPane lower = new SplitPane();

	private Timeline showSideTimeLine;
	private Timeline hideSideTimeLine;
	private Timeline showBottomTimeLine;
	private Timeline hideBottomTimeLine;
	
	private Region topLeft;
	private Region topRight;
	private Region bottomLeft;
//	private Region bottomRight;
	
	
	public QuadSplit() {
		this.setOrientation(Orientation.VERTICAL);
		this.setStyle("-fx-background-color: rgba(0,0,0,0);");
		upper.setStyle("-fx-background-color: rgba(0,0,0,0);");
		lower.setStyle("-fx-background-color: rgba(0,0,0,0);");
		upper.setMinHeight(0);
		lower.setMinHeight(0);
		this.getItems().addAll(upper);
	}

	public void setTopLeft(Region region) {
		if(topLeft == null && region != null) {//new
			upper.getItems().add(0,region);
		}else if(topLeft != null && region != null) {//swop
			if(topLeft != region) {
				upper.getItems().remove(topLeft);
				upper.getItems().add(0,region);
			}
		}else {
			upper.getItems().remove(topLeft);
		}
		topLeft = region;
	}
	
	public void setTopRight(Region region) {
		System.out.println("setTopRight from: " + topRight + "  to: "+ region );
		if(topRight == null && region != null) {//new
			System.out.println("[NEW]");
			this.showTopRight(region);
		}else if(topRight != null && region != null) {//swop
			if(topRight != region) {
				upper.getItems().remove(topRight);
				upper.getItems().add(1,region);
				Divider div = upper.getDividers().get(0);
				div.setPosition(0.82);
				System.out.println("[SWOP]");
			}
		}else {
			hideTopRight(topRight);
			System.out.println("[REMOVE]");
		}
		topRight = region;
	}
	
	public void setBottomLeft(Region region) {
		if(!this.getItems().contains(lower)) this.getItems().addAll(lower);//move to animate

		if(bottomLeft == null && region != null) {//new
			lower.getItems().add(0,region);
		}else if(bottomLeft != null && region != null) {//swop
			if(bottomLeft != region) {
				lower.getItems().remove(bottomLeft);
				lower.getItems().add(0,region);
			}
		}else {
			lower.getItems().remove(bottomLeft);
			this.getItems().remove(lower);//move to animate
		}
		bottomLeft = region;
	}
	
	
	//ANIMATION ********************************************************************************************* ANIMATION
	private void showTopRight(Region region) {
		if(hideSideTimeLine != null && hideSideTimeLine.getStatus() == Status.RUNNING) hideSideTimeLine.stop();
		if(!upper.getItems().contains(region)) {
			upper.getItems().add(1,region);
			region.setOpacity(0);
			Divider div = upper.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.82));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 1));
			showSideTimeLine = new Timeline();
			showSideTimeLine.getKeyFrames().addAll(kf1, kf2);
		    showSideTimeLine.setCycleCount(1);
		    showSideTimeLine.play();
		}
	}
		
	private void hideTopRight(Region region) {
		if(showSideTimeLine != null && showSideTimeLine.getStatus() == Status.RUNNING) showSideTimeLine.stop();				
		if (upper.getItems().contains(region)) {
			Divider div = upper.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 0));
		    hideSideTimeLine = new Timeline();
		    hideSideTimeLine.getKeyFrames().addAll(kf1, kf2);
		    hideSideTimeLine.setCycleCount(1);
		    hideSideTimeLine.setOnFinished(e -> {
		    	upper.getItems().remove(region);
			});
		    hideSideTimeLine.play();
		}
	}
	


	private void showGrid(Region region) {
		if(hideBottomTimeLine != null && hideBottomTimeLine.getStatus() == Status.RUNNING) hideBottomTimeLine.stop();
		
		if(!this.getItems().contains(lower)) this.getItems().add(lower);
		if(!lower.getItems().contains(region)) {
			lower.getItems().add(region);
			region.setOpacity(0);
			Divider div = this.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.6));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 1));
			showBottomTimeLine = new Timeline();
			showBottomTimeLine.getKeyFrames().addAll(kf1, kf2);
			showBottomTimeLine.setCycleCount(1);
			showBottomTimeLine.play();
		}
	}
	
	
	private void hideGrid(TabPane region) {
		if(showBottomTimeLine != null && showBottomTimeLine.getStatus() == Status.RUNNING) showBottomTimeLine.stop();				
		if (lower.getItems().contains(region)) {
			Divider div = this.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 0));
			hideBottomTimeLine = new Timeline();
			hideBottomTimeLine.getKeyFrames().addAll(kf1, kf2);
			hideBottomTimeLine.setCycleCount(1);
		    hideBottomTimeLine.setOnFinished(e -> {
		    	lower.getItems().remove(region);
			});
		    hideBottomTimeLine.play();
		}
	}

	
	
//	public void showGrid(Region region) {
//		if(!this.getItems().contains(lower)) this.getItems().add(lower);
//		if(bottomLeft == null && region != null) {//new
//			lower.getItems().add(0,region);
//			this.getDividers().get(0).setPosition(0.6);
//		}else if(bottomLeft != null && region != null) {//swop
//			if(bottomLeft != region) {
//				lower.getItems().remove(bottomLeft);
//				lower.getItems().add(0,region);
//			}
//		}else {
//			lower.getItems().remove(bottomLeft);
//		}
//		bottomLeft = region;
//	}
	
//	public void clear() {
//		upper.getItems().clear();
//		lower.getItems().clear();
////		this.getItems().clear();
//	}
}

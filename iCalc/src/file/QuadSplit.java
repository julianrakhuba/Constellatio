package file;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class QuadSplit extends SplitPane {
	private SplitPane upper = new SplitPane();
	private SplitPane lower = new SplitPane();

	private Timeline showInfoTl;
	private Timeline hideInfoTl;
	
	private Timeline shoewChartTl;
	private Timeline hideChartTl;
	
	private Timeline showBottomTl;
	private Timeline hideBottomTl;
	
	private Timeline shoewGridTl;
	private Timeline hideGridTl;
	
	private Region topLeft;
	private Region topRight;
	private Region bottomLeft;
	private Region bottomRight;
	
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
				if(upper.getDividers().size()>0) upper.getDividers().get(0).setPosition(0.82);//DO I NEED DEVIDER UPDATE ON SWOP???
				System.out.println("[SWOP]");
			}
		}else {
			hideTopRight(topRight);
			System.out.println("[REMOVE]");
		}
		topRight = region;
	}
	
	private void showTopRight(Region region) {
		if(hideInfoTl != null && hideInfoTl.getStatus() == Status.RUNNING) hideInfoTl.stop();
		if(!upper.getItems().contains(region)) {
			upper.getItems().add(1,region);
			region.setOpacity(0);
			Divider div = upper.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.82));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 1));
			showInfoTl = new Timeline();
			showInfoTl.getKeyFrames().addAll(kf1, kf2);
		    showInfoTl.setCycleCount(1);
		    showInfoTl.play();
		}
	}
		
	private void hideTopRight(Region region) {
		if(showInfoTl != null && showInfoTl.getStatus() == Status.RUNNING) showInfoTl.stop();				
		if (upper.getItems().contains(region)) {
			Divider div = upper.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 0));
		    hideInfoTl = new Timeline();
		    hideInfoTl.getKeyFrames().addAll(kf1, kf2);
		    hideInfoTl.setCycleCount(1);
		    hideInfoTl.setOnFinished(e -> {
		    	upper.getItems().remove(region);
			});
		    hideInfoTl.play();
		}
	}
	
	//BOTTOM *******************************************************************************
	public void setBottomRight(Region region) {
		if(!this.getItems().contains(lower)) this.getItems().addAll(lower);//move to animate

		if(bottomRight == null && region != null) {//new
			this.showBottomRight(region);
		}else if(bottomRight != null && region != null) {//swop
			if(bottomRight != region) {
				lower.getItems().remove(bottomRight);
				lower.getItems().add(region);
			}
		}else {
			hideBottomRight(bottomRight);
		}
		bottomRight = region;
	}
	
	private void showBottomRight(Region region) {
		if(lower.getItems().size() > 0) {
			//add
			if(hideChartTl != null && hideChartTl.getStatus() == Status.RUNNING) hideChartTl.stop();
			if(!lower.getItems().contains(region)) {
				lower.getItems().add(region);
				region.setOpacity(0);			
				Divider div = lower.getDividers().get(0);
				div.setPosition(1);
				KeyFrame kf1 = new KeyFrame(Duration.millis(400), new KeyValue(div.positionProperty(), 0.5));
				KeyFrame kf2 = new KeyFrame(Duration.millis(400), new KeyValue(region.opacityProperty(), 1));
				shoewChartTl = new Timeline();
				shoewChartTl.getKeyFrames().addAll(kf1, kf2);
				shoewChartTl.setCycleCount(1);
			    shoewChartTl.play();
			}
		}else {
			showBottom(region);//new USE BOTTOM LEFT
		}		
	}

	private void hideBottomRight(Region region) {
		if(lower.getItems().size() == 2) {
			if(shoewChartTl != null && shoewChartTl.getStatus() == Status.RUNNING) shoewChartTl.stop();				
			if (lower.getItems().contains(region)) {
				Divider div = lower.getDividers().get(0);
				KeyFrame kf1 = new KeyFrame(Duration.millis(400), new KeyValue(div.positionProperty(), 1));
				KeyFrame kf2 = new KeyFrame(Duration.millis(400), new KeyValue(region.opacityProperty(), 0));
				hideChartTl = new Timeline();
				hideChartTl.getKeyFrames().addAll(kf1, kf2);
				hideChartTl.setCycleCount(1);
				hideChartTl.setOnFinished(e -> {
					lower.getItems().remove(region);
					if(lower.getItems().size() == 0) this.getItems().remove(lower);//make it optional
				});
				hideChartTl.play();
			}
		}else {
			hideBottom(region);
		}	
	}

	
	private void showBottomLeft(Region region) {
		if(lower.getItems().size() > 0) {
			//add
			if(hideGridTl != null && hideGridTl.getStatus() == Status.RUNNING) hideGridTl.stop();
			if(!lower.getItems().contains(region)) {
				lower.getItems().add(0,region);
				region.setOpacity(0);			
				Divider div = lower.getDividers().get(0);
				div.setPosition(0);
				KeyFrame kf1 = new KeyFrame(Duration.millis(400), new KeyValue(div.positionProperty(), 0.5));
				KeyFrame kf2 = new KeyFrame(Duration.millis(400), new KeyValue(region.opacityProperty(), 1));
				shoewGridTl = new Timeline();
				shoewGridTl.getKeyFrames().addAll(kf1, kf2);
				shoewGridTl.setCycleCount(1);
				shoewGridTl.play();
			}
		}else {
			showBottom(region);//new USE BOTTOM LEFT
		}		
	}
	

	private void hideBottomLeft(Region region) {
		if(lower.getItems().size() == 2) {
			if(shoewGridTl != null && shoewGridTl.getStatus() == Status.RUNNING) shoewGridTl.stop();				
			if (lower.getItems().contains(region)) {
				Divider div = lower.getDividers().get(0);
				KeyFrame kf1 = new KeyFrame(Duration.millis(400), new KeyValue(div.positionProperty(), 0));
				KeyFrame kf2 = new KeyFrame(Duration.millis(400), new KeyValue(region.opacityProperty(), 0));
				hideGridTl = new Timeline();
				hideGridTl.getKeyFrames().addAll(kf1, kf2);
				hideGridTl.setCycleCount(1);
				hideGridTl.setOnFinished(e -> {
					lower.getItems().remove(region);
					if(lower.getItems().size() == 0) this.getItems().remove(lower);//make it optional
				});
				hideGridTl.play();
			}
		}else {
			hideBottom(region);
		}
		
	}
	//••••••••••••••••••••••••••••••••••••8
	
	public void setBottomLeft(Region region) {
		if(!this.getItems().contains(lower)) this.getItems().addAll(lower);//move to animate

		if(bottomLeft == null && region != null) {//new
			this.showBottomLeft(region);
		}else if(bottomLeft != null && region != null) {//swop
			if(bottomLeft != region) {
				lower.getItems().remove(bottomLeft);
				lower.getItems().add(0,region);
				Divider div = lower.getDividers().get(0);//DO I NEED DEVIDER UPDATE ON SWOP???
				div.setPosition(0.7);
			}
		}else {
			hideBottomLeft(bottomLeft);
		}
		bottomLeft = region;
	}
	
	private void showBottom(Region region) {
		if(hideBottomTl != null && hideBottomTl.getStatus() == Status.RUNNING) hideBottomTl.stop();
		
		if(!this.getItems().contains(lower)) this.getItems().add(lower);
		if(!lower.getItems().contains(region)) {
			lower.getItems().add(0, region);
			region.setOpacity(0);
			Divider div = this.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.7));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 1));
			showBottomTl = new Timeline();
			showBottomTl.getKeyFrames().addAll(kf1, kf2);
			showBottomTl.setCycleCount(1);
			showBottomTl.play();
		}
	}
	
	//ANIMATION ********************************************************************************************* ANIMATION
	private void hideBottom(Region region) {
		if(showBottomTl != null && showBottomTl.getStatus() == Status.RUNNING) showBottomTl.stop();				
		if (lower.getItems().contains(region)) {
			Divider div = this.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(region.opacityProperty(), 0));
			hideBottomTl = new Timeline();
			hideBottomTl.getKeyFrames().addAll(kf1, kf2);
			hideBottomTl.setCycleCount(1);
		    hideBottomTl.setOnFinished(e -> {
		    	lower.getItems().remove(region);
				this.getItems().remove(lower);//move to animate,  make this optional if size content is 0
			});
		    hideBottomTl.play();
		}
	}
}

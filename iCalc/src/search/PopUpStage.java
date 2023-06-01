package search;

import application.Constellatio;
import application.NScene;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PopUpStage extends Stage {
	private VBox children = new VBox();
	private NScene nscene;
	private Region region;
	
	public PopUpStage(Constellatio napp, Region region) {
		initOwner(napp.getStage());
		initStyle(StageStyle.TRANSPARENT);		
		initModality(Modality.NONE);
		this.region = region;
		nscene = new NScene(children, napp);
		nscene.setFill(Color.TRANSPARENT);
		this.setScene(nscene);
		children.setSpacing(5);
		children.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 3 3 3 3 ;");
		children.setPadding(new Insets(10, 10 , 10 ,10));				
		this.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {if (!isNowFocused) {this.hide();} });
		this.setAlwaysOnTop(true);	
		
		
		this.setOnShowing(e ->{			
			this.setOpacity(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(this.opacityProperty(), 1));
		    Timeline timeline = new Timeline(kf1);
		    timeline.setCycleCount(1);
		    timeline.play();		    
		});
	}
	
	public void showSearchStage() {
		if(!this.isShowing()) {
			Point2D pt = region.localToScreen(0, region.getHeight() + 1);
			this.setX(pt.getX() + 15);
			this.setY(pt.getY());
			this.setWidth(region.getWidth() - 30);		
			this.show();
		}
		
	}
	
	
	public void hide() {
		KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(this.opacityProperty(), 0));
	    Timeline timeline = new Timeline(kf1);
	    timeline.setCycleCount(1);
	    timeline.setOnFinished(e -> super.hide());
	    timeline.play();
	}

	public void add(Node item) {
		if(!children.getChildren().contains(item)) {
			children.getChildren().add(item);
		}
	}
	
}
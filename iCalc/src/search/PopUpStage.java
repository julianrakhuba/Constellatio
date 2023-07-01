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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PopUpStage extends Stage {
	private VBox vbox = new VBox();
	private StackPane root = new StackPane(vbox);

	
	private NScene nscene;
	private Region anchor;
	
	public PopUpStage(Constellatio napp, Region anchor) {
		initOwner(napp.getStage());
		initStyle(StageStyle.TRANSPARENT);		
		initModality(Modality.NONE);
		this.anchor = anchor;
		
		nscene = new NScene(root, napp);
		
		nscene.setFill(Color.TRANSPARENT);
		this.setScene(nscene);
		vbox.setSpacing(5);
		if (napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			vbox.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.5); "
	        		+ "-fx-border-width: 0.5;"
	        		+ "-fx-border-color: derive(#1E90FF, 50%);"
	        		+ "-fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 8, 0.2, 0.0, 0.0);"
	        		+ "-fx-background-radius: 5;"
	        		+ "-fx-border-radius: 5;");

		}else {
			vbox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 3 3 3 3 ;");
		}
//		this.getScene().getRoot().setEffect(new DropShadow(20, Color.ORANGE));
		vbox.setPadding(new Insets(10, 10 , 10 ,10));				

		root.setStyle("-fx-background-color: transparent;");
		root.setPadding(new Insets(0, 10 , 10 ,10));				
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
	
	public void showPopUp() {
		if(!this.isShowing()) {
			Point2D pt = anchor.localToScreen(0, anchor.getHeight() + 1);
			this.setX(pt.getX() + 5);
			this.setY(pt.getY());
			this.setWidth(anchor.getWidth() - 5);		
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
		if(!vbox.getChildren().contains(item)) {
			vbox.getChildren().add(item);
		}
	}
	
}
package search;

import application.NScene;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import launcher.Constellatio;

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
	}
	
	public void showSearchStage() {
		//TODO redo it proper way
		Point2D pt = region.localToScreen(0, region.getHeight() + 1);
		this.setX(pt.getX() + 15);
		this.setY(pt.getY());
		this.setWidth(region.getWidth() - 30);		
		this.show();
	}

	public void add(Node item) {
		if(!children.getChildren().contains(item)) {
			children.getChildren().add(item);
		}
	}
	
}
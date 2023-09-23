package application;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.util.Duration;

public class ViewMenu extends Menu {
	private Constellatio constellatio;
	private MenuItem centerMenuItem = new MenuItem("Center");
	private MenuItem inMenuItem = new MenuItem("In");	
	private MenuItem outMenuItem = new MenuItem("Out");
	private MenuItem clearConsole = new MenuItem("Clear Console");
	
	private CheckMenuItem simpleViewMenuItem = new CheckMenuItem("Simple View");
	private CheckMenuItem animationMenuItem = new CheckMenuItem("Animation");
	private CheckMenuItem glassModeMenuItem = new CheckMenuItem("Glass Mode (restart)");
	private CheckMenuItem dynamicSearchMenuItem = new CheckMenuItem("Dynamic SQL");
	private CheckMenuItem autoFoldMenuItem = new CheckMenuItem("Auto-fold");

	public ViewMenu(String string, Constellatio constellatio) {
		super(string);
		this.constellatio = constellatio;
		
		dynamicSearchMenuItem.setSelected(true);
		animationMenuItem.setSelected(true);
		
		this.getItems().addAll(dynamicSearchMenuItem, new SeparatorMenuItem(), autoFoldMenuItem, new SeparatorMenuItem(), inMenuItem,
				centerMenuItem, outMenuItem, new SeparatorMenuItem(), simpleViewMenuItem,new SeparatorMenuItem(),glassModeMenuItem, new SeparatorMenuItem(),
				new SeparatorMenuItem(), clearConsole,new SeparatorMenuItem(),  animationMenuItem);
		
		clearConsole.setOnAction(e -> constellatio.getConsole().clear());

		autoFoldMenuItem.setSelected(true);
		simpleViewMenuItem.setSelected(true);
		
		simpleViewMenuItem.setOnAction(e -> {
			if (simpleViewMenuItem.isSelected()) {
				constellatio.getFilemanager().setCompactView(true);
			} else {
				constellatio.getFilemanager().setCompactView(false);
			}
		});
		
		glassModeMenuItem.setOnAction(e ->{
			constellatio.updateGlassMode(glassModeMenuItem.isSelected());
		});
		
		
		inMenuItem.setOnAction(e -> this.zoom(constellatio.getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 1.5));
		centerMenuItem.setOnAction(e -> this.zoom(1.0));
		outMenuItem.setOnAction(e -> this.zoom(constellatio.getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 0.75));
	}
	

	private void zoom(double zoomValue) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().clear();
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(400),
						new KeyValue(constellatio.getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty(),
								zoomValue, Interpolator.EASE_BOTH)));
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(400),
						new KeyValue(constellatio.getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleYProperty(),
								zoomValue, Interpolator.EASE_BOTH)));
		timeline.setCycleCount(1);
		timeline.playFromStart();
	}

	public MenuItem getInMenuItem() {
		return inMenuItem;
	}

	public MenuItem getCenterMenuItem() {
		return centerMenuItem;
	}

	public MenuItem getOutMenuItem() {
		return outMenuItem;
	}

	public CheckMenuItem getAutoFoldMenuItem() {
		return autoFoldMenuItem;
	}

	public CheckMenuItem getDynamicSearchMenuItem() {
		return dynamicSearchMenuItem;
	}

	public CheckMenuItem getSimpleViewMenuItem() {
		return simpleViewMenuItem;
	}


	public CheckMenuItem getGlassModeMenuItem() {
		return glassModeMenuItem;
	}
	
	public CheckMenuItem getAnimationMenuItem() {
		return animationMenuItem;
	}

}

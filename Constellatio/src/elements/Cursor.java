package elements;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
//import rakhuba.elements.ELM;

public class Cursor extends Pane {
	private FadeTransition pulse = new FadeTransition(Duration.millis(500), this);
	
	public Cursor(ELM elm) {
		this.getStyleClass().add("cusrsorElmUnfocused");
		pulse.setFromValue(1);
		pulse.setToValue(0.3);
		pulse.setAutoReverse(true);
		pulse.setCycleCount(Animation.INDEFINITE);
		pulse.setInterpolator(Interpolator.EASE_BOTH);
	}

	public void styleFocused() {
		this.getStyleClass().clear();
		this.getStyleClass().add("cusrsorElmFocused");	
		pulse.play();
	}
	
	public void styleUnfocused() {
		this.getStyleClass().clear();
		this.getStyleClass().add("cusrsorElmUnfocused");
		pulse.stop();		
	}	
}
package generic;

import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;
//import rakhuba.generic.LAY;
//import rakhuba.generic.LayColors;
import status.ColorMode;

public class AnimatedStyler {
	private LayColors layColors = new LayColors();
	private LAY lay;
	private ObjectProperty<Color> c1 = new SimpleObjectProperty<>();
	private ObjectProperty<Color> c2 = new SimpleObjectProperty<>();
	private ObjectProperty<Color> c3 = new SimpleObjectProperty<>();
	private ObjectProperty<Color> c4 = new SimpleObjectProperty<>();

	public AnimatedStyler(LAY lay) {
		this.lay = lay;
		c1.addListener((obs, oldColor, n) -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));
		c2.addListener((obs, oldColor, n) -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));
		c3.addListener((obs, oldColor, n) -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));
		c4.addListener((obs, oldColor, n) -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));
	}
	
	private void applyStyle(String st1, String st2, String st3, String st4) {
		lay.getPane().setStyle("-fx-background-color: linear-gradient(" + st1 + ", " + st2 + "), radial-gradient(center 50% -40%, radius 200%, " + st3 + " 45%, " + st4 + " 50%);");
	}

	public void updateLayStyle(ColorMode c) {
		boolean animate = true;
		List<Color> tocolors = layColors.getColors(c);
		if(animate
				&& (c1.getValue() != null && c2.getValue() != null && c3.getValue() != null && c4.getValue() != null)
				) {
			KeyFrame kf1 = new KeyFrame(Duration.millis(100), new KeyValue(c1, tocolors.get(0)));
		    KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(c2, tocolors.get(1)));
		    KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(c3, tocolors.get(2)));
		    KeyFrame kf4 = new KeyFrame(Duration.millis(100), new KeyValue(c4, tocolors.get(3)));
		    Timeline timeline = new Timeline(kf1, kf2, kf3, kf4);
		    timeline.setCycleCount(1);
		    timeline.play();
		}else {
			c1.setValue(tocolors.get(0));
			c2.setValue(tocolors.get(1));
			c3.setValue(tocolors.get(2));
			c4.setValue(tocolors.get(3));
			applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get()));
		}
	}
	
	public String web( Color color){
		if(color == null) {
			return "#7cd0f9";// this is work around for null error on color.getRed()
		}else {
			return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
		}
	}
	
	public void pulse(ColorMode c) {
		List<Color> tocolors = layColors.getColors(c);
		ObjectProperty<Color> tc1 = new SimpleObjectProperty<>(tocolors.get(0));
		ObjectProperty<Color> tc2 = new SimpleObjectProperty<>(tocolors.get(1));
		ObjectProperty<Color> tc3 = new SimpleObjectProperty<>(tocolors.get(2));
		ObjectProperty<Color> tc4 = new SimpleObjectProperty<>(tocolors.get(3));
		
		applyStyle(web(tc1.get()), web(tc2.get()), web(tc3.get()), web(tc4.get()));
		lay.getPane().scaleXProperty().set(1.2);
		lay.getPane().scaleYProperty().set(1.2);
		
	    tc4.addListener((obs, oldColor, n) -> applyStyle(web(tc1.get()), web(tc2.get()), web(tc3.get()), web(tc4.get())));
	    
	    KeyFrame kf1 = new KeyFrame(Duration.millis(1000), new KeyValue(tc1, c1.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf2 = new KeyFrame(Duration.millis(1000), new KeyValue(tc2, c2.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf3 = new KeyFrame(Duration.millis(1000), new KeyValue(tc3, c3.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf4 = new KeyFrame(Duration.millis(1000), new KeyValue(tc4, c4.get(), Interpolator.EASE_BOTH));
	    
	    KeyFrame sx = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleXProperty(), 1, Interpolator.EASE_BOTH));
	    KeyFrame sy = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleYProperty(), 1, Interpolator.EASE_BOTH));
	    
	    Timeline timeline = new Timeline(kf1, kf2, kf3, kf4, sx, sy);
	    timeline.setCycleCount(1);
	    timeline.setOnFinished(e -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));//return to original color
	    timeline.play();
	}
	
	public void pulseSize(ColorMode c) {
		List<Color> tocolors = layColors.getColors(c);
		ObjectProperty<Color> tc1 = new SimpleObjectProperty<>(tocolors.get(0));
		ObjectProperty<Color> tc2 = new SimpleObjectProperty<>(tocolors.get(1));
		ObjectProperty<Color> tc3 = new SimpleObjectProperty<>(tocolors.get(2));
		ObjectProperty<Color> tc4 = new SimpleObjectProperty<>(tocolors.get(3));
		
		applyStyle(web(tc1.get()), web(tc2.get()), web(tc3.get()), web(tc4.get()));
//		lay.getPane().scaleXProperty().set(1.2);
//		lay.getPane().scaleYProperty().set(1.2);
		
	    tc4.addListener((obs, oldColor, n) -> applyStyle(web(tc1.get()), web(tc2.get()), web(tc3.get()), web(tc4.get())));
	    
	    KeyFrame kf1 = new KeyFrame(Duration.millis(100), new KeyValue(tc1, c1.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(tc2, c2.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf3 = new KeyFrame(Duration.millis(100), new KeyValue(tc3, c3.get(), Interpolator.EASE_BOTH));
	    KeyFrame kf4 = new KeyFrame(Duration.millis(100), new KeyValue(tc4, c4.get(), Interpolator.EASE_BOTH));
	    
//	    new KeyFrame(Duration.ZERO,         event -> scale.setValue(1)),
//        new KeyFrame(Duration.seconds(0.5), event -> scale.setValue(1.1))
//	    KeyFrame sx = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleXProperty(), 1, Interpolator.EASE_BOTH));
//	    KeyFrame sy = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleYProperty(), 1, Interpolator.EASE_BOTH));
	    
	    Timeline timeline = new Timeline(kf1, kf2, kf3, kf4);
	    timeline.setCycleCount(1);
	    timeline.setOnFinished(e -> applyStyle(web(c1.get()), web(c2.get()), web(c3.get()), web(c4.get())));//return to original color
	    timeline.play();
	}
}

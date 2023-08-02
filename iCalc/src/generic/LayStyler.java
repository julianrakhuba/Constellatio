package generic;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import status.ColorMode;

public class LayStyler {
	private LayColors layColors = new LayColors();
	private LAY lay;
	private ObjectProperty<Color> color = new SimpleObjectProperty<>();

	public LayStyler(LAY lay) {
		this.lay = lay;
		color.addListener((obs, oldColor, n) -> applyStyle(color.get()));
	}
	
	private void applyStyle(Color color) {
		String st4;
		if(color == null) {
			st4 =  "#7cd0f9";
		}else {
			st4 = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
		}
		
//		String st4 = web(color);
		
		
		
		String style = "-fx-background-color: linear-gradient(derive(" + st4 + ", 90.0%), derive(" + st4 + ", -10.0%)), radial-gradient(center 50% -40%, radius 200%, derive(" + st4 + ", 60.0%) " + " 45%, " + st4 + " 50%);";		
		lay.getPane().setStyle(style);
	}

	public void updateLayStyle(ColorMode c) {
		boolean animate = true;
		if(animate
				&& ( color.getValue() != null)
				) {
		    KeyFrame kf4 = new KeyFrame(Duration.millis(200), new KeyValue(color, layColors.getColor(c), Interpolator.EASE_BOTH));
		    Timeline timeline = new Timeline( kf4);
		    timeline.setCycleCount(1);
		    timeline.play();
		}else {
			color.setValue(layColors.getColor(c));
			applyStyle(color.get());
		}
	}
	
//	private String web( Color color){
//		if(color == null) {
//			return "#7cd0f9";
//		}else {
//			return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
//		}
//	}
	
	public void pulse(ColorMode c) {
		ObjectProperty<Color> tc4 = new SimpleObjectProperty<>(layColors.getColor(c));
		
		applyStyle(tc4.get());
		lay.getPane().scaleXProperty().set(1.2);
		lay.getPane().scaleYProperty().set(1.2);
		
	    tc4.addListener((obs, oldColor, n) -> applyStyle(tc4.get()));

	    KeyFrame kf4 = new KeyFrame(Duration.millis(1000), new KeyValue(tc4, color.get(), Interpolator.EASE_BOTH));
	    KeyFrame sx = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleXProperty(), 1, Interpolator.EASE_BOTH));
	    KeyFrame sy = new KeyFrame(Duration.millis(1000), new KeyValue(lay.getPane().scaleYProperty(), 1, Interpolator.EASE_BOTH));
	    
	    Timeline timeline = new Timeline(kf4, sx, sy);
	    timeline.setCycleCount(1);
	    timeline.setOnFinished(e -> applyStyle(color.get()));//return to original color
	    timeline.play();
	}
}

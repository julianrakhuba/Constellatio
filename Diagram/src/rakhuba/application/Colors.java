package rakhuba.application;

import javafx.scene.paint.Color;

public class Colors {
	static double BLUE = 200;
	static double VIOLET = 210;
	static double GREEN = 120; //45
	static double PINK = 330; //0
	static double LAVANDER = 255;
	static double YELLOW = 75;//YELLOW	
	static double BLUE2 = 215;
	

	public String getLight(double hue) {
		return this.buildStyle(hue, 0.0, 1.00);
	}
	
	public String getMedium(double hue) {
		return this.buildStyle(hue, 0.1, 0.98);
	}
	
	public String getDark(double hue) {
		return this.buildStyle(hue, 0.3, 0.95);
	}
	
	//PRIVATE
	private String buildStyle(double h, double s, double b) {
		return "-fx-background-color: linear-gradient(" + rgb(h,s,b) + ", " + rgb(h,s + 0.5,b) + "), radial-gradient(center 50% -40%, radius 200%, " + rgb(h,s + 0.1,b) + " 45%, " + rgb(h,s + 0.4,b) + " 50%);";
	}
	
	private String rgb(double h, double s, double b) {
		Color c = Color.hsb(h, s, b);
		return String.format("#%02x%02x%02x", (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
	}
	
}

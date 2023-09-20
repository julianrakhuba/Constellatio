package sidePanel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class HeadingLabel extends StackPane {
	private Label label = new Label();

	//Large
	public HeadingLabel(String string, String color) {//lay name, messages
		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-size: 15; -fx-text-fill: #738296;");

		this.setStyle(
				"-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3) , 3, 0.3, 0.0, 0.0); -fx-padding:3px 7px 3px 7px; -fx-text-fill: #738296; -fx-background-color: rgba(255, 255, 255,1);\n"
						+ "   -fx-background-insets: 0 0 -1 0, 0,1;\n" + "   -fx-background-radius: 15;");
	}
	
	//small
	public HeadingLabel(String string) {//conditions, functions options

		label.setStyle(
				"-fx-effect: innershadow(gaussian, rgba(0, 0, 0, 0.3) , 3, 0.3, 0.0, 0.0); -fx-padding:1px 10px 1px 10px; -fx-text-fill: #738296; -fx-background-color: rgba(255, 255, 255,1);   -fx-background-insets: 0 0 -1 0, 0,1;   -fx-background-radius: 10;");

		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER_LEFT);
	}



}
package sidePanel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class HeaderLabel extends StackPane {
	private Label label = new Label();

	public HeaderLabel(String string) {

		label.setStyle(
				"-fx-effect: dropshadow(gaussian, derive(#1E90FF, 5%) , 2, 0.4, 0.0, 0.0); -fx-padding:1px 10px 1px 10px; -fx-text-fill: #738296; -fx-background-color: rgba(255, 255, 255, 1);   -fx-background-insets: 0 0 -1 0, 0,1;   -fx-background-radius: 10;");

		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER_LEFT);
	}

	public HeaderLabel(String string, String color) {
		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-size: 15; -fx-text-fill: #708090;");

		this.setStyle(
				"-fx-effect: dropshadow(gaussian, derive(#1E90FF, 5%) , 2, 0.4, 0.0, 0.0); -fx-padding:3px 7px 3px 7px; -fx-text-fill: #738296; -fx-background-color: rgba(255, 255, 255, 1);\n"
						+ "   -fx-background-insets: 0 0 -1 0, 0,1;\n" + "   -fx-background-radius: 15;");
	}

}

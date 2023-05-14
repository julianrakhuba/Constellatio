package rakhuba.sidePanel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class HeaderLabel extends StackPane {
	private Label label = new Label();
	
	public HeaderLabel(String string) {
		label.setStyle("-fx-background-color: white; -fx-border-color: #ade0ff;  -fx-padding:0 7px 0 7px; -fx-background-radius: 10 10 10 10;  -fx-border-radius: 10 10 10 10; -fx-text-fill: #708090;-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);");
		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER_LEFT);
	}

	public HeaderLabel(String string, String color) {
		label.setText(string);
		this.getChildren().add(label);
		this.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-size: 15; -fx-text-fill: #708090;");
		this.setStyle("-fx-background-color: white; -fx-border-color: #ade0ff;  -fx-background-radius: 10 10 10 10 ;  -fx-border-radius: 10 10 10 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);");
	}

}

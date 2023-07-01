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


//
//
//
//package sidePanel;
//
//import javafx.geometry.Pos;
//import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
//
//public class HeaderLabel extends StackPane {
//	private Label label = new Label();
//	
//	public HeaderLabel(String string, String color) {
//		label.setText(string);
//		this.getChildren().add(label);
//		this.setAlignment(Pos.CENTER);
//		label.setStyle("-fx-font-size: 15; -fx-text-fill: #738296;");
////		label.setStyle("-fx-background-color: -fx-shadow-highlight-color, linear-gradient(to bottom, derive(-fx-background, -10%), derive(-fx-background, -5%)),linear-gradient(from 0px 0px to 0px 4px, derive(-fx-background, -4%), derive(-fx-background, 10%));");
////		this.setStyle("-fx-background-color: white; -fx-border-color: #ade0ff;  -fx-background-radius: 10 10 10 10 ;  -fx-border-radius: 10 10 10 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);");
//	
//		this.setStyle(" -fx-background-color: -fx-shadow-highlight-color,\n"
//				+ "        linear-gradient(to bottom, derive(-fx-background, -10%), derive(-fx-background, -5%)),\n"
//				+ "        linear-gradient(from 0px 0px to 0px 4px, derive(-fx-background, -4%), derive(-fx-background, 10%));\n"
//				+ "   -fx-background-insets: 0 0 -1 0, 0,1;\n"
//				+ "   -fx-background-radius: 15;\n"
//				+ "   -fx-padding: 6px;");
//
//	}
//	
//	public HeaderLabel(String string) {
////		label.setStyle("-fx-background-color: white; -fx-border-color: #ade0ff;  -fx-padding:0 7px 0 7px; -fx-background-radius: 10 10 10 10;  -fx-border-radius: 10 10 10 10; -fx-text-fill: #708090;-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);");
//				
//		label.setStyle("-fx-padding:2px 7px 2px 7px; -fx-text-fill: #738296; -fx-background-color: -fx-shadow-highlight-color,\n"
//				+ "        linear-gradient(to bottom, derive(-fx-background, -10%), derive(-fx-background, -5%)),\n"
//				+ "        linear-gradient(from 0px 0px to 0px 4px, derive(-fx-background, -4%), derive(-fx-background, 10%));\n"
//				+ "   -fx-background-insets: 0 0 -1 0, 0,1;\n"
//				+ "   -fx-background-radius: 10;");
//		
//		label.setText(string);
//		this.getChildren().add(label);
//		this.setAlignment(Pos.CENTER_LEFT);
//	}
//
//}



//public HeaderLabel(String string, String color) {
//	label.setText(string);
//	this.getChildren().add(label);
//	this.setAlignment(Pos.CENTER);
//	label.setStyle("-fx-font-size: 15; -fx-text-fill: #708090;");
//	this.setStyle("-fx-background-color: white; -fx-border-color: #ade0ff;  -fx-background-radius: 10 10 10 10 ;  -fx-border-radius: 10 10 10 10; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);");
//}

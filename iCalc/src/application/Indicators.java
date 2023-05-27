package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Indicators {
	private HBox root = new HBox();
	private Pane conditions = new Pane();
	private Pane fields = new Pane();
	private Pane error = new Pane();
	private Pane remoteFields = new Pane();
	
	public Indicators() {
		conditions.getStyleClass().add("indicatorBlue");
		fields.getStyleClass().add("indicatorGreen");
		error.getStyleClass().add("indicatorRed");
		remoteFields.getStyleClass().add("indicatorGreen");
		
		root.setMinHeight(3);
		root.setMaxHeight(3);
		root.setSpacing(1);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(0,0,0,0));
		
		
		root.setBorder(null);
	}
	
	public HBox getRoot() {
		return root;
	}
	
	public void errorOn() {
		if(!root.getChildren().contains(error))  root.getChildren().add(error);
	}
	
	public void errorOff() {
		if(root.getChildren().contains(error))  root.getChildren().remove(error);
	}
	
	public void conditionsOn() {
		if(!root.getChildren().contains(conditions))  root.getChildren().add(conditions);
	}
	
	public void conditionsOff() {
		if(root.getChildren().contains(conditions))  root.getChildren().remove(conditions);
	}
	
	public void fieldsOn() { //add, logic xml field loop
		if(!root.getChildren().contains(fields))  root.getChildren().add(fields);
	}
	
	public void fieldsOff() {//remove
		if(root.getChildren().contains(fields))  root.getChildren().remove(fields);
	}
	
	public void remoteFieldsOn() {//add, logic remoteFieldsOn
		if(!root.getChildren().contains(remoteFields))  root.getChildren().add(remoteFields);
	}
	
	public void remoteFieldsOff() {
		if(root.getChildren().contains(remoteFields))  root.getChildren().remove(remoteFields);
	}
	
	
}

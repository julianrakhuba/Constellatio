package sidePanel;

import file.NFile;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import status.Status;

public class Message {
	private Pane selectorPane = new Pane();
	private Label label = new Label();	
	private Property<Status> status = new SimpleObjectProperty<Status>(Status.UNACTIVE);
	private String message;
	private String description;
	
	
	public Message(NFile nFile, String message, String description) {
		this.message = message;
		this.description = description;
		selectorPane.getStyleClass().add(status.getValue().toString());
		selectorPane.setMinSize(14, 14);
		selectorPane.setMaxSize(14, 14);
		label.setStyle("-fx-font-size: 10; -fx-text-fill: #525e6b;");

		label.setGraphic(selectorPane);
		label.setText(message + " " + description);
		selectorPane.setOnMouseClicked(e -> {
			
			if(e.isControlDown()
//					nFile.getFileManager().napp.getNscene().getHoldKeys().contains("CONTROL")
					) {
				nFile.getMessages().remove(this);
			}
			e.consume();
		});
		status.addListener((e,a,d) -> {
			selectorPane.getStyleClass().clear(); 
			selectorPane.getStyleClass().add(status.getValue().toString());	
		});
	}
	
	public void activeClick() {

	}

	public Label getLabel() {
		label.setOnMouseClicked(e ->  this.activeClick());
		return label;		
	}

	public String getError() {
		return message;
	}

	public String getDescription() {
		return description;
	}

}

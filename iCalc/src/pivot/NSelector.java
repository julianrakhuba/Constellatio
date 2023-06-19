package pivot;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NSelector extends SimpleObjectProperty<Boolean> {
	private Pane pane = new Pane();
	private Label label = new Label();

	public NSelector() {
		this.setValue(false);
		pane.setMinSize(17,17); 
		pane.setMaxSize(17,17);
		pane.getStyleClass().add("grayMenuSelector");//default		
		label.setGraphic(pane);
		label.setTextFill(Color.rgb(60,60,60));
//		label.setMinWidth(120);
		
		this.addListener((a,b,c) ->{
//			System.out.println("NSelector listener: " + c);
			pane.getStyleClass().clear();
			if(c) pane.getStyleClass().add("blueMenuSelector");
			else pane.getStyleClass().add("grayMenuSelector");
		});
	}

	public NSelector(String string) {
		this();
		label.setText(string);
	}

	public NSelector(String string, boolean b) {
		this();
		label.setText(string);
		
//  		sideLabel.setStyle("-fx-text-fill: #ababab; -fx-font-size: 12;");//overwrite text, ugly work around

		label.setStyle("-fx-font-size: 12; -fx-text-fill: #ababab;");
	}

	public Label getLabel() {
		return label;
	}
	
	public Pane getPane() {
		return pane;
	}

}

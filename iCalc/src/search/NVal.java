package search;

import elements.ValuesELM;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import status.Selector;

public class NVal {
	private Property<Selector> selected = new SimpleObjectProperty<Selector>(Selector.UNSELECTED);
	private String string;
	private ValuesELM valuesELM;
	private Pane selectorPane = new Pane();
	private Label label;

	public NVal(String string, Selector selected, ValuesELM valuesELM) {
		this.string = string;
		this.valuesELM = valuesELM;
		label = new Label(string, selectorPane);
//		label.setStyle("-fx-font-size: 12; -fx-text-fill: #525e6b;");
		label.setStyle("-fx-text-fill: #525e6b;");

		this.selected.addListener((a,b,c)-> this.updateStyle());
		this.selected.setValue(selected);
		selectorPane.setMinSize(14, 14);
		selectorPane.setMaxSize(14, 14);
	}
	
	public String  getText() {
		return string;
	}

	public Selector getSelected() {
		return selected.getValue();
	}

	public void setSelected(Selector selected) {
		this.selected.setValue(selected);
	}

	public void click(MouseEvent e) {
		valuesELM.click(this,e);
	}

	public Label getLabel() {		
		return label ;
	}
	
    public void updateStyle() {
    	if(this.getSelected() == Selector.SELECTED) {
    		selectorPane.setId("list-Item-Blue");
    	}else{
    		selectorPane.setId("nothing");
    	}
    }

}

/*******************************************************************************
 *   Copyright (c) 2023 Constellatio
 *  
 *   This software is released under the [Educational/Non-Commercial License or Commercial License, choose one]
 *  
 *   Educational/Non-Commercial License (GPL):
 *  
 *   Permission is hereby granted, free of charge, to any person or organization
 *   obtaining a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including without
 *   limitation the rights to use, copy, modify, merge, publish, distribute,
 *   sublicense, and/or sell copies of the Software, and to permit persons to
 *   whom the Software is furnished to do so, subject to the following conditions:
 *  
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *  
 *   THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 *  
 *   Commercial License:
 *  
 *   You must obtain a separate commercial license if you
 *   wish to use this software for commercial purposes. Please contact
 *   rakhuba@gmail.com for licensing information.
 *  
 *  
 *******************************************************************************/
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

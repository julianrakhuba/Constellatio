/*******************************************************************************
 * /*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *  *******************************************************************************/
 *******************************************************************************/
package configure;

import clientcomponents.NKey;
import clients.XMLBase;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
//import rakhuba.configure.NLink;
import status.Selector;

public class NLink {
	private Property<Selector> selected = new SimpleObjectProperty<Selector>(Selector.UNSELECTED);
	private Pane selectorPane = new Pane();
	private Label label;
	private ListView<NLink> listViewKeyMap;
	private NKey nkey;
	private XMLBase base;
	
	public NLink(NKey nkey, Selector sel, ListView<NLink> listViewKeyMap, XMLBase base) {
		this.listViewKeyMap = listViewKeyMap;
		this.nkey = nkey;
		this.base = base;
		label = new Label(nkey.getTable() + "." + nkey.getColumn() + " = " + nkey.getRTable() +"."+ nkey.getRColumn(), selectorPane);
		label.setStyle("-fx-text-fill: #525e6b;");

		this.selected.addListener((a,b,c)-> this.updateStyle());
		this.selected.setValue(sel);
		selectorPane.setMinSize(14, 14);
		selectorPane.setMaxSize(14, 14);
	}
	
	public Selector getSelected() {
		return selected.getValue();
	}

	public void setSelected(Selector selected) {
		this.selected.setValue(selected);
	}

	public Node getLabel() {		
		return label ;
	}
	
    public void updateStyle() {
    	if(this.getSelected() == Selector.SELECTED) {
    		selectorPane.setId("list-Item-Blue");
    	}else{
    		selectorPane.setId("nothing");
    	}
    }

	public void click(MouseEvent e) {
		if(e.isControlDown()) {
			listViewKeyMap.getItems().remove(this);
			base.getKeys().remove(nkey);
		}
	}

}

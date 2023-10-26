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
package sidePanel;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import search.NVal;

public class ValueCell extends ListCell<NVal> {
	
    public ValueCell() {
    	this.setPadding(new Insets(6,4,6,4));
	}

    public void updateItem(NVal value, boolean empty) {
        super.updateItem(value, empty);        
        this.setOnMouseClicked(e -> {
        	if(value != null) {
        		value.click(e);
        	}
        });
        
        if (empty) {
        	this.setText(null);
            this.setId(null);
            this.setGraphic(null);
        } else {
        	this.setGraphic(value.getLabel());
        }
    }
	
}
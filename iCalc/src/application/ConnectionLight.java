/*******************************************************************************
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
 *******************************************************************************/
package application;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import status.ConnectionStatus;

public class ConnectionLight extends Pane {	
	private Property<ConnectionStatus> status = new SimpleObjectProperty<ConnectionStatus>(ConnectionStatus.DISCONNECTED);
	
	public ConnectionLight() {
		super();
		this.getStyleClass().add("connectionLightOrange");
		this.setStatus(ConnectionStatus.DISCONNECTED);
		
		status.addListener((a,b,c) -> {
			if(c == ConnectionStatus.CONNECTED) {
				this.getStyleClass().clear();
				this.getStyleClass().add("connectionLightBlue");
			}else {
				this.getStyleClass().clear();
				this.getStyleClass().add("connectionLightOrange");
			}
		});
	}
	
	public void setStatus(ConnectionStatus status) {
		this.status.setValue(status);
	}
	
	public ConnectionStatus getStatus() {
		return this.status.getValue();
	}
}

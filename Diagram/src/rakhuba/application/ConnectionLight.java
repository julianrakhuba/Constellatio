package rakhuba.application;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import rakhuba.status.ConnectionStatus;

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

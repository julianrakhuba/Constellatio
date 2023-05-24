package login;

import javafx.scene.control.ListCell;
//import rakhuba.login.Login;

public class LoginCell extends ListCell<Login> {
	@Override
    public void updateItem(Login login, boolean empty) {  
        super.updateItem(login, empty);
        if (empty) {
            setText("null"); 
            setGraphic(null);
        } else {
        	setText(login.getLabel());
        	setGraphic(null);
        }
    }	
}
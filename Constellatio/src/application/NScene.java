package application;

import java.util.HashSet;

import generic.ACT;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
//import rakhuba.application.Constellatio;
import launcher.Constellatio;


public class NScene extends Scene {
	private static HashSet<String> currentKeys = new HashSet<String>();
	
	public NScene(Parent root, Constellatio napp) {
		super(root);
		this.getStylesheets().add(getClass().getResource("/Graph.css").toExternalForm());
	
        this.setOnKeyPressed(e -> {   
        	if(e.getCode() == KeyCode.BACK_SPACE) {
        		if(napp.filemanager.getActiveNFile() != null) {
        			ACT act = napp.filemanager.getActiveNFile().getActivity();
                	if(e.getCode() == KeyCode.BACK_SPACE) {
                		act.passKeyEvent(e);
                	}
        		}
			}
            currentKeys.add(e.getCode().toString());
        });
        
        this.setOnKeyReleased(e -> {
        	currentKeys.remove(e.getCode().toString());
        	if(e.getCode().toString().equals("ALT")) {// this is workaround for app  menu was selected when function menu is in use
        		e.consume();
        	}
        });                
	}
	
	public HashSet<String> getHoldKeys() {
		return currentKeys;
	}
}
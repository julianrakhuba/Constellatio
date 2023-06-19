package application;

import java.util.HashSet;

import generic.ACT;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;


public class NScene extends Scene {
	private static HashSet<String> currentKeys = new HashSet<String>();
	private Constellatio napp;
	public NScene(Parent root, Constellatio napp) {
		super(root);
		this.napp = napp;
		
		if (napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			this.getStylesheets().add(getClass().getResource("/GraphDark.css").toExternalForm());
		}else {
			this.getStylesheets().add(getClass().getResource("/Graph.css").toExternalForm());
		}

			
	
        this.setOnKeyPressed(e -> {   
        	if(e.getCode() == KeyCode.BACK_SPACE) {
        		if(napp.getFilemanager().getActiveNFile() != null) {
        			ACT act = napp.getFilemanager().getActiveNFile().getActivity();
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

	public Constellatio getNapp() {
		return napp;
	}
}
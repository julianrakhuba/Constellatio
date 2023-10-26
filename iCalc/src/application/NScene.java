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
package application;

import java.util.HashSet;

import generic.ACT;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;


public class NScene extends Scene {
	private static HashSet<String> currentKeys = new HashSet<String>();
	private Constellatio napp;
    private String graphDark = getClass().getResource("/GraphDark.css").toExternalForm();//"/GraphDark.css";
    private String graph = getClass().getResource("/Graph.css").toExternalForm();//"/GraphDark.css";

    
	public NScene(Parent root, Constellatio napp) {
		super(root);
		this.napp = napp;
		
		if (napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			this.getStylesheets().add(graphDark);
		}else {
			this.getStylesheets().add(graph);
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
	
//    public void refreshStyle() {
//    	if (napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
//			this.getStylesheets().remove(graph);
//			this.getStylesheets().add(graphDark);
//		}else {
//			this.getStylesheets().remove(graphDark);
//			this.getStylesheets().add(graph);
//		}
//    }
    
	
	public HashSet<String> getHoldKeys() {
		return currentKeys;
	}

	public Constellatio getNapp() {
		return napp;
	}
}
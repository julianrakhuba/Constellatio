
/*******************************************************************************
 *  MIT License
 *
 * Copyright (c) 2023 Julian Rakhuba
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Indicators {
	private HBox root = new HBox();
	private Pane conditions = new Pane();
	private Pane fields = new Pane();
	private Pane error = new Pane();
	private Pane remoteFields = new Pane();
	
	public Indicators() {
		conditions.getStyleClass().add("indicatorBlue");
		fields.getStyleClass().add("indicatorGreen");
		error.getStyleClass().add("indicatorRed");
		remoteFields.getStyleClass().add("indicatorGreen");
		
		root.setMinHeight(3);
		root.setMaxHeight(3);
		root.setSpacing(1);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(0,0,0,0));
		
		root.setFocusTraversable(false);
		
		conditions.setFocusTraversable(false);
		fields.setFocusTraversable(false);
		error.setFocusTraversable(false);
		remoteFields.setFocusTraversable(false);

		root.setBorder(null);
	}
	
	public HBox getRoot() {
		return root;
	}
	
	public void errorOn() {
		if(!root.getChildren().contains(error))  root.getChildren().add(error);
	}
	
	public void errorOff() {
		if(root.getChildren().contains(error))  root.getChildren().remove(error);
	}
	
	public void conditionsOn() {
		if(!root.getChildren().contains(conditions))  root.getChildren().add(conditions);
	}
	
	public void conditionsOff() {
		if(root.getChildren().contains(conditions))  root.getChildren().remove(conditions);
	}
	
	public void fieldsOn() { //add, logic xml field loop
		if(!root.getChildren().contains(fields))  root.getChildren().add(fields);
	}
	
	public void fieldsOff() {//remove
		if(root.getChildren().contains(fields))  root.getChildren().remove(fields);
	}
	
	public void remoteFieldsOn() {//add, logic remoteFieldsOn
		if(!root.getChildren().contains(remoteFields))  root.getChildren().add(remoteFields);
	}
	
	public void remoteFieldsOff() {
		if(root.getChildren().contains(remoteFields))  root.getChildren().remove(remoteFields);
	}
	
	
}

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
 *   sub-license, and/or sell copies of the Software, and to permit persons to
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
 *   (documentation will be provided for commercial license)
 *  
 *  
 *******************************************************************************/
package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import status.Selection;
import status.Selector;

public class Group {
	public ObjectProperty<String> status = new SimpleObjectProperty<String>("Open");
	private ObservableList<SearchCON> items = FXCollections.observableArrayList();
 
	private Pane oldbutton;
	private Arc arcButton;

	private Level level;
	private Level childLevel;
	
	
	public Group(Level level) {
		this.level = level;
		oldbutton = new Pane();
		arcButton = new Arc();
		arcButton.setType(ArcType.OPEN);
		arcButton.setStrokeLineCap(StrokeLineCap.ROUND);
		arcButton.setPickOnBounds(false);
//		arcButton.setStyle("-fx-stroke: rgba(255, 255, 255, 1); -fx-stroke-width: 6px; -fx-fill: null; -fx-effect: dropshadow(gaussian, derive(#1E90FF, 60%) , 3, 0.2, 0.0, 0.0);");
		arcButton.setOnMouseClicked(e ->{
			click();
			e.consume();
		});

		oldbutton.getStyleClass().add("Open");
		arcButton.getStyleClass().add("OpenArc");

		
		oldbutton.setOnMouseClicked(e ->{
			click();
			e.consume();
		});
		status.addListener((e,s,f) -> {
			oldbutton.getStyleClass().clear();
			oldbutton.getStyleClass().add(status.get());
			
			arcButton.getStyleClass().clear();
			arcButton.getStyleClass().add(status.get() + "Arc");
		});
		
		items.addListener((ListChangeListener<SearchCON>) c -> {
			if(items.size() == 0) {
				if (childLevel != null) {
					this.pullChildrenUp();// PULL UP CHILDREN
				}					
				if( items.size() == 0) {
					this.removeFromLevel();
				}					
			}			
		});
		
	}
	
	public Pane getPane() {
		return oldbutton;
	}
	
	public ObservableList<SearchCON> getItems(){
		return items;
	}

	
	public boolean  contains(SearchCON con) {
		return items.contains(con);
	}
	
	public void add(SearchCON searchCON) {
		items.add(searchCON);
		searchCON.addGroup(this);
	}
	
	public void remove(SearchCON con) {
		items.remove(con);
		con.removeGroup(this);
	}
	
	public int size() {
		return items.size();
	}
	
	protected void click() {
		if (level.getActiveGroup() == this) {
			if(status.get().equals("Open"))  this.hide();
			else if(status.get().equals("Closed")) this.down();
			else if(status.get().equals("Down")) this.show();
		}else{
			level.getActiveGroup().hide();
			level.setActiveGroup(this);
			this.show();
		}
	}
	
	protected void show () {
		status.set("Open");
		if(getChild() != null) getChild().hide();
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.SELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.SELECTED);
		});
	}
	
	public void  hide() {
		status.set("Closed");
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.UNSELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.UNSELECTED);
		});
		if(getChild() != null) getChild().hide();
	}
	
	private void down() {
		status.set("Down");
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.UNSELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.UNSELECTED);
		});
		if(getChild() != null) getChild().show();
	}

	public Level getChild() {
		return childLevel;
	}

	public void setChildLevel(Level childLevel) {
		this.childLevel = childLevel;
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	//SQL
	public void buildSQL(SQL sql) {
		ArrayList<SearchCON> tmp = new  ArrayList<SearchCON>(items);
		if(tmp.size() > 1 || getChild() != null) sql.open();
			tmp.forEach(con -> {
				con.buildSQL(sql);//TODO monitor this, will it brake after using TEXT Objects
				if((tmp.indexOf(con) + 1) < tmp.size()) sql.AND();//sql.addNText(new NText(" AND ", con.getLay()));
			});			
		if (getChild() != null) getChild().buildSQL(sql.AND());
		if(tmp.size() > 1 || getChild() != null) sql.close();
	}
	
	//Search SQL
	public void buildSearchSQL(SQL sql) {
		ArrayList<SearchCON> tmp = new  ArrayList<SearchCON>(items);
		if(status.get() == "Open") {
			this.buildSQL(sql);
		}else if(status.get() == "Down") {
			if(tmp.size() > 1 || getChild() != null) sql.open();
			tmp.forEach(con -> {
				con.buildSQL(sql);
				if((tmp.indexOf(con) + 1) < tmp.size()) sql.AND();//sql.addNText(new NText(" AND ", null));
			});
			if (getChild() != null && getChild().getActiveGroup().status.get() != "Closed") getChild().getActiveGroup().buildSearchSQL(sql.AND());
			if(tmp.size() > 1 || getChild() != null) sql.close();
		}
	}
	
	public void pullChildrenUp() {
		childLevel.getGroups().forEach(group -> {
			group.setLevel(level);
			level.getGroups().add(group);
		});
	}

	public void removeFromLevel() {
		level.getGroups().remove(this);
		if (level.getGroups().size() > 0) {
			// ACTIVATE OTHER GROUP
			 level.setActiveGroup(level.getGroups().get(level.getGroups().size() - 1));
		}else {
			 level.setActiveGroup(null);
		}
	}

	public HashSet<SearchCON> getLogicConditions(HashSet<SearchCON> mergedCon) {
		items.forEach(con -> mergedCon.add(con));
		if (getChild() != null) childLevel.getLogicConditions(mergedCon);
		return mergedCon;
	}

	public void saveGroup(Document document, Element level) {
		Element group = document.createElement("group");
		level.appendChild(group);
		items.forEach(con -> {
			Element searchE = document.createElement("searchCON");
			searchE.setAttribute("uniqueId", con.getUniqueId());	
			group.appendChild(searchE);						
		});
		if (childLevel != null) childLevel.saveLevel(document, group);
	}

	public Arc getArc() {
		return arcButton;
	}
	
	public List<Group> getAllGroups() {
		List<Group> grz =  new ArrayList<Group>();
		if (childLevel != null) grz.addAll(childLevel.getGroupsAll());
		return grz;
	}

}
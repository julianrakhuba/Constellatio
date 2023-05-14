package rakhuba.logic;

import java.util.ArrayList;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import rakhuba.status.Selection;
import rakhuba.status.Selector;

public class Group {
	public ObjectProperty<String> status = new SimpleObjectProperty<String>("Open");
	private ObservableList<SearchCON> items = FXCollections.observableArrayList();
 
	private Pane pane = new Pane();
	private Level level;
	private Level childLevel;
	
	public Group(Level level) {
		this.level = level;
		pane.getStyleClass().add("Open");
		pane.setOnMouseClicked(e ->{
			click();
			e.consume();
		});
		status.addListener((e,s,f) -> {
			pane.getStyleClass().clear();
			pane.getStyleClass().add(status.get());
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
		return pane;
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
			if(status.get().equals("Open"))  this.close();
			else if(status.get().equals("Closed")) this.down();
			else if(status.get().equals("Down")) this.open();
		}else{
			level.getActiveGroup().close();
			level.setActiveGroup(this);
			this.open();
		}
	}
	
	protected void open () {
		status.set("Open");
		if(getChild() != null) getChild().close();
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.SELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.SELECTED);
		});
	}
	
	public void  close() {
		status.set("Closed");
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.UNSELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.UNSELECTED);
		});
		if(getChild() != null) getChild().close();
	}
	
	private void down() {
		status.set("Down");
		items.forEach(con -> {
			con.getRoot().setSelected(Selector.UNSELECTED);
			if(con.getRemoteLay() != null) con.getRemoteLay().setSelection(Selection.UNSELECTED);
		});
		if(getChild() != null) getChild().open();
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
		
			tmp.forEach(sel -> {
				sql.append(sel.getFuncColumn());
				sql.append(((tmp.indexOf(sel) + 1) < tmp.size()) ? " AND " : "");
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
			tmp.forEach(sel -> {
				sql.append(sel.getFuncColumn());
				sql.append(((tmp.indexOf(sel) + 1) < tmp.size()) ? " AND " : "");
			});
			if (getChild() != null && getChild().getActiveGroup().status.get() != "Closed") getChild().getActiveGroup().buildSearchSQL(sql.AND());
			if(tmp.size() > 1 || getChild() != null) sql.close();
		}
	}
	
	public void pullChildrenUp() {
		childLevel.getGroups().forEach(group -> {
			group.setLevel(level);
			level.getGroups().add(group);
			level.getLevelHBox().getChildren().add(group.getPane());
		});
	}

	public void removeFromLevel() {
		level.getGroups().remove(this);
		level.getLevelHBox().getChildren().remove(pane);
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

}
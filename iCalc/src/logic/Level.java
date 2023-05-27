package logic;

import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import generic.LAY;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class Level {
	private HBox levelHBox = new HBox();
	public ObservableList<Group> groups = FXCollections.observableArrayList();	
	private LAY lay;
	private Group activeGroup;
	private Group parentGroup;

	public Level(LAY lay, Group parentGroup) {
		this.lay = lay;
		this.parentGroup = parentGroup;
		levelHBox.setAlignment(Pos.CENTER_LEFT);
		levelHBox.setSpacing(3.0);
		this.open();
		
		//AutoRemoveLevel
		groups.addListener((ListChangeListener<Group>) c -> {
			if(c.getList().size() == 0 && this.parentGroup != null) {
				lay.getLogic().getChildren().remove(levelHBox);// visual remove
				parentGroup.setChildLevel(null);
			}
		});	
	}
	
	public HBox getLevelHBox() {
		return levelHBox;
	}
	
	public void open() {
		if(!lay.getLogic().getChildren().contains(levelHBox)) lay.getLogic().getChildren().add(levelHBox);
		if(activeGroup != null) activeGroup.open();
	}
	
	public void close() {
		if(lay.getLogic().getChildren().contains(levelHBox)) lay.getLogic().getChildren().remove(levelHBox);
		if(activeGroup != null) activeGroup.close();
	}
	
	public Group getActiveGroup() {
		return activeGroup;
	}

	public void setActiveGroup(Group activeGroup) {
		this.activeGroup = activeGroup;
	}
	
	public Group getDynamicGroup() {
		if(activeGroup != null && activeGroup.status.getValue().equals("Open")) {
			return activeGroup;
		}else if(activeGroup == null || activeGroup.status.getValue().equals("Closed")) {
			Group group = new Group(this);//NEW GROUP
			activeGroup = group;
			groups.add(group);
			levelHBox.getChildren().add(group.getPane());
			return group;
		}else if(activeGroup != null && activeGroup.status.getValue().equals("Down")) {
			if(activeGroup.getChild() == null) activeGroup.setChildLevel(new Level(lay, activeGroup));//NEW LEVEL
			return activeGroup.getChild().getDynamicGroup();
		}
		return null;
	}

	
	public List<Group> getGroups() {
		return groups;
	}
	
	public Group getParentGroup() {
		return parentGroup;
	}

	//SQL MOVE TO LOGIC???
	public SQL buildSQL(SQL sql) {
		if (groups.size() > 1) sql.open();
		groups.forEach(group -> {
			group.buildSQL(sql);
			if ((groups.indexOf(group) + 1) < groups.size()) sql.OR();
		});
		if (groups.size() > 1) sql.close();
		return sql;
	}

	public HashSet<SearchCON> getLogicConditions(HashSet<SearchCON> searchConditions) {
		groups.forEach(group -> group.getLogicConditions(searchConditions));
		return searchConditions;
	}
	
	public LAY getLay() {
		return lay;
	}

	public void saveLevel(Document document, Element logic) {
		Element level = document.createElement("level");
		logic.appendChild(level);
		groups.forEach(group -> {
			group.saveGroup(document, level);
		});		
	}
}
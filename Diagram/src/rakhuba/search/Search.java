package rakhuba.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import rakhuba.application.Constallatio;
import rakhuba.application.Nnode;
import rakhuba.file.NFile;
import rakhuba.generic.LAY;
import rakhuba.status.ActivityMode;
import rakhuba.status.SqlType;

public class Search extends TextField {	
	private ContextMenu contextMenu;
	private Constallatio napp;
	private ArrayList<String> dynamicChache;
	private ListView<String> listView = new ListView<String>();
	private ArrayList<MenuItem> menuItems3 = new ArrayList<MenuItem>();
	private ArrayList<MenuItem> menuItems2 = new ArrayList<MenuItem>();
	
	
	public Search(Constallatio app) {
		super();
		this.setFocusTraversable(false);
		this.napp = app;
		this.setStyle(" -fx-padding: 2 2 2 18; -fx-background-radius: 15 15 15 15; -fx-border-radius: 15 15 15 15;");// try again to increase text field round corbers
		this.prefWidthProperty().bind(app.searchHBox.widthProperty().divide(1.75));
		this.setMinHeight(30);
		
		contextMenu = new ContextMenu();
		contextMenu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 2 2 2 2;");

		this.setContextMenu(contextMenu);
		this.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, ev ->  ev.consume());

		contextMenu.setOnShowing(e -> {
			if(napp.funcContext.isShowing()) {
				napp.funcContext.hide();
			}
		});
	
//		this.focusedProperty().addListener( (a,b,c) ->{
//			if(c && napp.filemanager.getActiveNFile().getActivityMode() == ActivityMode.EDIT) {
//				Nnode nd = napp.filemanager.getActiveNFile().getActivity().getActiveLayer().nnode;
//				setText(nd.getTable()+".");
//			}
//		});
				
		
		this.textProperty().addListener((observable, oldvalue, newvalue) -> {
			if (getText().length() == 0) {
				contextMenu.hide();
			} else {
				if(napp.filemanager.size() > 0) {					
					String[] splitted = getText().split("\\.", 3);
					String table = splitted[0];
					Nnode toNnode = napp.filemanager.getActiveNFile().getActiveNmap().getNnode(table);
					 // TABLE
					if (splitted.length == 1) {
						regenerateContextMenu(napp.filemanager.getActiveNFile().getActiveNmap().getTablesList(), table, "");
						this.clearDynamicChache();
					// COLUMN
					} else if (splitted.length == 2) {
						String column = splitted[splitted.length - 1];
						if (toNnode != null) {
							String string = "";
							for (int i = 0; i < (splitted.length - 1); i++)  string = string + splitted[i] + ".";
							regenerateContextMenu(toNnode.getColumnsList(), column, string);
						}
						this.clearDynamicChache();
					//VALUE
					} else if (splitted.length == 3 && splitted[splitted.length - 2].length()>0) {
						String column = splitted[splitted.length - 2];
						String value = splitted[splitted.length - 1];
						String string = "";
						for (int i = 0; i < (splitted.length - 1); i++)  string = string + splitted[i] + ".";
						TreeSet<String> list = new TreeSet<String>(this.getValuesList(table, column));
						regenerateContextMenu(list, value, string);
					}
					// workaround for menu shifting down every other time, needs work
					if (!contextMenu.isShowing()) contextMenu.show(this, Side.BOTTOM, 15, 1);
				}
			}
		});
		
		this.setOnAction(e ->{
			System.out.println("setOnAction: " + this.getText());
			String[] splitted = this.getText().split("\\.", 3);	
			if (splitted.length == 3 && splitted[2].length() > 0) {
				Nnode nnod = napp.filemanager.getActiveNFile().getActiveNmap().getNnode(this.getSplit()[0]);
				String col = this.getSplit()[1];
				String val = this.getSplit()[2];					
				napp.filemanager.getActiveNFile().getActivity().newSearchFUNCTION(nnod, col, new PAIR("=", val));//default function is =
				this.clear();
				this.requestFocus();
			}
			e.consume();
		});
		
//		this.setOnKeyPressed(e -> {//default for ENTER is "="
//			if(e.getCode() == KeyCode.BACK_SPACE) {
//				System.out.println("Search BackSpace:");
//				e.consume();
//			}else {
//				e.consume();
//			}
//		});
		
		listView.setMaxHeight(200);
		this.createFunctionMenu();
	}
	
	private void clearDynamicChache() {
		dynamicChache = null;
	}

	private String[] getSplit() {
		return  this.getText().split("\\.", 3);
	}
	
	private ArrayList<String>  getValuesList(String table, String sql_column_name) {
		NFile file = napp.filemanager.getActiveNFile();
		LAY actLAY = file.getActivity().getActiveLayer();
	
		Nnode toNnode = file.getActiveNmap().getNnode(table);
		if(napp.dynamicSearch.isSelected() &&  file.getActivityMode() == ActivityMode.EDIT  &&  actLAY.nnode == toNnode  && (actLAY.getRootLevel().getActiveGroup() != null || actLAY.getSqlType() == SqlType.SQLJ)) {			
			if(dynamicChache ==  null) {
				String full_name = actLAY.getAliase() + "." + sql_column_name;
				dynamicChache =  toNnode.getOpenDAO().readDistinctValues(actLAY.getSearchSQL(full_name, full_name));
			}
			return dynamicChache;
		}else {
			return  toNnode.getStaticValuesList(sql_column_name);
		}
	}
	
	private void regenerateContextMenu(TreeSet<String> list,String val, String dot) {
		LinkedList<String> resultsVal = new LinkedList<>(list.subSet(val, val + Character.MAX_VALUE));		
		LinkedList<String> resultsMax = new LinkedList<>(resultsVal.subList(0, Math.min(resultsVal.size(), 10)));	
		
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		resultsMax.forEach(mx -> {
			Label entryLabel = new Label(mx);
			entryLabel.setPrefWidth(670);
			entryLabel.prefWidthProperty().bind(this.widthProperty().subtract(45));
			
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			item.setMnemonicParsing(true);  
			item.setOnAction(e -> {
				e.consume();
				setText(dot + mx);
				positionCaret(getLength());
			});
			menuItems.add(item);
		});

		contextMenu.getItems().clear();
		contextMenu.getItems().addAll(menuItems);
	}
	
	public void exitEdit() {
		this.clear();
		contextMenu.hide();
	}
	
	private void createFunctionMenu() {
		//3 PART MENU
		List<String>  funcStrings =  Arrays.asList("=", "!=", ">", "<", ">=", "<=", "like");
		funcStrings.forEach(fnc ->{
			MenuItem mi = new MenuItem(fnc);
			menuItems3.add(mi);
			mi.setOnAction( e ->{
				if((this.getSplit().length == 3 && this.getSplit()[2].length() > 0) || (this.getSplit().length == 2 && this.getSplit()[1].length() > 0)) {
					Nnode nnod = napp.filemanager.getActiveNFile().getActiveNmap().getNnode(this.getSplit()[0]);
					String col = this.getSplit()[1];
					String val = ((this.getSplit().length == 3) ? this.getSplit()[2] : null);
					napp.filemanager.getActiveNFile().getActivity().newSearchFUNCTION(nnod, col, new PAIR(fnc, val));					
					this.clear();
					this.requestFocus();
				}
			});
		});
		
		List<String>  inList =  Arrays.asList("in");
		inList.forEach(ml ->{
			MenuItem mi = new MenuItem(ml);
			menuItems2.add(mi);
			mi.setOnAction( e ->{
				PopUpStage inMenu = new PopUpStage(napp,this);
				inMenu.add(listView);
				listView.getItems().clear();
				listView.getItems().addAll(this.getValuesList(this.getSplit()[0], this.getSplit()[1]));
				inMenu.showSearchStage();
				
				listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				listView.setOnKeyPressed(eee -> {
					if (eee.getCode().toString().equals("ENTER")) {
						ArrayList<String> selectedList = new ArrayList<String>(listView.getSelectionModel().getSelectedItems());						
						napp.filemanager.getActiveNFile().getActivity().newSearchIN(napp.filemanager.getActiveNFile().getActiveNmap().getNnode(this.getSplit()[0]), this.getSplit()[1], ml, selectedList);		
						inMenu.hide();
						this.clear();
						this.requestFocus();
					}
				});				
				inMenu.setOnHidden(d -> this.requestFocus());
			});
		});
			
		MenuItem btwItem = new MenuItem("between");
		menuItems2.add(btwItem);
		btwItem.setOnAction(e ->{
			BetweenMenu between = new BetweenMenu(napp, this.getSplit()[0], this.getSplit()[1], this.getValuesList(this.getSplit()[0], this.getSplit()[1]));
			between.setOnHidden(ee -> this.requestFocus());
		});		
	}
		
	public ArrayList<MenuItem> getMenuItems() {
		if(this.getSplit().length == 3) {
			return menuItems3;
		}else if(this.getSplit().length == 2) {
			return menuItems2;
		}else {
			return  new ArrayList<MenuItem>();
		}
	}
	
	public void clear() {
		super.clear();
		this.clearDynamicChache();
	}
	
	public boolean isSwitchableMode() {
		return !this.isFocused() || this.getSplit().length <=1;
	}
}
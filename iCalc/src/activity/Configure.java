package activity;

import java.util.ArrayList;

import application.Nnode;
import application.NnodeLine;
import clientcomponents.NKey;
import clients.XMLBase;
import configure.NKeyCell;
import configure.NLink;
import file.NFile;
import generic.ACT;
import generic.LAY;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import search.PAIR;
import search.PopUpStage;
import status.Selector;

public class Configure extends ACT {
	private Nnode configNnode;

	public Configure(NFile nFile) {
		this.nFile = nFile;
	}

	public void passLAY(LAY lay) {
		this.passNnode(lay.nnode, null);
	}
	public void newSearchFUNCTION(Nnode nnod, String col, PAIR funcVAL) {}
	public void newSearchBETWEEN(Nnode nnod, String col, String from, String to) {}
	public void newSearchIN(Nnode nnod, String col, String in, ArrayList<String> values) {}
	
	public void passNnode(Nnode nnode, MouseEvent e) {
		if(configNnode != null && nnode != configNnode && e != null && e.isShiftDown()) {
			this.createLinkStage(nnode);
		}else if(configNnode != null && nnode == configNnode && e != null && e.isShiftDown()) {
			this.createLinkStage();
		}else {
			this.closeActivity();
			configNnode = nnode;
			configNnode.styleOrange();
		}
	}

	public void closeActivity() {
		if(configNnode != null) {
			configNnode.styleGray();
			configNnode = null;
		}
	}

	public void clearSelection() {
		if(configNnode != null) configNnode.styleGray();
		configNnode = null;		
	}
	
	public Nnode getConfigNnode() {
		return configNnode;
	}

	public void rebuildFieldMenu() {
//		if(configNnode != null) {
//			this.buildPreJoinMenu(configNnode);
//		}
	}
	
//	public void buildPreJoinMenu(Nnode nnode2){
//		ArrayList<String> foreignColumnsStrings = new ArrayList<String>();
//		XMLBase base = nnode2.nmap.napp.getDBManager().getActiveConnection().getXMLBase();
//		//CURRENT JOINS (KEYS) MENU
//		base.getKeys().filtered(k -> k.getSchema().equals(nnode2.getSchema())
//				&& k.getConst().equals("FOREIGN KEY")		
//				&& k.getTable().equals(nnode2.getTable())
//				).forEach(key -> {
//					Menu columnMenu = new Menu("• "+ key.getColumn());
////					joinMenu.getItems().add(columnMenu);					
//					nFile.getFileManager().napp.funcContext.getItems().add(columnMenu);
//					Menu refSchemaMenu = new Menu(key.getRSchema());//NEW SCHEMA
//					Menu refTableMenu = new Menu(key.getRTable());
//					MenuItem refColumnMenu = new MenuItem(key.getRColumn());
//					refColumnMenu.setOnAction(e -> {
//						 //Remove
//						 ButtonType unJoinBtn = new ButtonType("Delete");
//						 ButtonType cancel = new ButtonType("Cancel");
//						 Alert alert = new Alert(AlertType.NONE, "Un-Link", unJoinBtn, cancel);
//						 alert.setTitle("Un-Link " + key.getTable() + " from " + key.getRTable());
////							 alert.setHeaderText("Separate child form parent.");
////							 alert.setContentText("Un-Join " + key.getTable_name() + ".[" + key.getColumn_name() + "] via " + key.getReferenced_table_name() + ".[" + key.getReferenced_column_name()+ "]");
//						 alert.showAndWait().ifPresent(alertReturn -> {
//						     if (alertReturn == unJoinBtn) {
////						    	 base.getNkeyDAO().deleteRecord(key);
//						    	 base.getKeys().remove(key);//don't like this design
//						    	 if(key.getRSchema().equals(nnode2.getSchema())) {
//						    		 //TEMPORARY DISABLED, NEED TO RESTART APP
////						    		 Nnode nnode = this.nmap.getMapNodes().get(key.getReferenced_table_name());//								mapNodes.put(nnode.getTableName(), nnode);
////							    	 Nline line = this.rootLines.get(nnode);
////							    	 this.rootLines.remove(nnode);
////							    	 nnode.rootLines.remove(this);
////							    	 nmap.remove(line);
////							    	 joins.removefromLocalJoins(nnode, key);
//						    	 }
//						     } 
//						 });
//						 e.consume();
//					});
//					refTableMenu.getItems().add(refColumnMenu);
//					refSchemaMenu.getItems().add(refTableMenu);
//					columnMenu.getItems().add(refSchemaMenu);
////					joinMenu.getItems().add(columnMenu);					
//					foreignColumnsStrings.add(key.getColumn());
//			});
//
//		//POSSIBLE KEYS
//		base.getXColumns().filtered(fcol -> fcol.getSchema().equals(nnode2.getSchema())  && fcol.getTable().equals(nnode2.getTable())).forEach((column) -> {
////			 if( !foreignColumnsStrings.contains(column.getColumn())){
//				 Menu columnA = new Menu(" " + column.getColumn());
////				 joinMenu.getItems().add(columnA);
//				nFile.getFileManager().napp.funcContext.getItems().add(columnA);
//
//				 base.getSchemas().forEach(sch -> {
//						 Menu schemaM = new Menu(sch);
//						 columnA.getItems().add(schemaM);
//						 base.getXTables().filtered(ft -> ft.getSchema().equals(sch)).forEach((table) -> {//this need to go to bos
//							 if(table.getTable() != nnode2.getTable()){
//								 Menu tableM = new Menu(table.getTable());
//								 
//								 schemaM.getItems().add(tableM);
//								 base.getXColumns().filtered(fcol -> fcol.getSchema().equals(sch)  && fcol.getTable().equals(table.getTable())).forEach(refColumn ->{
//									 MenuItem columnMb = new MenuItem(refColumn.getColumn());
//									 tableM.getItems().add(columnMb);
//									 columnMb.setOnAction(e -> {
//										 
//										 NKey key = new NKey();
//										key.setSchema(nnode2.getSchema());
//										key.setTable(nnode2.getTable());
//										key.setColumn(column.getColumn());
//										key.setRSchema(refColumn.getSchema());
//										key.setRTable(refColumn.getTable());
//										key.setRColumn(refColumn.getColumn());
//										key.setConst("FOREIGN KEY");
//										
//										 ButtonType confirm = new ButtonType("Confirm");
//										 ButtonType cancel = new ButtonType("Cancel");
//										 Alert alert = new Alert(AlertType.NONE, "Link", confirm, cancel);
//										 alert.setTitle("Link" + key.getTable() + " to " + key.getRTable());
//										 alert.showAndWait().ifPresent(alertReturn -> {
//										     if (alertReturn == confirm) {
//										    	 base.getKeys().add(key);//don't like this design
//										    	 if(key.getRSchema().equals(nnode2.getSchema())) {//visual link only for local schema
//										    		Nnode nnode = nnode2.nmap.getNnode(key.getRTable());
//										    		if(!nnode2.getRootLines().containsKey(nnode) && !nnode.getRootLines().containsKey(nnode2)) {
//										    			NnodeLine line = new NnodeLine(nnode2, nnode);
//														nnode2.getRootLines().put(nnode, line);
//														nnode.getRootLines().put(nnode2, line);
//														nnode2.nmap.add(line);
//														line.toBack();
//													}
//										    	 }else {
//										    		 //ADD EXTERNAL visual  JOIN HERE or not needed ???
//										    	 }
//										     }
//										 });
//										 e.consume();
//									 });
//								 }); 
//							 }
//						 });
//					});	
////			 	}
//		 });
//	}
	

	private void createLinkStage(Nnode nnode) {
		PopUpStage inMenu = new PopUpStage(nFile.getFileManager().napp, nFile.getFileManager().napp.getUpperPane().getPlaceHolder());
		ListView<NLink> listViewKeyMap = new ListView<NLink>();
		ListView<String> listViewA = new ListView<String>();
		ListView<String> listViewB = new ListView<String>();
		
		listViewKeyMap.setMaxHeight(200);		
		listViewKeyMap.setCellFactory(param -> new NKeyCell());

		listViewA.setMaxHeight(200);
		listViewB.setMaxHeight(200);		
		HBox.setHgrow(listViewKeyMap, Priority.ALWAYS);

		XMLBase base = configNnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase();
		base.getKeys().filtered(k -> k.getSchema().equals(configNnode.getSchema())
				&& k.getConst().equals("FOREIGN KEY")		
				&& k.getTable().equals(configNnode.getTable())
				).forEach(key -> {
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
				});
		
		configNnode.getColumns().forEach( s->{
			listViewA.getItems().add(s.getColumn());
		});
		
		nnode.getColumns().forEach( s->{
			listViewB.getItems().add(s.getColumn());
		});
		
		listViewB.setOnMouseClicked(ee -> {
			if (ee.isShiftDown()) {
				if(listViewA.getSelectionModel().getSelectedItems().size() == 1) {
					NKey key = new NKey();
					key.setSchema(configNnode.getSchema());
					key.setTable(configNnode.getTable());
					key.setColumn(listViewA.getSelectionModel().getSelectedItem());
					key.setRSchema(nnode.getSchema());
					key.setRTable(nnode.getTable());
					key.setRColumn(listViewB.getSelectionModel().getSelectedItem());
					key.setConst("FOREIGN KEY");
					String st = key.getTable() + "." + key.getColumn() + " = " + key.getRTable() + "."+ key.getRColumn();
							
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
					System.out.println("NEW KEY: " + st);
					    	 base.getKeys().add(key);//don't like this design
					    	 if(nnode.getSchema().equals(configNnode.getSchema())) {//visual link only for local schema
					    		if(!configNnode.getRootLines().containsKey(nnode) && !nnode.getRootLines().containsKey(configNnode)) {
					    			NnodeLine line = new NnodeLine(configNnode, nnode);
					    			configNnode.getRootLines().put(nnode, line);
									nnode.getRootLines().put(configNnode, line);
									configNnode.nmap.add(line);
									line.toBack();
								}
					    	 }
					 ee.consume();
				}
			}
		});
		
		VBox vboxa = new VBox(5,new Label(configNnode.getTable()), listViewA);
		VBox vboxb = new VBox(5,new Label(nnode.getTable()), listViewB);		
		HBox.setHgrow(vboxa, Priority.ALWAYS);
		HBox.setHgrow(vboxb, Priority.ALWAYS);
		HBox hbox = new HBox(5,vboxa, vboxb);
		inMenu.add(new Label(configNnode.getTable() + " connections"));
		inMenu.add(listViewKeyMap);
		inMenu.add(hbox);
		inMenu.showSearchStage();
		
	}
	
	private void createLinkStage() {
		PopUpStage inMenu = new PopUpStage(nFile.getFileManager().napp, nFile.getFileManager().napp.getUpperPane().getPlaceHolder());
		ListView<NLink> listViewKeyMap = new ListView<NLink>();
		ListView<String> listViewA = new ListView<String>();		
		listViewKeyMap.setMaxHeight(200);		
		listViewKeyMap.setCellFactory(param -> new NKeyCell());
		listViewA.setMaxHeight(200);
		HBox.setHgrow(listViewKeyMap, Priority.ALWAYS);

		XMLBase base = configNnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase();
		base.getKeys().filtered(k -> k.getSchema().equals(configNnode.getSchema())
				&& k.getConst().equals("FOREIGN KEY")		
				&& k.getTable().equals(configNnode.getTable())
				).forEach(key -> {
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
				});
		
		configNnode.getColumns().forEach( s->{
			listViewA.getItems().add(s.getColumn());
		});
		
		
		VBox vboxa = new VBox(5,new Label(configNnode.getTable()), listViewA);
		HBox.setHgrow(vboxa, Priority.ALWAYS);
		HBox hbox = new HBox(5,vboxa);
		inMenu.add(new Label(configNnode.getTable() + " joins"));
		inMenu.add(listViewKeyMap);
		inMenu.add(hbox);
		inMenu.showSearchStage();		
	}

}

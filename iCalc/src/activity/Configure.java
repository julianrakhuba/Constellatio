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
	private Nnode activeNnode;
	public void rebuildFieldMenu() {}

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
		if(activeNnode != null && nnode != activeNnode && e != null && e.isShiftDown()) {
			this.createLinkStage(nnode);
		}else if(activeNnode != null && nnode == activeNnode && e != null && e.isShiftDown()) {
			this.createLinkStage();
		}else {
			this.closeActivity();
			activeNnode = nnode;
			activeNnode.styleOrange();
			activeNnode.getGreenNeon().show();
		}
	}

	public void closeActivity() {
		if(activeNnode != null) {
			activeNnode.styleGray();
			activeNnode.getGreenNeon().hide();
			activeNnode = null;
		}
	}

	public void clearSelection() {
		if(activeNnode != null) {
			activeNnode.styleGray();
			activeNnode.getGreenNeon().hide();
		}
		activeNnode = null;		
	}
	
	public Nnode getActiveNnode() {
		return activeNnode;
	}

	

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

		XMLBase base = activeNnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase();
		base.getKeys().filtered(k -> k.getSchema().equals(activeNnode.getSchema())
				&& k.getConst().equals("FOREIGN KEY")		
				&& k.getTable().equals(activeNnode.getTable())
				).forEach(key -> {
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
				});
		
		activeNnode.getColumns().forEach( s->{
			listViewA.getItems().add(s.getColumn());
		});
		
		nnode.getColumns().forEach( s->{
			listViewB.getItems().add(s.getColumn());
		});
		
		listViewB.setOnMouseClicked(ee -> {
			if (ee.isShiftDown()) {
				if(listViewA.getSelectionModel().getSelectedItems().size() == 1) {
					NKey key = new NKey();
					key.setSchema(activeNnode.getSchema());
					key.setTable(activeNnode.getTable());
					key.setColumn(listViewA.getSelectionModel().getSelectedItem());
					key.setRSchema(nnode.getSchema());
					key.setRTable(nnode.getTable());
					key.setRColumn(listViewB.getSelectionModel().getSelectedItem());
					key.setConst("FOREIGN KEY");
							
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
					    	 base.getKeys().add(key);//don't like this design
					    	 if(nnode.getSchema().equals(activeNnode.getSchema())) {//visual link only for local schema
					    		if(!activeNnode.getRootLines().containsKey(nnode) && !nnode.getRootLines().containsKey(activeNnode)) {
					    			NnodeLine line = new NnodeLine(activeNnode, nnode);
					    			activeNnode.getRootLines().put(nnode, line);
									nnode.getRootLines().put(activeNnode, line);
									activeNnode.nmap.add(line);
									line.toBack();
								}
					    	 }
					 ee.consume();
				}
			}
		});
		
		VBox vboxa = new VBox(5,new Label(activeNnode.getTable()), listViewA);
		VBox vboxb = new VBox(5,new Label(nnode.getTable()), listViewB);		
		HBox.setHgrow(vboxa, Priority.ALWAYS);
		HBox.setHgrow(vboxb, Priority.ALWAYS);
		HBox hbox = new HBox(5,vboxa, vboxb);
		inMenu.add(new Label(activeNnode.getTable() + " connections"));
		inMenu.add(listViewKeyMap);
		inMenu.add(hbox);
		inMenu.showPopUp();
		
	}
	
	private void createLinkStage() {
		PopUpStage inMenu = new PopUpStage(nFile.getFileManager().napp, nFile.getFileManager().napp.getUpperPane().getPlaceHolder());
		ListView<NLink> listViewKeyMap = new ListView<NLink>();
		ListView<String> listViewA = new ListView<String>();		
		listViewKeyMap.setMaxHeight(200);		
		listViewKeyMap.setCellFactory(param -> new NKeyCell());
		listViewA.setMaxHeight(200);
		HBox.setHgrow(listViewKeyMap, Priority.ALWAYS);

		XMLBase base = activeNnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase();
		base.getKeys().filtered(k -> k.getSchema().equals(activeNnode.getSchema())
				&& k.getConst().equals("FOREIGN KEY")		
				&& k.getTable().equals(activeNnode.getTable())
				).forEach(key -> {
					listViewKeyMap.getItems().add(new NLink(key, Selector.SELECTED, listViewKeyMap, base));
				});
		
		activeNnode.getColumns().forEach( s->{
			listViewA.getItems().add(s.getColumn());
		});
		
		
		VBox vboxa = new VBox(5,new Label(activeNnode.getTable()), listViewA);
		HBox.setHgrow(vboxa, Priority.ALWAYS);
		HBox hbox = new HBox(5,vboxa);
		inMenu.add(new Label(activeNnode.getTable() + " joins"));
		inMenu.add(listViewKeyMap);
		inMenu.add(hbox);
		inMenu.showPopUp();		
	}

}

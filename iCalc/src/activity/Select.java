package activity;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

import application.JoinLine;
import application.Nnode;
import file.NFile;
import generic.ACT;
import generic.DLayer;
import generic.LAY;
import generic.SLayer;
import javafx.scene.input.MouseEvent;
import logic.Field;
import logic.Join;
import logic.SearchCON;
import search.PAIR;
import status.ActivityMode;
import status.JoinType;
import status.LayerMode;
import status.Population;
import status.Selection;
import status.Selector;
import status.SqlType;
import status.VisualStatus;

public class Select extends ACT {
	private boolean modefied = false;

	public Select(NFile nFile) {
		this.nFile = nFile;
	}

	public void passLAY(LAY lay) {		
		if (nFile.getFileManager().getNapp().getNscene().getHoldKeys().contains("ALT") && lay.isRoot()) {
			this.closeActivity();
			nFile.setActivityMode(ActivityMode.FORMULA);
			nFile.getActivity().passLAY(lay);
		}else {
			if (rootLay == null) {
				rootLay = lay;
				if (nFile.getFileManager().getNapp().getNscene().getHoldKeys().contains("CONTROL")) {
					this.populate(lay);
				}	
				this.activate(lay);
				rootLay.showExternalLinks();
			} else if (rootLay == lay) {
				if (nFile.getFileManager().getNapp().getNscene().getHoldKeys().contains("CONTROL")) {
					this.populate(lay);
					this.activate(lay);					
				}else {
					this.closeActivity();
					nFile.setActivityMode(ActivityMode.EDIT);
					nFile.getActivity().passLAY(lay);
				}
			} else if (rootLay != lay) {
				this.closeActivity();
				this.passLAY(lay);
			}
		}
		if(rootLay != null) {
			nFile.getSidePane().activateSearch(rootLay);
		}
	}
	
	public void activate(LAY lay) {
		lay.setSelection(Selection.SELECTED);
		if (lay.getPopulation().getValue() == Population.POPULATED) {
			nFile.getTabManager().selectTab(lay.getSheet());
			if(nFile.getTabManager().getStatus() == VisualStatus.UNAVALIBLE) nFile.getTabManager().showGrid();
		}		
	}

	private void populate(LAY lay) {
		if(lay.isRoot() || lay.getSqlType() == SqlType.SQL) {			
			if(lay instanceof DLayer) ((DLayer)lay).rebuildDFieldsAndJoins();
			lay.populate();
		}
	}
	
	public void passNnode(Nnode nnode, MouseEvent e) {
		Instant start = Instant.now();
		if(nnode.isSelectable()) {			
			HashSet<String> keyboard = nFile.getFileManager().getNapp().getNscene().getHoldKeys();
			if (rootLay != null  && !keyboard.contains("D") && rootLay.getRelatedJoins(nnode).size()>0) {
				//ADD LOGIC TO CHECK IF RELATED
				nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().setSqlType(rootLay.getSqlType());
				LAY newLAY = new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
				newLAY.addToMap();
				LAY previousLAY = rootLay;
				this.closeActivity();			
				nFile.setActivityMode(ActivityMode.EDIT);

				if(previousLAY.getRelatedJoins(newLAY).size() == 1) {
					((Edit)nFile.getActivity()).enterEdit(newLAY, true);
					((Edit)nFile.getActivity()).autoJoinLayViaFirstJoin(previousLAY, true);
					nFile.getActivity().closeActivity();//Close EDIT
					nFile.getActivity().passLAY(newLAY);//Pass to SELECT
				}else {
					nFile.getActivity().passLAY(newLAY);
					nFile.getActivity().passLAY(previousLAY);// pass to join with previous layer
				}
				
				nFile.getUndoManager().saveUndoAction();
			} else if (keyboard.contains("SHIFT")) {
				if(keyboard.contains("D")) {
					this.createDLayer(nnode);
				}else {
					
				this.createSLayer(nnode);
				}
			}
		}
		
		// END CODE TIMER HERE        
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toSeconds();
		boolean print = false;
		if(print)System.out.println("Select.passNnode() seconds: " + timeElapsed);
	}
	
	private void createDLayer(Nnode nnode) {
		if(rootLay != null && rootLay.getNnode() == nnode  && rootLay.getChildDLayer() == null && (rootLay.isRoot() && rootLay.getSelectedFields().size()>0)) {					
			DLayer dLAY = new DLayer(rootLay.getNnode(), rootLay); //Create derived LAY
			dLAY.rebuildDFieldsAndJoins();
			dLAY.addToMap();
			//Line-------
			JoinLine joinLine = new JoinLine(dLAY, rootLay, JoinType.DLINE);
			dLAY.setJoinLine(joinLine);
			int index = rootLay.getNnode().getNmap().getSchemaPane().getChildren().indexOf(dLAY.getPane());
			int index2 = rootLay.getNnode().getNmap().getSchemaPane().getChildren().indexOf(rootLay.getPane());
			rootLay.getNnode().getNmap().getSchemaPane().getChildren().add(Math.min(index, index2) -1, joinLine.getCubicCurve());
			//----------
			nFile.getActivity().passLAY(dLAY);
			nFile.getUndoManager().saveUndoAction();			
		}
	}
	
	public void createSLayer(Nnode nnode) {
		LAY lay = new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
		lay.addToMap();
		nFile.getActivity().passLAY(lay);
		nFile.getUndoManager().saveUndoAction();
//		nnode.nmap.napp.getConsole().addTextToQue(new SQL().append("• ").SELECT().append(" * ").FROM(lay).toString() + "\n");
	}
	

	public void closeActivity() {
		if (rootLay != null) {
			rootLay.setSelection(Selection.UNSELECTED);
			rootLay.setMode(LayerMode.BASE);
			rootLay.hideExternalLinks();
			rootLay = null;
		}
		if (modefied) {
			nFile.getUndoManager().saveUndoAction();
			modefied = false;
		}
	}

	public void rebuildFieldMenu() {
		nFile.getFileManager().getNapp().getUpperPane().getSearchContext().getItems().addAll(nFile.getFileManager().getNapp().getUpperPane().getSearchTextField().getMenuItems());
	}
	
	private void finishNewLayer(LAY lay, SearchCON con) {
		lay.addToMap();
		lay.getRootLevel().getDynamicGroup().add(con);
		lay.addSearchCONtoSearchList(con);
		nFile.getSidePane().activateSearch(lay);
		lay.refreshFilterIndicator();
		con.getRoot().setSelected(Selector.UNSELECTED);
		modefied = true;
		
		//CONSOLE ONLY
//		lay.nnode.nmap.napp.getConsole().addTextToQue(new SQL().append("• ").SELECT().append(" * ").FROM(lay).toString() + "\n");
		//••••••••••••••

		this.closeActivity();
		if (nFile.getFileManager().getNapp().getNscene().getHoldKeys().contains("ALT")) {//why I have this??
			nFile.setActivityMode(ActivityMode.EDIT);
			nFile.getActivity().passLAY(lay);
		}else {
			nFile.getActivity().passLAY(lay);
		}
		
	}
	
	public void newSearchFUNCTION(Nnode nnode, String col, PAIR funcVAL) {
		if(nnode.isSelectable()) {
			LAY lay =  new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
			Field field = lay.getFieldOrFunction(lay.getAliase() + "_" + col);
			SearchCON con = new SearchCON(lay);		
			con.autoSearchFunc(field, funcVAL);
			this.finishNewLayer(lay, con);
		}
	}
	
	public void newSearchBETWEEN(Nnode nnode, String col, String from, String to) {
		if(nnode.isSelectable()) {
			LAY lay =  new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
			Field field = lay.getFieldOrFunction(lay.getAliase() + "_" + col);
			SearchCON con = new SearchCON(lay);
			con.autoBetween(field, from, to);	
			this.finishNewLayer(lay, con);
		}
	}

	public void newSearchIN(Nnode nnode, String col, String in, ArrayList<String> values) {
		if(nnode.isSelectable()) {
			LAY lay =  new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
			Field field = lay.getFieldOrFunction(lay.getAliase() + "_" + col);
			SearchCON con = new SearchCON(lay);
			con.autoIn(field, in, values);
			this.finishNewLayer(lay, con);
		}
	}

	public void passExternalJoin(Join jn) {
		if(nFile.getFileManager().getNapp().getNscene().getHoldKeys().contains("SHIFT")) {
			nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().setSqlType(rootLay.getSqlType());
			this.closeActivity();
			if (!nFile.getMaps().containsKey(jn.getRemoteSchema())) {
				nFile.createNewMap(jn.getRemoteSchema());
			} else if (!jn.isLocal_by_Derived()) {
				nFile.showNmap(jn.getRemoteSchema());
			}
			Nnode nnode = nFile.getMaps().get(jn.getRemoteSchema()).getNnode(jn.getRemoteTable());
			if(nnode.isSelectable()) {
				LAY lay = new SLayer(nnode, nFile.getFileManager().getNapp().getUpperPane().getFunctionsButton().getSqlType());
				lay.addToMap();
				nFile.setActivityMode(ActivityMode.EDIT);
				nFile.getActivity().passLAY(lay);
				nFile.getActivity().passLAY(jn.getLay());// pass to join with previous layer				

				if(jn.getLay().getRelatedJoins(lay).size() == 1) {
					nFile.getActivity().closeActivity();
					nFile.getActivity().passLAY(lay);
				}
				nFile.getUndoManager().saveUndoAction();
			}
		}else {
			if (nFile.getMaps().containsKey(jn.getRemoteSchema())) {
				nFile.showNmap(jn.getRemoteSchema());
			} 
		}
	}
}
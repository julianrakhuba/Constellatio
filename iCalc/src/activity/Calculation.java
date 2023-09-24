package activity;

import java.util.ArrayList;

import application.Nnode;
import clientcomponents.NFunction;
import file.NFile;
import generic.ACT;
import generic.DLayer;
import generic.LAY;
import generic.SLayer;
import javafx.collections.ObservableList;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import logic.FormulaField;
import pivot.LayerMenu;
import search.PAIR;
import sidePanel.Message;
import status.ActivityMode;
import status.LayerMode;
import status.Selection;
import status.SqlType;
import status.Status;

public class Calculation extends ACT {
	private boolean modefied = false;
	private FormulaField activeField;
	
	public void newSearchFUNCTION(Nnode nnod, String col,PAIR funcVAL) {}
	public void newSearchBETWEEN(Nnode nnod, String col, String from, String to) {}
	public void newSearchIN(Nnode nnod, String col, String in, ArrayList<String> values) {}
	public void passNnode(Nnode nnode, MouseEvent e) {}
	
	public Calculation(NFile nFile) {
		this.nFile = nFile;
	}
	
	public void passLAY(LAY lay) {
		if (rootLay == null) {
			rootLay = lay;
			rootLay.setMode(LayerMode.FORMULA);
//			if(lay.getFormulaFields().size()>0) this.activateField(lay.getFormulaFields().get(0));	//BETER REMOVE	Autoselect first formula	
			nFile.getSidePane().activateFormula(rootLay);
		}
	}

	public void closeActivity() {
		this.deactivateField(activeField);
//		rootLay.nnode.nmap.napp.getUpperPane().setRegularSearch();
		rootLay.setSelection(Selection.UNSELECTED);
		rootLay.setMode(LayerMode.BASE);
		nFile.setActivityMode(ActivityMode.SELECT);		
		if (modefied) {
			nFile.getUndoManager().saveUndoAction();
			nFile.getFileManager().getNapp().getConsole().refreshActiveMonotor();
			modefied = false;
		}
		rootLay = null;
	}
	
	//
	public void rebuildFieldMenu() {
		//rebuild formula menu here
		Label pivotLabel = new Label("functions");
		pivotLabel.setStyle("-fx-font-weight: bold;");
		Menu functionsMenu = new Menu();
		functionsMenu.setGraphic(pivotLabel);
		rootLay.getNnode().getNmap().getNapp().getUpperPane().getSearchContext().getItems().addAll(functionsMenu,  new SeparatorMenuItem());
			Label label = new Label("string");
			label.setPrefWidth(100);
			CustomMenuItem mnI = new CustomMenuItem(label, true);
			functionsMenu.getItems().addAll(mnI, new SeparatorMenuItem());
			mnI.setOnAction(e ->{
				if(activeField == null) this.createNewCustomField();
				activeField.createStringELM("", true);
				modefied = true;
			});			
		
		ObservableList<NFunction> functions = nFile.getFileManager().getNapp().getDBManager().getActiveConnection().getXMLBase().getXFunctions();	
		functions.forEach(nf -> {
			if(nf.getType().equals("AGRIGATE")) {
				CustomMenuItem menuItem = new CustomMenuItem(new Label(nf.getLabel()),false);
				functionsMenu.getItems().add(menuItem);
				menuItem.setOnAction(e ->{
					if(activeField == null) this.createNewCustomField();		
					activeField.createFunctionELM(nf.getRealname(), nf.getLabel(), nf.getOpen(), nf.getOpenParam(), nf.getCloseParam(), nf.getClose());
					modefied = true;
				});
			}
		});
		
		functionsMenu.getItems().add(new SeparatorMenuItem());
		functions.forEach(nf -> {
			if(nf.getType().equals("GROUP")) {
				CustomMenuItem menuItem = new CustomMenuItem(new Label(nf.getLabel()),false);
				functionsMenu.getItems().add(menuItem);
				menuItem.setOnAction(e ->{
					if(activeField == null) this.createNewCustomField();		
					activeField.createFunctionELM(nf.getRealname(), nf.getLabel(), nf.getOpen(), nf.getOpenParam(), nf.getCloseParam(), nf.getClose());
					modefied = true;
				});
			}
		});
		
		ArrayList<LAY> allLays = rootLay.getFullChildren();
		allLays.add(0, rootLay);
		allLays.forEach(joinLay -> {
			if((joinLay instanceof SLayer && joinLay.getSqlType() == SqlType.SQLJ) || joinLay instanceof DLayer) {
				LayerMenu menu = new LayerMenu(rootLay, joinLay);
				 joinLay.getFields().forEach(field ->{
					CustomMenuItem menuItem = new CustomMenuItem(new Label(field.getLabelText()), false);
			        menuItem.setOnAction(je ->{
						if(activeField == null) this.createNewCustomField();		
						activeField.createFieldELM(field);
						modefied = true;
			        });
			        menu.getItems().add(menuItem);
				 });
				 rootLay.getNnode().getNmap().getNapp().getUpperPane().getSearchContext().getItems().add(menu);
			}			
		});
	}
	
	private void createNewCustomField() {
		int index = 0;
		String tmpAliase = rootLay.getAliase()  + "_formula";
		while (rootLay.containsFormulaField(tmpAliase + index)) { index = index + 1; }
		FormulaField formulaField = new FormulaField(rootLay);
		formulaField.setRowset_type("String");
 		formulaField.setAliase(tmpAliase + index);
		rootLay.addFormulaField(formulaField);
		this.activateField(formulaField);	
	}
	
	// get list for all set aliase and get field alise
	public void activateClick(FormulaField customField, MouseEvent e) {
		if(e.isControlDown()) {
			if(!customField.isSelected()) {
				this.deactivateField(customField);
				customField.getFieldLay().removeField(customField.getAliase());
			}else {
				nFile.getMessages().add(new Message(nFile, "Selected Field", "Can't Delete Selected FormulaField " + customField.getLabelText()));
			}
		}else {
			if(customField.getRoot().getStatus() == Status.UNACTIVE) {
				this.activateField(customField);
			}else {
				this.deactivateField(customField);
			}
		}			
	}
	
	public void activateField(FormulaField field) {
		if(activeField != field && activeField != null) {
			this.deactivateField(activeField);
		}
		field.getRoot().setStatus(Status.ACTIVE);
		activeField = field;
		field.getFieldLay().getNnode().getNmap().getNapp().getUpperPane().setFormulaSearch(field.getCursorBox());
		field.getCursorBox().requestFocus();
	}

	public void deactivateField(FormulaField field) {
		if(activeField == field && field != null) {
			field.getRoot().setStatus(Status.UNACTIVE);
			activeField = null;
		}
		nFile.getFileManager().getNapp().getUpperPane().setRegularSearch();
	}
}
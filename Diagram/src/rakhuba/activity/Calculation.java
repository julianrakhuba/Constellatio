package rakhuba.activity;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import rakhuba.application.Nnode;
import rakhuba.builder.base.clientcomponents.NFunction;
import rakhuba.file.NFile;
import rakhuba.generic.ACT;
import rakhuba.generic.DLayer;
import rakhuba.generic.LAY;
import rakhuba.generic.SLayer;
import rakhuba.logic.FormulaField;
import rakhuba.pivot.LayerMenu;
import rakhuba.search.PAIR;
import rakhuba.sidePanel.Message;
import rakhuba.status.ActivityMode;
import rakhuba.status.LayerMode;
import rakhuba.status.Selection;
import rakhuba.status.SqlType;
import rakhuba.status.Status;

public class Calculation extends ACT {
	private boolean modefied = false;
	private HBox emptyPlaceHodler = new HBox();
	private FormulaField activeField;
	public void newSearchFUNCTION(Nnode nnod, String col,PAIR funcVAL) {}
	public void newSearchBETWEEN(Nnode nnod, String col, String from, String to) {}
	public void newSearchIN(Nnode nnod, String col, String in, ArrayList<String> values) {}
	public void passNnode(Nnode nnode, MouseEvent e) {}

	public Calculation(NFile nFile) {
		this.nFile = nFile;
		//move to css
		emptyPlaceHodler.setMinHeight(30);
		emptyPlaceHodler.prefWidthProperty().bind(nFile.getFileManager().napp.searchHBox.widthProperty().divide(1.75));
		
		emptyPlaceHodler.setAlignment(Pos.CENTER);
		emptyPlaceHodler.setStyle("-fx-background-color: white; -fx-border-width: 1 ;-fx-border-color: #b9baba;  -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;");
		Label label = new Label("new formula");
		label.setTextFill(Color.GRAY);
		emptyPlaceHodler.getChildren().add(label);
	}


	
	public void passLAY(LAY lay) {
		if (rootLay == null) {
			rootLay = lay;
			rootLay.setMode(LayerMode.FORMULA);
			rootLay.nnode.nmap.napp.setFormulaSearch(emptyPlaceHodler);
//			if(lay.getFormulaFields().size()>0) this.activateField(lay.getFormulaFields().get(0));	//BETER REMOVE	Autoselect first formula	
			nFile.getSidePaneManager().activateFormula(rootLay);
		}
	}

	public void closeActivity() {
		this.deactivateField(activeField);
		rootLay.nnode.nmap.napp.setRegularSearch();
//		LAY returnLAY = rootLay;
		rootLay.setSelection(Selection.UNSELECTED);
		rootLay.setMode(LayerMode.BASE);
		nFile.setActivityMode(ActivityMode.SELECT);		
		if (modefied) {
			nFile.getUndoManager().saveUndoAction();
			modefied = false;
		}
		rootLay = null;
//		return returnLAY;
	}
	
	//
	public void rebuildFieldMenu() {
		//rebuild formula menu here
		Label pivotLabel = new Label("functions");
		pivotLabel.setStyle("-fx-font-weight: bold;");
		Menu functionsMenu = new Menu();
		functionsMenu.setGraphic(pivotLabel);
		rootLay.nnode.nmap.napp.funcContext.getItems().addAll(functionsMenu,  new SeparatorMenuItem());
			Label label = new Label("string");
			label.setPrefWidth(100);
			CustomMenuItem mnI = new CustomMenuItem(label, true);
			functionsMenu.getItems().addAll(mnI, new SeparatorMenuItem());
			mnI.setOnAction(e ->{
				if(activeField == null) this.createNewCustomField();
				activeField.createStringELM("", true);
				modefied = true;
			});			
		
		ObservableList<NFunction> functions = nFile.getFileManager().napp.getDBManager().getActiveConnection().getXMLBase().getXFunctions();	
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
					CustomMenuItem menuItem = new CustomMenuItem(new Label(field.getText()), false);
			        menuItem.setOnAction(je ->{
						if(activeField == null) this.createNewCustomField();		
						activeField.createFieldELM(field);
						modefied = true;
			        });
			        menu.getItems().add(menuItem);
				 });
				 rootLay.nnode.nmap.napp.funcContext.getItems().add(menu);
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
	public void activateClick(FormulaField customField) {
		if(nFile.getFileManager().napp.getNscene().getHoldKeys().contains("CONTROL")) {
			if(!customField.isSelected()) {
				this.deactivateField(customField);
				customField.getFieldLay().removeField(customField.getAliase());
			}else {
				nFile.getMessages().add(new Message(nFile, "Selected Field", "Can't Delete Selected FormulaField " + customField.getText()));
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
		field.getFieldLay().nnode.nmap.napp.setFormulaSearch(field.getFormulaHBox());
		field.getFormulaHBox().requestFocus();
	}

	public void deactivateField(FormulaField field) {
		if(activeField == field && field != null) {			
			field.getFieldLay().nnode.nmap.napp.setFormulaSearch(emptyPlaceHodler);
			field.getRoot().setStatus(Status.UNACTIVE);
			activeField = null;
		}
	}
}
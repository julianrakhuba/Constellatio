package elements;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import activity.Edit;
import application.Constellatio;
import file.OpenContext;
import generic.ACT;
import generic.LAY;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import logic.Field;
import logic.FormulaField;
import logic.SearchCON;
//import rakhuba.elements.Cursor;
//import rakhuba.elements.CursorBox;
//import rakhuba.elements.ELM;
//import rakhuba.elements.SideLabel;
import status.Selector;
import status.Status;

public class RootELM extends ELM{
	
	private SearchCON searchCON;
	private FormulaField formula;
	private SideLabel sideLabel = new SideLabel();
	private Property<Status> status = new SimpleObjectProperty<Status>(Status.UNACTIVE);
	private Property<Selector> selected = new SimpleObjectProperty<Selector>(Selector.UNSELECTED);
	
	private String focusedCursorBox =   "-fx-padding: 0 2 0 2; -fx-spacing: 2;  -fx-alignment:CENTER;  -fx-min-height: 30; -fx-effect: innershadow(three-pass-box, #99ddff, 4, 0.5, 0, 0); -fx-background-color: white; -fx-text-fill: #708090; -fx-border-width: 1 ;-fx-border-color: #b9baba; -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;" ;
	private String unfocusedCursorBox = "-fx-padding: 0 2 0 2; -fx-spacing: 2;  -fx-alignment:CENTER;  -fx-min-height: 30; -fx-effect: innershadow(three-pass-box, #cbcccd, 4, 0.5, 0, 0); -fx-background-color: white; -fx-text-fill: #708090; -fx-border-width: 1 ;-fx-border-color: #b9baba; -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;" ;
	
	public RootELM(SearchCON searchCON, Constellatio app) {
		this();
		this.searchCON = searchCON;
		cursorBox.prefWidthProperty().bind(app.getUpperPane().getOverlapBox().widthProperty().divide(1.75));
		sideLabel.setOnMouseClicked(e ->  {
			ACT act = searchCON.getLay().nnode.nmap.getNFile().getActivity();
			if(act instanceof Edit && act.getActiveLayer() == searchCON.getLay()) {
				((Edit)act).conditionActivateClick(searchCON);
			}
		});
		sideLabel.getPane().setOnMouseClicked(e -> {
			ACT act = searchCON.getLay().nnode.nmap.getNFile().getActivity();
			if(act instanceof Edit && act.getActiveLayer() == searchCON.getLay()) {
				((Edit)act).conditionClick(searchCON);
			}
			e.consume();
		});
		selected.addListener((a,b,c)-> this.updateStyle());
		this.updateStyle();
	}
	
	public RootELM(FormulaField formula, Constellatio app) {
		this();
		this.formula = formula;
		cursorBox.prefWidthProperty().bind(app.getUpperPane().getOverlapBox().widthProperty().divide(1.75));
		sideLabel.styleUnselected();
		sideLabel.setOnMouseClicked(e ->  formula.activeClick(e));

	}
	
	public RootELM() {
		cursorBox = new CursorBox(this);
		cursorBox.getChildren().add(new Cursor(this));
		cursorBox.setStyle(unfocusedCursorBox);		
		this.getElements().addListener((ListChangeListener<? super ELM>) a -> this.refreshSideLableText());	
	}
	
	public LAY getLay() {
		if(searchCON !=null) {
			return searchCON.getLay();
		}else {
			return formula.getFieldLay();
		}
	}
	
	public void styleFocused() {
		cursorBox.setStyle(focusedCursorBox);//local
	}

	public void styleUnfocused() {
		cursorBox.setStyle(unfocusedCursorBox);
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public SideLabel getLabel() {
		return sideLabel;
	}
	
	public String getText() {	
		return sideLabel.getText();
	}
	
	public String getSideLabelText() {	
		return sideLabel.getText();
	}
	
	public String getFullSqlName() {
		StringBuilder ret = new StringBuilder();
		this.getElements().forEach(elm ->{
			ret.append(elm.getFullSqlName() + "");
		});
		return ret.toString();
	}
	
	public String getSqlPivotizedColumn(Field pvtFld, String val) {
		StringBuilder ret = new StringBuilder();
		this.getElements().forEach(elm ->{
			ret.append(elm.getSqlPivotizedColumn(pvtFld, val) + "");
		});
		return ret.toString();
	}	
	
	public HBox getNode() {
		return cursorBox;
	}

	public void refreshSideLableText() {
		StringBuilder ret = new StringBuilder();
		this.getElements().forEach(elm -> ret.append(elm.getText() + ""));
		sideLabel.setText(ret.toString());		
	}

	public void saveXml(Document doc, Element parent) {
		Element rootE = doc.createElement("RootELM");
		parent.appendChild(rootE);
		this.getElements().forEach(ch -> ch.saveXml(doc, rootE));
	}

	public void openB(OpenContext context, org.w3c.dom.Node fx) {
		super.createXMLChildren(context, fx,this);
	}
	
	public Constellatio getNapp() {
		if(searchCON !=null) {
			return searchCON.getLay().nnode.nmap.napp;
		}else {
			return formula.getFieldLay().nnode.nmap.napp;
		}
	}

	public SearchCON getSearchCON() {
		return searchCON;
	}

	public FormulaField getFormula() {
		return formula;
	}

	public List<ELM> isUsedInElm(Field field) {
		List<ELM> used = new  ArrayList<ELM>();
		 this.getElements().forEach(el -> used.addAll(el.isUsedInElm(field)));
		return used;
	}
	
	public ELM getActiveELM() {		
		Node nd = cursorBox.getScene().focusOwnerProperty().get();
		if (nd instanceof CursorBox) {
			return ((CursorBox) nd).getElm();
	    }else {
	    	return null;
	    }	
	}
	
    public void updateStyle() {
    	if(this.getSelected() == Selector.SELECTED) {
    		sideLabel.styleSelected();
    	}else{
    		sideLabel.styleUnselected();
    	}
    }
	
	public void setStatus(Status status) {
		this.status.setValue(status);
	}
	
	public Status getStatus() {
		return status.getValue();
	}
	
	public void setSelected(Selector sel) {
		this.selected.setValue(sel);
	}
	
	public Selector getSelected() {
		return selected.getValue();
	}
}

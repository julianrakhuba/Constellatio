package logic;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import activity.Calculation;
import application.XML;
import elements.ELM;
import elements.RootELM;
import file.OpenContext;
import generic.ACT;
import generic.LAY;
//import javafx.beans.property.Property;
//import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class FormulaField extends Field {
	private RootELM root;
	
	public FormulaField(LAY fieldLay) {		
		super(fieldLay);
		root = new RootELM(this, fieldLay.nnode.nmap.napp);
	}
	
	public void activeClick(MouseEvent e) {
		ACT act = this.getFieldLay().nnode.nmap.napp.getFilemanager().getActiveNFile().getActivity();
		if(act instanceof Calculation) {
			((Calculation)act).activateClick(this, e);
		}
	}
	
	public String getFunction_Column() {
		return root.getFullSqlName();
	}
	
	public String getPivot_Column(Field pvtFld, String val) {
		return root.getSqlPivotizedColumn(pvtFld, val);
	}

	public HBox getFormulaHBox() {
		return root.getNode();
	}
		
	public String getText() {
		return root.getText();
	}
	public Label getSelectLabel() {	
		select.getLabel().setText(this.getText().trim());
		return  select.getLabel();
	}
	
	public void saveXml(Document document, Element fieldsE) {
		Element fieldE = document.createElement("field");
		fieldE.setAttribute("fieldType", "CALCULATED");	
		fieldE.setAttribute("rowset_type", this.getRowset_type());
		fieldE.setAttribute("aliase", this.getAliase());
		fieldE.setAttribute("select", this.isSelected() + "");
		fieldE.setAttribute("group", this.isGroupBy() + "");
		fieldE.setAttribute("pivot", this.isPivot() + "");
		fieldE.setAttribute("agrigate", this.isAgrigated() + "");
		fieldE.setAttribute("format_id", this.getFormat().getId());
		fieldsE.appendChild(fieldE);		
		root.saveXml(document, fieldE);
	}
	
	public void loopB(OpenContext context, Node nn) {
		XML.children(nn).forEach(n2 ->{
			if(n2.getNodeName().equals("RootELM")) {
				root.openB(context, n2);
			}
		});
	}

	public Label getLabel() {
		return root.getLabel();
	}

	public  List<ELM> isUsed(Field f) {
		return  this.root.isUsedInElm(f);
	}
	
	//CREATE ELEMENTS ••••••••••••••••••••••••••••••••••••••••••
	public void createFunctionELM(String nname, String label,String open, String openPar, String closePar, String close) {
		root.getActiveELM().createFunctionELM(nname, label, open, openPar, closePar, close, root, true).getCursorBox().activateFocus();
	}

	public void createFieldELM(Field field) {
		root.getActiveELM().createFieldELM(field, root, true);
	}

	public void createStringELM(String string, boolean activate) {
		root.getActiveELM().createStringELM(string, root, true).getNode().requestFocus();
	}

	public RootELM getRoot() {
		return root;
	}

}

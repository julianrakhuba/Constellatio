package rakhuba.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import rakhuba.application.XML;
import rakhuba.elements.ELM;
import rakhuba.elements.RootELM;
import rakhuba.elements.StringELM;
import rakhuba.file.OpenContext;
import rakhuba.generic.LAY;
import rakhuba.search.PAIR;
import rakhuba.status.Status;
import rakhuba.status.ValueType;

public class SearchCON {
	private LAY localLay;
	private LAY remoteLay;
	private RootELM root;
	private ArrayList<Group> groups = new ArrayList<Group>();
	private String uniqueId = UUID.randomUUID().toString(); //.replace("-", "");;

	public SearchCON(LAY localLay) {
		this.localLay = localLay;
		root = new RootELM(this, localLay.nnode.nmap.napp);
	}

	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(String aliase) {
		uniqueId = aliase;
	}
	
	//ROOT
	public HBox getNode() {
		return  root.getNode();
	}
	
	public Label getLabel() {
		return root.getLabel();
	}

	public String getFuncColumn() {
		if(root.getStatus() == Status.ACTIVE) {
			return " 1 = 1 ";// remove SearchCON from group to avoid this, when looking up for values from ValueELM.
		}else {
			return root.getFullSqlName();
		}
	}

	//PASS elements ••••••••••••••••••••••••••••••••••••••••••	
	public void saveXML(Document document, Element searchList) {
		Element searchE = document.createElement("searchCON");
		searchE.setAttribute("uniqueId", uniqueId);
		searchE.setAttribute("remoteLay", remoteLay !=null ? remoteLay.getAliase() : "null");
		root.saveXml(document, searchE);
		searchList.appendChild(searchE);
	}
	
	//loopA do not reach here
	public void openB(OpenContext context, Node sc) {
		String rl = XML.atr(sc, "remoteLay");
		if(!rl.equals("null"))	this.setRemoteLay(context.getAliaseLAYs().get(rl));		
		XML.children(sc).forEach(n->{
			if(n.getNodeName().equals("RootELM")) root.openB(context, n);
		});
	}
	
	public LAY getLay() {
		return this.localLay;
	}

	public LAY getRemoteLay() {
		return remoteLay;
	}
	
	public void setRemoteLay(LAY lay) {
		remoteLay = lay;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public void removeGroup(Group group) {
		groups.remove(group);
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public  List<ELM> isUsed(Field f) {
		return  this.root.isUsedInElm(f);
	}

	//CREATE ELEMENTS FOR ACTIVE ELM ••••••••••••••••••••••••••••••••••••••••••
	//FIELD
	public void createFieldELM(Field field) {
		if(root.getActiveELM() != null) root.getActiveELM().createFieldELM(field, root, true);
	}
	
	//VALUES
	public void createValuesELM(Field field, ValueType valueType) {
		root.getActiveELM().createValuesELM(field, valueType, root, true).showValuesMenu();
	}
	
	//STRING
	public void createStringELM(String string, boolean activate) {
		StringELM elm = root.getActiveELM().createStringELM(string, root, true);
		if(activate) elm.getNode().requestFocus();
	}
	
	//LAY
	public void createSubELM(LAY lay) {
		root.getActiveELM().createLayELM(lay, root, true).getCursorBox().activateFocus();
	}
	
	//FUNCTION
	public void createFunctionELM(String name, String label,String open, String openPar, String closePar, String close) {
		if(root.getActiveELM() != null) root.getActiveELM().createFunctionELM(name, label, open, openPar, closePar, close, root, true).getCursorBox().activateFocus();
	}
	
	//AUTO CREATE SETS •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public void autoSearchFunc(Field field, PAIR funcVAL) {
		root.createFieldELM(field, root, false);
		root.createStringELM(funcVAL.getFnc(), root, false);		
		if(funcVAL.getVal() != null)  {// use to have no value search
			root.createValuesELM(funcVAL.getVal(), field, root, false);
		}
	}

	public void autoBetween(Field field, String from, String to) {
		root.createFieldELM(field, root, false);
		root.createStringELM(" BETWEEN ", root, false);
		root.createValuesELM(from, field, root, false);
		root.createStringELM(" AND ", root, false);
		root.createValuesELM(to, field, root, false);
	}

	public void autoIn(Field field, String in, ArrayList<String> values) {
		root.createFieldELM(field, root, false);
		root.createFunctionELM(in, in , "(", "", "", ")", root, false).createValuesELM(values, field, root, false);
	}

	public void autoJoin(Field localField, Field remoteField) {
		root.createFieldELM(localField, root, false);
		root.createStringELM("=", root, false);
		root.createFieldELM(remoteField, root, false);
	}

	public void autoSub(Field localField, LAY lay, Field remoteField) {
		root.createFieldELM(localField, root, false);
		root.createFunctionELM("IN", "in" , "(", "", "", ")", root, false).createLayELM(lay, root, false).createFieldELM(remoteField, root, false);
	}
	
	public RootELM getRoot() {
		return root;
	}
}
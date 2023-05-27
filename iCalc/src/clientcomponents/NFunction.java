package clientcomponents;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import application.XML;
import generic.BO;

public class  NFunction  extends BO{
	public String label; 
	public String open; 
	public String close; 
	public String type; 
	public String realname; 
	public String openParam; 
	public String closeParam;
	public String rowset_type = "String"; 

	
	public void saveXml(Document doc, Element rowE) {
		rowE.setAttribute("label", label);
		rowE.setAttribute("open", open);
		rowE.setAttribute("close", close);
		rowE.setAttribute("type", type);
		rowE.setAttribute("realname", realname);
		rowE.setAttribute("openParam", openParam);
		rowE.setAttribute("closeParam", closeParam);
		rowE.setAttribute("rowset_type", rowset_type);
	}
	
	public NFunction(Node ch) {
		this.setLabel(XML.atr(ch, "label") == null? null: XML.atr(ch, "label"));
		this.setOpen(XML.atr(ch, "open") == null? null: XML.atr(ch, "open"));
		this.setClose(XML.atr(ch, "close") == null? null: XML.atr(ch, "close"));
		this.setType(XML.atr(ch, "type") == null? null: XML.atr(ch, "type"));
		this.setRealname(XML.atr(ch, "realname") == null? null: XML.atr(ch, "realname"));
		this.setOpenParam(XML.atr(ch, "openParam") == null? null: XML.atr(ch, "openParam"));
		this.setCloseParam(XML.atr(ch, "closeParam") == null? null: XML.atr(ch, "closeParam"));
		this.setRowset_type(XML.atr(ch, "rowset_type") == null? null: XML.atr(ch, "rowset_type"));
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getOpenParam() {
		return openParam;
	}

	public void setOpenParam(String openParam) {
		this.openParam = openParam;
	}

	public String getCloseParam() {
		return closeParam;
	}

	public void setCloseParam(String closeParam) {
		this.closeParam = closeParam;
	}
	
	public String toString(){return "{" + ", " + "label: " + label + ", " + "open: " + open + ", " + "close: " + close + ", " + "type: " + type + ", " + "realname: " + realname + ", " + "openParam: " + openParam + ", " + "closeParam: " + closeParam + "}";}

	public String getRowset_type() {
		return rowset_type;
	}

	public void setRowset_type(String rowset_type) {
		this.rowset_type = rowset_type;
	}

	
}
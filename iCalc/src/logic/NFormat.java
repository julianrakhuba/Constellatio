package logic;

//import java.util.ArrayList;
//import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import application.XML;
import generic.BO;

public class NFormat extends BO{
	private String id;
	private String label;
	private String format;
	
	public NFormat(Node ch) {
		id = XML.atr(ch, "id") == null? null: XML.atr(ch, "id");
		label = XML.atr(ch, "label") == null? null: XML.atr(ch, "label");
		format = XML.atr(ch, "format") == null? null: XML.atr(ch, "format");
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFormatString() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void saveXml(Document document, Element fieldE) {
		Element formatE = document.createElement("format");
		formatE.setAttribute("id", id);
		formatE.setAttribute("label", label);
		formatE.setAttribute("format", format);
		fieldE.appendChild(formatE);
	}

	public String getId() {
		return id;
	}
}

package rakhuba.clients;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rakhuba.application.XML;
import rakhuba.generic.BO;

public class DataType extends BO {
	private String name;
	private ArrayList<String> formats = new ArrayList<String>();

	public DataType(Node ch) {
		name = XML.atr(ch, "name") == null? null: XML.atr(ch, "name");
		XML.children(ch).forEach(ch2->{
			if(ch2.getNodeName().equals("format")) {
				formats.add(XML.atr(ch2, "formatId") == null? null: XML.atr(ch2, "formatId"));
			}
		});
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<String> getFormats() {
		return formats;
	}
	
	public void saveXml(Document document, Element fieldE) {
		fieldE.setAttribute("name", name);
		formats.forEach(fm ->{
			Element fE = document.createElement("format");
			fE.setAttribute("formatId", fm);
			fieldE.appendChild(fE);
		});
	}
	
}

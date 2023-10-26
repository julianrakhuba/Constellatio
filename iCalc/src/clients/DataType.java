/*******************************************************************************
 * /*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *  *******************************************************************************/
 *******************************************************************************/
package clients;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import application.XML;
import generic.BO;

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

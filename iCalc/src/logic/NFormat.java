/*******************************************************************************
 *  MIT License
 *
 * Copyright (c) 2023 Julian Rakhuba
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package logic;

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

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
package login;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import application.Constellatio;
import application.XML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Configuration {
	private final ObservableList<Login> loginsList = FXCollections.observableArrayList();
	private File file;
	private Constellatio napp;

	public Configuration(Constellatio napp) {
		this.napp = napp;
		file = new File(napp.getConfigurationPath() + "configuration.xml");
		if (!file.exists()) {
			try {
				Files.createDirectories(Paths.get(napp.getConfigurationPath()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Login login = new Login();
			login.sampleFill();
			loginsList.add(login);
			this.save();
			loginsList.clear();
			file = new File(napp.getConfigurationPath() + "configuration.xml");
		}
		if (file != null) {
			if (file.isFile()) {
				
				try {
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
					for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
						if (doc.getChildNodes().item(i).getNodeName().equals("configuration")) {
							String translusent = XML.atr(doc.getChildNodes().item(i), "translusent");
							napp.getMenu().getViewMenu().getGlassModeMenuItem().setSelected(Boolean.valueOf(translusent));
							this.open(doc.getChildNodes().item(i));
						}
					}
				} catch (SAXException | IOException | ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void open(Node node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equals("connection")) {
				Node conN = node.getChildNodes().item(i);
				Login login = new Login(conN);
				loginsList.add(login);
			}
		}
	}

	public void save() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element docE = doc.createElement("configuration");
			docE.setAttribute("translusent", "" + napp.getMenu().getViewMenu().getGlassModeMenuItem().isSelected());
			doc.appendChild(docE);
			loginsList.forEach(login -> login.saveToXml(docE));
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// overwrites every time
			transformer.transform(new DOMSource(doc), new StreamResult(file));
		} catch (ParserConfigurationException | TransformerException pce) {
			pce.printStackTrace();
		}
	}

	public ObservableList<Login> getLoginList() {
		return loginsList;
	}
}

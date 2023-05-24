package login;

import java.io.File;
import java.io.IOException;
//import java.util.HashMap;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import rakhuba.login.Login;

public class Logins {
	private final ObservableList<Login> loginsList = FXCollections.observableArrayList();	
	private File file;
	
	public Logins() {
		this.openFromFile();
	}

	private void openFromFile() {
		String pathname = System.getProperty("user.home") + "/Library/Application Support/rakhuba/config/";
		file = new File(pathname + "logins.xml");		
		if (!file.exists()) {
			try { Files.createDirectories(Paths.get(pathname)); } catch (IOException e1) { e1.printStackTrace(); }
			Login login = new Login();
			login.sampleFill();
			loginsList.add(login);
			this.saveConnectionsToXML();
			loginsList.clear();
			file = new File(pathname+ "logins.xml");
		}
		if (file != null) {			
			if(file.isFile()) {
				try {
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
					for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
						if (doc.getChildNodes().item(i).getNodeName().equals("document")) {
							this.openLoginsFile(doc.getChildNodes().item(i));
						}
					}
				}
				catch (SAXException | IOException | ParserConfigurationException e) {e.printStackTrace();}
			}
		}
	}
	
	private void openLoginsFile(Node node) {
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			if (node.getChildNodes().item(i).getNodeName().equals("connection")) {
				Node conN = node.getChildNodes().item(i);
				Login login = new Login(conN);			
				loginsList.add(login);
			}			
		}
	}
	
	
	public void saveConnectionsToXML() {
		try {
		    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		    Element docE = doc.createElement("document");
		    doc.appendChild(docE);
		    loginsList.forEach(login -> {
//		    	this.hashSecureLogin(login); // this used here for initial loop, will create all logins salt and password every time logs in
		    	login.saveToXml(docE);
		    });

		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        //overwrites every time
		    transformer.transform(new DOMSource(doc), new StreamResult(file));
		}catch(ParserConfigurationException | TransformerException pce){  pce.printStackTrace();}
	}

	public ObservableList<Login> getLoginList() {
		return loginsList;
	}
	
	public Login getLogin(String db, String instance) {
		return null;
	}
}

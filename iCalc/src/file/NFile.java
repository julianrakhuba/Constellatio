package file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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

import activity.Calculation;
import activity.Configure;
import activity.Edit;
import activity.Select;
import activity.View;
import application.NMap;
import application.XML;
import generic.ACT;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import managers.FileManager;
import managers.NSidePane;
import managers.TabManager;
import managers.UndoManager;
import sidePanel.HeaderLabel;
import sidePanel.Message;
import status.ActivityMode;
import status.VisualStatus;


public class NFile  {
	private File file;
	private HashMap<String, NMap> maps = new HashMap<String, NMap>();
	private UndoManager undoManager = new UndoManager(this);
	private boolean isNewFile;	
	public 	TabManager tabManager;
	public NSidePane infoPaneManager;
	private NMap activeNmap;
	private FileManager fileManager;
	private ObservableList<Region> messagesSideVBox = FXCollections.observableArrayList();
	public StackPane logicGlassSP = new StackPane();
	private Property<ActivityMode> mode = new SimpleObjectProperty<ActivityMode>(ActivityMode.SELECT);
	private HashMap<ActivityMode, ACT> activities = new HashMap<ActivityMode, ACT>();
	private ObservableList<Message> messages = FXCollections.observableArrayList();
	public VBox messageListHBox = new VBox(10);
	private Pane messagesLbl = new HeaderLabel("messages","#ade0ff");
	
	private CenterMessage centerMessage;

	//new split
	private QuadSplit quadSplit = new QuadSplit();
	
	public NFile(File file, FileManager fileManager) {
		this.file = file;
		this.fileManager = fileManager;
		centerMessage = new CenterMessage(this);
		tabManager = new TabManager(this);
		infoPaneManager = new NSidePane(this);
		activities.put(ActivityMode.SELECT, new Select(this)); 
		activities.put(ActivityMode.VIEW, new View(this));
		activities.put(ActivityMode.EDIT, new Edit(this));
		activities.put(ActivityMode.CONFIGURE, new Configure(this));
		activities.put(ActivityMode.FORMULA, new Calculation(this));
		
		
//		StackPane.setAlignment(centerMessage, Pos.BOTTOM_RIGHT);
//	    StackPane.setMargin(centerMessage, new Insets(0, 0, 12, 0));	    
//	    centerMessage.setStyle("-fx-text-fill: rgba(0,0,0, 0.5); -fx-border-width: 1;-fx-border-color: white;  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.0); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 4, 0.2, 0.0, 0.0); -fx-background-radius: 15; -fx-border-radius: 15;");
//	    centerMessage.setAlignment(Pos.BASELINE_CENTER);
////	    centerMessageText.prefWidthProperty().bind(logicGlassSP.widthProperty().add(-20));
//	    centerMessage.prefWidthProperty().bind(logicGlassSP.widthProperty());
//
//
//	    
//		if(fileManager.napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
//		    centerMessage.setStyle("-fx-text-fill: rgba(255,255,255, 0.8); -fx-border-width: 0.5; -fx-border-color: #1E90FF;  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 4, 0.2, 0.0, 0.0); -fx-background-radius: 0; -fx-border-radius: 0;");
//		}else {
//		    centerMessage.setStyle("-fx-text-fill: rgba(0,0,0, 0.5); -fx-border-width: 1 0 1 0; -fx-border-color: white;  -fx-padding: 2 10 2 10; -fx-background-color: rgba(255, 255, 255, 0.4);  -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.1), 5, 0.0 , 0, 0); -fx-background-radius: 0; -fx-border-radius: 0;");
//		}

//		centerMessage.setMessage("message set test");
		logicGlassSP.setAlignment(Pos.CENTER);
		logicGlassSP.setStyle("-fx-background-color: transparent; -fx-padding: 5 5 5 5;");	
		logicGlassSP.setPickOnBounds(false);
		logicGlassSP.setMinWidth(0);

		quadSplit.setTopLeft(logicGlassSP);
		messagesSideVBox.addAll(messagesLbl, messageListHBox);
		
		messages.addListener((ListChangeListener<? super Message>) c -> {
			if(c.next()) {
				c.getAddedSubList().forEach(jl -> {
					messageListHBox.getChildren().add(jl.getLabel());
				});
				c.getRemoved().forEach(jl -> {
					messageListHBox.getChildren().remove(jl.getLabel());
				});
			}
		});

		this.addMessage(new Message(this, "", "security, rolls"));
		this.addMessage(new Message(this, "", "disc joins on shcema delete"));
		this.addMessage(new Message(this, "", "func fld labl update"));
		this.addMessage(new Message(this, "", "clear search when deleted condition"));
	}	
	
	public UndoManager getUndoManager() {
		return undoManager;
	}

	public NSidePane getSidePaneManager() {
		return infoPaneManager;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	public NMap createNewMap(String schema) {
		NMap nmap = new NMap(this,schema);
		maps.put(schema, nmap);
		this.showNmap(schema);
		return nmap;
	}
	
	//************************************************************************ LOGIC STACK PANE
	public void showNmap(String schema) {
		NMap nmap = maps.get(schema);
		if(activeNmap !=null) {
			logicGlassSP.getChildren().clear();
		}
		activeNmap = nmap;		
		logicGlassSP.getChildren().addAll(nmap.schemaScrollPane, centerMessage);		
		this.setAppTitle();
	}
	
	public void refreshTempFixForOffsetIssue() {
		if(activeNmap !=null) {
			logicGlassSP.getChildren().clear();
			logicGlassSP.getChildren().addAll(activeNmap.schemaScrollPane, centerMessage);
		}
	}
	
	public void removeSchema(String schema) {
		NMap nmapToRRemove = maps.get(schema);
		maps.remove(schema);
		tabManager.removeNSheetFor(nmapToRRemove);
		
		if(activeNmap == nmapToRRemove ) {
			logicGlassSP.getChildren().clear();

//			logicGlassSP.getChildren().remove(activeNmap.schemaScrollPane);
			//REMOVE SHEETS
			//REMOVE SEARCHES
			//disconnect from other logics
			this.showNmap(maps.keySet().stream().findFirst().get());
		}else {
			//REMOVE SHEETS
			//REMOVE SEARCHES
			//disconnect from other logics
		}		
	}
//************************************************************************
	
	
	public void setAppTitle() {
		if(activeNmap != null) {
			fileManager.napp.setTitle(this.getXMLFile().getName() + " (" + activeNmap.getSchemaName() +")");
		}else {
			fileManager.napp.setTitle(this.getXMLFile().getName() + " ()");
		}
	}
	
	public NMap getActiveNmap() {
		return activeNmap;
	}
	public HashMap<String, NMap> getMaps() {
		return maps;
	}

	public File getXMLFile() {
		return file;
	}
	
	public void setZIP(File file) {
		this.file = file;
	}
	
	public boolean isNewFile() {
		return isNewFile;
	}

	public void setNewFile(boolean newFile) {
		this.isNewFile = newFile;
	}
	
	public void openFile() {
		this.fileManager.selectNFile(this);
		String extension = "";
		if (file.getName().contains("."))   extension = file.getName().substring(file.getName().lastIndexOf("."));
		
		if(extension.equals(".xml")) {//XML
			try {this.openNewDocument(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file));}
			catch (SAXException | IOException | ParserConfigurationException e) {e.printStackTrace();}
		}else if(extension.equals(".con")) {//CON
			try {
				ZipFile zf = new ZipFile (file);
				InputStream in = zf.getInputStream(zf.getEntry("document.xml"));
				try {this.openNewDocument(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in));}
				catch (SAXException | IOException | ParserConfigurationException e) {e.printStackTrace();}
				zf.close();
			} catch (IOException e) {e.printStackTrace();}
		}	       
	}

	public void openNewDocument(Document doc) {
		
		XML.children(doc).forEach(n->{
			if(n.getNodeName().equals("NFile")) {
				this.openNewRoot(n);
			}
		});
		if(tabManager.getStatus() == VisualStatus.SHOW) tabManager.showGrid();
		undoManager.saveUndoAction();
	}
	
	private void openNewRoot (Node rootX) {
		OpenContext context = new OpenContext();
		List<Node> nodes = XML.children(rootX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("configuration")) {
				tabManager.setStatus(VisualStatus.valueOf(XML.atr(n, "gridVisablity")));
				infoPaneManager.setStatus(VisualStatus.valueOf(XML.atr(n, "searchVisability")));
			}
		});
		
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schemas")) {
				this.openNewSchemas(context, n);
			}
		});
	}
	
	private void openNewSchemas (OpenContext context, Node schemasX) {
		List<Node> nodes = XML.children(schemasX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schema")) {
				NMap newMap = this.createNewMap(XML.atr(n, "schemaName"));
				newMap.loopA(context, n);
			}
		});
		
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schema")) {
				maps.get(XML.atr(n, "schemaName")).loopB(context, n);
			}
		});
	}
	
	private void openUndoSchemas (OpenContext context, Node schemasX) {
		List<Node> nodes = XML.children(schemasX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schema")) {
				maps.get(XML.atr(n, "schemaName")).loopA(context, n);
			}
		});
		
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schema")) {
				maps.get(XML.atr(n, "schemaName")).loopB(context, n);
			}
		});
	}
	
	public void openUndoDocument(Document doc) {
		maps.forEach((s, m) -> m.clear());//Clear all maps of this file
		XML.children(doc).forEach(n->{
			if(n.getNodeName().equals("NFile")) {
				this.openUndoRoot(n);
			}
		});
		if(tabManager.getStatus() == VisualStatus.SHOW) tabManager.showGrid();
	}
	
	private void openUndoRoot (Node rootX) {
		OpenContext context = new OpenContext();
		
		List<Node> nodes = XML.children(rootX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("configuration")) {
				tabManager.setStatus(VisualStatus.valueOf(XML.atr(n, "gridVisablity")));
				infoPaneManager.setStatus(VisualStatus.valueOf(XML.atr(n, "searchVisability")));
			}
		});
		
		nodes.forEach(n ->{
			if(n.getNodeName().equals("schemas")) {
				this.openUndoSchemas(context,n);
			}
		});
	}
	


	public void clear() {
		tabManager.clear();
	}

	public TabManager getTabManager() {
		return tabManager;
	}

	public void saveFile() {
    	String extension = "";
		if (file.getName().contains("."))   extension = file.getName().substring(file.getName().lastIndexOf("."));
		if(extension.equals(".xml")) {
			try {
				if (!(file == null)){	        
			        Transformer transformer = TransformerFactory.newInstance().newTransformer();
			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.transform(new DOMSource(this.createDocument()), new StreamResult(file));
				} 
			} catch (TransformerException e){e.printStackTrace();}  
		}else if(extension.equals(".con")) {
			try {
				FileOutputStream fos = new FileOutputStream(file);
				ZipOutputStream zipOut = new ZipOutputStream(fos);
				ZipEntry zipEntry = new ZipEntry("document.xml");
				zipOut.putNextEntry(zipEntry);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				transformer.transform(new DOMSource(this.createDocument()), new StreamResult(bout));
				zipOut.write(bout.toByteArray());
				zipOut.closeEntry();
				zipOut.close();
			} catch (IOException | TransformerException e) {
				e.printStackTrace();
			}
		}
	}
			
	public Document createDocument() {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("NFile");
			document.appendChild(root);
			//Configuration info
			Element configE = document.createElement("configuration");
			configE.setAttribute("gridVisablity", this.tabManager.getStatus().toString());
			configE.setAttribute("searchVisability", this.infoPaneManager.getStatus().toString());
			root.appendChild(configE);

			Element schemas = document.createElement("schemas");
			root.appendChild(schemas);	       
			maps.forEach((name, map) -> {//multi-schema Currently will have only one map
				map.save(document, schemas);
			});
	        return document;
	        
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	public ActivityMode getActivityMode() {
		return mode.getValue();
	}

	public void setActivityMode(ActivityMode mode) {
		this.mode.setValue(mode);
	}
	
	public ACT getActivity() {
		return activities.get(mode.getValue());
	}
	
	public HashMap<ActivityMode, ACT> getActivities() {
		return activities;
	}

	public void setCompactView(boolean b) {
		maps.forEach((s,m) -> {
			m.setCompactView(b);
		});		
	}
	
	public ObservableList<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void ActivateFile() {
		fileManager.napp.appBorderPane.setCenter(quadSplit);		
	}

	public ObservableList<Region> getMessagesRegion() {
		return messagesSideVBox;
	}

	public QuadSplit getQuadSplit() {
		return quadSplit;
	}

	public CenterMessage getCenterMessage() {
		return centerMessage;
	}
	
}

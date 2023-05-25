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
import application.Nmap;
import application.XML;
import generic.ACT;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import managers.FileManager;
import managers.SideManager;
import managers.TabManager;
import managers.UndoManager;
import sidePanel.HeaderLabel;
import sidePanel.Message;
import status.ActivityMode;
import status.VisualStatus;


public class NFile  {
	private File file;
	private HashMap<String, Nmap> maps = new HashMap<String, Nmap>();

	private UndoManager undoManager = new UndoManager(this);
	private boolean isNewFile;	
	public 	TabManager gridManager;
	public SideManager infoPaneManager;
	private Nmap activeNmap;
	private FileManager fileManager;
	private BorderPane fileBorderPane = new BorderPane();
	private ObservableList<Region> messagesSideVBox = FXCollections.observableArrayList();

	
	public StackPane stackPane = new StackPane();
	private Property<ActivityMode> mode = new SimpleObjectProperty<ActivityMode>(ActivityMode.SELECT);
	private HashMap<ActivityMode, ACT> activities = new HashMap<ActivityMode, ACT>();
	private ObservableList<Message> messages = FXCollections.observableArrayList();	
	public VBox messageListHBox = new VBox(10);
	private SplitPane splitPane = new SplitPane();
	private Pane messagesLbl = new HeaderLabel("messages","#ade0ff");
//	
	
	
	public NFile(File file, FileManager fileManager) {
		this.file = file;
		this.fileManager = fileManager;
		gridManager = new TabManager(this);
		infoPaneManager = new SideManager(this);
		activities.put(ActivityMode.SELECT, new Select(this)); 
		activities.put(ActivityMode.VIEW, new View(this));
		activities.put(ActivityMode.EDIT, new Edit(this));
		activities.put(ActivityMode.CONFIGURE, new Configure(this));
		activities.put(ActivityMode.FORMULA, new Calculation(this));
		
		splitPane.setOrientation(Orientation.VERTICAL);
		stackPane.setAlignment(Pos.TOP_LEFT);
		stackPane.setPickOnBounds(false);
		fileBorderPane.setMinHeight(0);
		
		
//		HBox hb = new HBox();
		
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
			infoPaneManager.activateErrorTab();
		});
//		this.addMessage(new Message(this, "", "function data type"));
//		this.addMessage(new Message(this, "", "hierchy xmlbase file"));
//		this.addMessage(new Message(this, "", "catalog-group schema"));
		this.addMessage(new Message(this, "", "security, rolls"));
		this.addMessage(new Message(this, "", "disconnect joins on shcema delete"));

	}
	
	public UndoManager getUndoManager() {
		return undoManager;
	}

	public SideManager getSidePaneManager() {
		return infoPaneManager;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	public Nmap createNewMap(String schema) {
		Nmap nmap = new Nmap(this,schema);
		maps.put(schema, nmap);
		this.activateNmap(schema);
		fileBorderPane.setCenter(stackPane);
		if(!splitPane.getItems().contains(fileBorderPane)) splitPane.getItems().add(fileBorderPane);
		return nmap;
	}
	
	public void activateNmap(String schema) {
		Nmap nmap = maps.get(schema);
		if(activeNmap !=null) {
			stackPane.getChildren().remove(activeNmap.schemaScrollPane);
		}
		activeNmap = nmap;		
		stackPane.getChildren().add(0, nmap.schemaScrollPane);
		
		this.setAppTitle();
	}
	
	public void setAppTitle() {
		if(activeNmap != null) {
			fileManager.napp.setTitle(this.getXMLFile().getName() + " (" + activeNmap.getSchemaName() +")");
		}else {
			fileManager.napp.setTitle(this.getXMLFile().getName() + " ()");
		}
	}
	
	public void refreshTempFixForOffsetIssue() {
		if(activeNmap !=null) {
			stackPane.getChildren().remove(activeNmap.schemaScrollPane);
			stackPane.getChildren().add(0, activeNmap.schemaScrollPane);
		}
	}
	
	public void removeSchema(String schema) {
		Nmap nmapToRRemove = maps.get(schema);
		maps.remove(schema);
		gridManager.removeNSheetFor(nmapToRRemove);
		
		if(activeNmap == nmapToRRemove ) {
			stackPane.getChildren().remove(activeNmap.schemaScrollPane);
			//REMOVE SHEETS
			//REMOVE SEARCHES
			//disconnect from other logics
			this.activateNmap(maps.keySet().stream().findFirst().get());
		}else {
			//REMOVE SHEETS
			//REMOVE SEARCHES
			//disconnect from other logics
		}		
	}

	public Nmap getActiveNmap() {
		return activeNmap;
	}
	public HashMap<String, Nmap> getMaps() {
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
		if(gridManager.getStatus() == VisualStatus.SHOW) gridManager.showGrid();
		undoManager.saveUndoAction();
	}
	
	private void openNewRoot (Node rootX) {
		OpenContext context = new OpenContext();
		List<Node> nodes = XML.children(rootX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("configuration")) {
				gridManager.setStatus(VisualStatus.valueOf(XML.atr(n, "gridVisablity")));
				infoPaneManager.setStatus(VisualStatus.valueOf(XML.atr(n, "searchVisability")));
//				fileManager.napp.rollup.setSelected(Boolean.valueOf(XML.atr(n, "rollup")));
//				fileManager.napp.orderby.setSelected(Boolean.valueOf(XML.atr(n, "orderby")));
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
				Nmap newMap = this.createNewMap(XML.atr(n, "schemaName"));
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
//		maps.forEach((s, m) -> m.removeMissignFields());//Clear all maps of this file
		if(gridManager.getStatus() == VisualStatus.SHOW) gridManager.showGrid();
	}
	
	private void openUndoRoot (Node rootX) {
		OpenContext context = new OpenContext();
		
		List<Node> nodes = XML.children(rootX);
		nodes.forEach(n ->{
			if(n.getNodeName().equals("configuration")) {
				gridManager.setStatus(VisualStatus.valueOf(XML.atr(n, "gridVisablity")));
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
		gridManager.clear();
	}

	public TabManager getGridManager() {
		return gridManager;
	}

	public void saveFile() {
    	System.out.println("Save Existing XML FILE: " + file.getPath());    	
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
			System.out.println("Create Document!");
			Element root = document.createElement("NFile");
			document.appendChild(root);
			//Configuration info
			Element configE = document.createElement("configuration");
			configE.setAttribute("gridVisablity", this.gridManager.getStatus().toString());
			configE.setAttribute("searchVisability", this.infoPaneManager.getStatus().toString());
//			configE.setAttribute("rollup", this.fileManager.napp.rollup.isSelected()+"");
//			configE.setAttribute("orderby", this.fileManager.napp.orderby.isSelected()+"");
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

	public BorderPane getFileBorderPane() {
		return fileBorderPane;
	}

	public void showTabPane(TabPane tabPane) {
		splitPane.getItems().add(tabPane);
		splitPane.getDividers().get(0).setPosition(0.7);//Default 100% 1st pane
	}

	public void hideTabPane(TabPane tabPane) {
		splitPane.getItems().remove(tabPane);
	}

	public void ActivateFile() {
		fileManager.napp.borderPane.setCenter(splitPane);		
	}

	public ObservableList<Region> getMessagesRegion() {
		return messagesSideVBox;
	}
	
	
}

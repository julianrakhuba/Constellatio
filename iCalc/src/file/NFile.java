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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
	private HashMap<String, NMap> maps = new HashMap<String, NMap>();

	private UndoManager undoManager = new UndoManager(this);
	private boolean isNewFile;	
	public 	TabManager gridManager;
	public SideManager infoPaneManager;
	private NMap activeNmap;
	private FileManager fileManager;
	private SplitPane upperSplitPane = new SplitPane();
	private ObservableList<Region> messagesSideVBox = FXCollections.observableArrayList();
	public StackPane logicStackPane = new StackPane();

	private Property<ActivityMode> mode = new SimpleObjectProperty<ActivityMode>(ActivityMode.SELECT);
	private HashMap<ActivityMode, ACT> activities = new HashMap<ActivityMode, ACT>();
	private ObservableList<Message> messages = FXCollections.observableArrayList();	
	public VBox messageListHBox = new VBox(10);
	private SplitPane fileSplitPane = new SplitPane();
	private Pane messagesLbl = new HeaderLabel("messages","#ade0ff");
	private Timeline showSideTimeLine;
	private Timeline hideSideTimeLine;
	
	private Timeline showBottomTimeLine;
	private Timeline hideBottomTimeLine;
	
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
		
		fileSplitPane.setOrientation(Orientation.VERTICAL);
		
		
		fileSplitPane.setStyle("-fx-background-color: rgba(0,0,0,0);");
		upperSplitPane.setStyle("-fx-background-color: rgba(0,0,0,0);");
		
		
		
//		ImageView imageView = new ImageView( new Image(getClass().getResource("/cyber.jpg").toExternalForm()));
//		imageView.setOpacity(0.05);
//		imageView.fitHeightProperty().bind(appBorderPane.heightProperty());
////		imageView.fitWidthProperty().bind(appBorderPane.widthProperty());
//		ColorAdjust grayscale = new ColorAdjust();
//		grayscale.setSaturation(-1);
//		GaussianBlur gaus = new GaussianBlur(10);
//		gaus.setInput(grayscale);		
//		imageView.setEffect(gaus);
		
		
		logicStackPane.setAlignment(Pos.CENTER);
		logicStackPane.setStyle("-fx-background-color: transparent; -fx-padding: 5;");	
		logicStackPane.setPickOnBounds(false);
		logicStackPane.setMinWidth(0);


		
		upperSplitPane.setMinHeight(0);		
		upperSplitPane.getItems().add(logicStackPane);

		
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

		this.addMessage(new Message(this, "", "security, rolls"));
		this.addMessage(new Message(this, "", "disc joins on shcema delete"));
		this.addMessage(new Message(this, "", "func fld labl update"));
		this.addMessage(new Message(this, "", "clear search when deleted condition"));
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
	
	public NMap createNewMap(String schema) {
		NMap nmap = new NMap(this,schema);
		maps.put(schema, nmap);
		this.activateNmap(schema);
		if(!fileSplitPane.getItems().contains(upperSplitPane)) fileSplitPane.getItems().add(upperSplitPane);
		return nmap;
	}
	
	//************************************************************************ LOGIC STACK PANE
	public void activateNmap(String schema) {
		NMap nmap = maps.get(schema);
		if(activeNmap !=null) {
			logicStackPane.getChildren().clear();
		}
		activeNmap = nmap;		
		logicStackPane.getChildren().add(nmap.schemaScrollPane);		
		this.setAppTitle();
	}
	
	public void refreshTempFixForOffsetIssue() {
		if(activeNmap !=null) {
			logicStackPane.getChildren().clear();
			logicStackPane.getChildren().add(activeNmap.schemaScrollPane);
		}
	}
	
	public void removeSchema(String schema) {
		NMap nmapToRRemove = maps.get(schema);
		maps.remove(schema);
		gridManager.removeNSheetFor(nmapToRRemove);
		
		if(activeNmap == nmapToRRemove ) {
			logicStackPane.getChildren().remove(activeNmap.schemaScrollPane);
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
		fileManager.napp.appBorderPane.setCenter(fileSplitPane);		
	}

	public ObservableList<Region> getMessagesRegion() {
		return messagesSideVBox;
	}
	
	
	//Bottom TapPane
	public void showLowerPane(TabPane tabPane) {
//		fileSplitPane.getItems().add(tabPane);
//		fileSplitPane.getDividers().get(0).setPosition(0.6);//Default 100% 1st pane
		
		if(hideBottomTimeLine != null && hideBottomTimeLine.getStatus() == Status.RUNNING) hideBottomTimeLine.stop();
		if(!fileSplitPane.getItems().contains(tabPane)) {
			fileSplitPane.getItems().add(tabPane);
			tabPane.setOpacity(0);
			Divider div = fileSplitPane.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.6));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(tabPane.opacityProperty(), 1));
			showBottomTimeLine = new Timeline();
			showBottomTimeLine.getKeyFrames().addAll(kf1, kf2);
			showBottomTimeLine.setCycleCount(1);
			showBottomTimeLine.play();
		}
	}

	public void hideTabPane(TabPane tabPane) {
//		fileSplitPane.getItems().remove(tabPane);
		
		if(showBottomTimeLine != null && showBottomTimeLine.getStatus() == Status.RUNNING) showBottomTimeLine.stop();				
		if (fileSplitPane.getItems().contains(tabPane)) {
			Divider div = fileSplitPane.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(tabPane.opacityProperty(), 0));
			hideBottomTimeLine = new Timeline();
			hideBottomTimeLine.getKeyFrames().addAll(kf1, kf2);
			hideBottomTimeLine.setCycleCount(1);
		    hideBottomTimeLine.setOnFinished(e -> {
		    	fileSplitPane.getItems().remove(tabPane);
			});
		    hideBottomTimeLine.play();
		}
		
		
	}

	//SIDE PANE
	public void showSideManager(StackPane rightPane) {
		if(hideSideTimeLine != null && hideSideTimeLine.getStatus() == Status.RUNNING) hideSideTimeLine.stop();
		if(!upperSplitPane.getItems().contains(rightPane)) {
			upperSplitPane.getItems().add(rightPane);
			rightPane.setOpacity(0);
			Divider div = upperSplitPane.getDividers().get(0);
			div.setPosition(1);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 0.82));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(rightPane.opacityProperty(), 1));
			showSideTimeLine = new Timeline();
			showSideTimeLine.getKeyFrames().addAll(kf1, kf2);
		    showSideTimeLine.setCycleCount(1);
		    showSideTimeLine.play();
		}		
	}

	public void hideSideManager(StackPane rightPane) {
		if(showSideTimeLine != null && showSideTimeLine.getStatus() == Status.RUNNING) showSideTimeLine.stop();				
		if (upperSplitPane.getItems().contains(rightPane)) {
			Divider div = upperSplitPane.getDividers().get(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(div.positionProperty(), 1));
			KeyFrame kf2 = new KeyFrame(Duration.millis(200), new KeyValue(rightPane.opacityProperty(), 0));
		    hideSideTimeLine = new Timeline();
		    hideSideTimeLine.getKeyFrames().addAll(kf1, kf2);
		    hideSideTimeLine.setCycleCount(1);
		    hideSideTimeLine.setOnFinished(e -> {
				upperSplitPane.getItems().remove(rightPane);
			});
		    hideSideTimeLine.play();
		}
	}
	
	
}

package application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import activity.Configure;
import clientcomponents.NFunction;
import clientcomponents.NTable;
import file.NFile;
import file.OpenContext;
import generic.DLayer;
import generic.LAY;
import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
//import rakhuba.application.Constellatio;
//import rakhuba.application.JoinLine;
//import rakhuba.application.Nnode;
//import rakhuba.application.XML;
import sidePanel.Message;
import status.ActivityMode;
import status.JoinType;
import status.Population;
import status.SqlType;

public class Nmap  {
	public  Constellatio napp;
	public  Pane schemaPane = new Pane();
	public LAY lastLAY;
	public ScrollPane schemaScrollPane;
	private Group group = new Group(schemaPane);
	private String schemaName;
	private HashMap<String, Nnode> mapNodes = new HashMap<String, Nnode>();
	private LinkedHashMap<String, NFunction> dbFunctionsMap = new LinkedHashMap<String, NFunction>();
	private NFile nFile;

	public Nmap(NFile nFile,String schema) {
		this.nFile = nFile;
		this.napp = nFile.getFileManager().napp;
		schemaScrollPane = new ScrollPane(group);
		VBox.setVgrow(schemaScrollPane, Priority.ALWAYS);
		
//		schemaScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
//		schemaScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		schemaPane.setScaleX(1);
		schemaPane.setScaleY(1);
		 schemaPane.setOpacity(0);

		schemaPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
			 if (newScene != null) {
//				 schemaPane.setOpacity(0.2);
				 FadeTransition ft = new FadeTransition(Duration.millis(1000), schemaPane);
				 ft.setFromValue(0.0);
				 ft.setToValue(1.0);
				 ft.play();
			 }
		});

		schemaScrollPane.setMinHeight(0);
		schemaScrollPane.setPannable(false);
		
		schemaScrollPane.addEventFilter(InputEvent.ANY, (event)-> {
		    if (event.getEventType().toString() == "SCROLL")
		        event.consume();
		});
		
		schemaScrollPane.setFitToHeight(true);
		schemaScrollPane.setFitToWidth(true);
		schemaScrollPane.setOnMouseClicked(e -> {
			if(nFile.getActivityMode() != ActivityMode.CONFIGURE) {//
				nFile.getActivity().closeActivity();
				nFile.setActivityMode(ActivityMode.SELECT);
				napp.sumLabel.clear();
				napp.countLabel.clear();
				napp.rowsCount.clear();
				nFile.infoPaneManager.deactivate();
			}else if(nFile.getActivityMode() == ActivityMode.CONFIGURE) {
				Configure conf  = 	(Configure) nFile.getActivity();
				conf.clearSelection();
			}
		}); 
		this.schemaName = schema;
		napp.getDBManager().getActiveConnection().getXMLBase().getXTables().filtered( t -> t.getSchema().equals(schemaName)).forEach(table -> {		
			this.getMapNodes().put(table.getTable(), new Nnode(this,null, table));
		});
		this.getMapNodes().forEach((k,nnode) -> nnode.addRootLines());
		this.getMapNodes().forEach((k,nnode) -> this.add(nnode));		
		this.addFunctions();
	
		schemaScrollPane.setOnZoom( zz ->{
			 schemaPane.setScaleX(schemaPane.getScaleX() * zz.getZoomFactor());
			 schemaPane.setScaleY(schemaPane.getScaleY() * zz.getZoomFactor());
		 });
		
		schemaScrollPane.setFocusTraversable(false);		
	}
	
	public void createRootLines() {
		 this.getMapNodes().forEach((k,nnode) -> nnode.addRootLines());
	}
	
	private void addFunctions() {
		napp.getDBManager().getActiveConnection().getXMLBase().getXFunctions().forEach(f -> {
			dbFunctionsMap.put(f.getLabel(), f);
		});		
	}

	public LinkedHashMap<String, NFunction> getDbFunctionsMap() {
		return dbFunctionsMap;
	}

//	private DAO newDAO(NTableBO nTableBO) {
//		String shema = nTableBO.getSchema_name();
//		String table = nTableBO.getTable_name();
//		String db = nTableBO.getDb();
//		try {
//			DAO dao = (DAO) Class.forName("rakhuba.dbs."+ db + "." + shema + "." + table.replaceAll(" ", "_") + "DAO").getDeclaredConstructor(Napp.class).newInstance(napp);			
//			return dao;
//		} 
//		catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException  |InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//			return null;
//		} 
//	}
	
	public Nnode getNnode(String tableName) {
		Nnode nnode =  getMapNodes().get(tableName);		
		return nnode;
	}
	
	public void clear(){
		if(this.nFile.getActivityMode() != ActivityMode.CONFIGURE) {
			nFile.getActivity().closeActivity();
			nFile.setActivityMode(ActivityMode.SELECT);
			this.getMapNodes().forEach((k,nnode) -> nnode.clear());		
			nFile.clear();
			napp.getSearch().clear();
			nFile.infoPaneManager.deactivate();			
			napp.sumLabel.clear();
			napp.countLabel.clear();
			napp.rowsCount.clear();
		}
	}
	
	public void saveNnodeCoordinates(){
		this.getMapNodes().forEach((k,v) -> v.saveLayoutLocation());
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public ArrayList<NTable> getSortedTablesList(){
		ArrayList<NTable> sortedTables = new ArrayList<NTable>(napp.getDBManager().getActiveConnection().getXMLBase().getXTables().filtered(t -> t.getSchema().equals(schemaName)));
		Collections.sort(sortedTables, new Comparator<NTable>() {
		    @Override
		    public int compare(NTable colA, NTable colB){
		    	return  colA.getTable().compareTo(colB.getTable()); 
		    } 
		});
		return sortedTables;
	}
	
	public boolean contains(javafx.scene.Node node) {
		return schemaPane.getChildren().contains(node);
	}
	
	public void add(javafx.scene.Node node) {
		schemaPane.getChildren().add(node);
	}
	
	public void remove(javafx.scene.Node node) {
		schemaPane.getChildren().remove(node);
	}

	public HashMap<String, Nnode> getMapNodes() {
		return mapNodes;
	}
	
	public TreeSet<String> getTablesList(){
		return new TreeSet<String>(mapNodes.keySet());
	}

	//SAVE •••••••••••••••••••••••••••••••••••
	public void save(Document document, Element schemasE) {
		Element schemaE = document.createElement("schema");
        schemaE.setAttribute("schemaName", this.getSchemaName());
        schemasE.appendChild(schemaE);
        this.getMapNodes().forEach((k, nnode) -> nnode.saveXml(document, schemaE));
	}
	
	public void loopA(OpenContext context, Node xmap) {
		XML.children(xmap).forEach(n ->{
			if(n.getNodeName().equals("nnode")) {
				Nnode nnode = this.getNnode(XML.atr(n, "tableName"));
				if(nnode != null && nnode.isSelectable()) {
					nnode.loopA(context, n);
				} else {
					this.getNFile().getMessages().add(new Message(this.getNFile(), "table is not in database or no access to: ", "Nnode: " + XML.atr(n, "tableName")));
				}
			}
		});						
	}
	
	public void loopB(OpenContext context, Node xmap) {
		XML.children(xmap).forEach(n ->{
			if(n.getNodeName().equals("nnode")) {
				XML.children(n).forEach(nn ->{
					if(nn.getNodeName().equals("layer")) {
						//MOVE TO NNODE
						LAY lay = context.getAliaseLAYs().get(XML.atr(nn, "aliase"));
						if(lay != null) { //Should be null only when db was modefied
							lay.loopB(context, nn);
							lay.refreshFilterIndicator();
							if((lay.isRoot()) && lay.getSelectedFields().size() > 0) {
								lay.getIndicators().fieldsOn();
							}
							lay.getChildJoins().forEach(line -> {
								if(line.getToLay().nnode.getSchema().equals(lay.nnode.getSchema())) {
									int index = schemaPane.getChildren().indexOf(line.getToLay().getPane());
									int index2 = schemaPane.getChildren().indexOf(lay.getPane());
									schemaPane.getChildren().add(Math.min(index, index2) -1, line.getCubicCurve());
								}
							});
							
							if(XML.atr(nn, "sqlType").equals(SqlType.SQLD.toString())) {
								LAY parent = context.getAliaseLAYs().get(XML.atr(nn, "parentLay"));
//								//DLine-------
								JoinLine joinLine = new JoinLine(lay, parent, JoinType.DLINE);
								((DLayer)lay).setJoinLine(joinLine);
								int index = lay.nnode.nmap.schemaPane.getChildren().indexOf(lay.getPane());
								int index2 = lay.nnode.nmap.schemaPane.getChildren().indexOf(parent.getPane());
								lay.nnode.nmap.schemaPane.getChildren().add(Math.min(index, index2) -1, joinLine.getCubicCurve());
							}
						}else {
							this.getNFile().getMessages().add(new Message(this.getNFile(), "missing", "LAY: " + XML.atr(nn, "aliase")));
						}
					}
				});
			}
		});
		
		//after B
		this.mapNodes.forEach((nm,nd)-> {
			nd.getLayers().forEach(lay -> {
				if (lay.getPopulation().getValue() == Population.POPULATED) {
					lay.createColumns();
					nFile.gridManager.selectTab(lay.getSheet());
				}
			});
		});
		nFile.infoPaneManager.deactivate();			
	}

	public NFile getNFile() {
		return nFile;
	}

	public void setCompactView(boolean b) {
		this.mapNodes.forEach((nm, nd)-> {
			nd.setCompactView(b);
		});
	}	
}
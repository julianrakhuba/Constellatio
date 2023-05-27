package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import clientcomponents.NColumn;
import clientcomponents.NTable;
import file.OpenContext;
import generic.DAO;
import generic.DLayer;
import generic.LAY;
import generic.OpenDAO;
import generic.SLayer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.SQL;
//import rakhuba.application.JoinLine;
//import rakhuba.application.Nmap;
//import rakhuba.application.Nnode;
//import rakhuba.application.NnodeLine;
//import rakhuba.application.XML;
import sidePanel.Message;
import status.ActivityMode;
import status.Population;
import status.SqlType;
import status.Visability;

public class Nnode extends Pane {
	private double uzelX = 0;
	private double uzelY = 0;
	private double mouseEventX = 0;
	private double mouseEventY = 0;	
	private DAO dao;
	private OpenDAO openDAO = new OpenDAO(this);
	public StackPane rootStackPane;
	public Nmap nmap;
	
	private NTable tableBO;
	private TreeSet<String> columnsList;
	private ArrayList<NColumn> columns;
	private HashMap<String, ArrayList<String>> distinctSearchList;
	private Text text = new Text();
	public Timeline timeline = new Timeline();
	private HashMap<Nnode, NnodeLine> rootLines = new HashMap<Nnode, NnodeLine>();
	private ArrayList<LAY> layers = new ArrayList<LAY>();
	private Tooltip toolTip;
	
	public Nnode(Nmap nmap, DAO dao, NTable tableBO) {		
		this.tableBO = tableBO;
		this.nmap = nmap;
		this.dao = dao;
		this.setLayoutX(tableBO.getX().doubleValue());
		this.setLayoutY(tableBO.getY().doubleValue());		
		
		//NNODE MOVEMENT
		this.setOnMousePressed(e -> {
			if (nmap.getNFile().getActivityMode() == ActivityMode.CONFIGURE) {
				mouseEventX = e.getSceneX();
				mouseEventY = e.getSceneY();
				uzelX = getLayoutX();
				uzelY = getLayoutY();
			}
		});

		this.setOnMouseDragged(e -> {
			if (nmap.getNFile().getActivityMode() == ActivityMode.CONFIGURE) {
				double offsetX = e.getSceneX() - mouseEventX;
				double offsetY = e.getSceneY() - mouseEventY;
				uzelX += offsetX;
				uzelY += offsetY;
				setLayoutX(uzelX);
				setLayoutY(uzelY);
				mouseEventX = e.getSceneX();
				mouseEventY = e.getSceneY();
				rootLines.forEach((k, line) ->  line.updateLayout());
				layers.forEach(layer -> {
					layer.getParentJoins().forEach(line -> line.updateLayout());
					layer.getChildJoins().forEach(line -> line.updateLayout());
				});
				e.consume();
			}
		});

		this.createRootLayer();		
	}
	
	public DAO getDAO() {
		return dao;
	}
	
	public void updateRootLines() {
		rootLines.forEach((k, line) ->  line.updateLayout());
	}
	
	public void overlapLayers() {
		if(!this.nmap.napp.getMenu().getViewMenu().getAutoFoldMenuItem().isSelected()) {
			if(!nmap.napp.getNscene().getHoldKeys().contains("SHIFT")){//MOVE THIS MORE OUT????
				if(layers.size()>1) {
					this.moveLayers(Duration.millis(500), Duration.millis(800), false);
				}
			}
		}
	}
	
	public void separateLayers() {
		if(!nmap.napp.getNscene().getHoldKeys().contains("SHIFT")){
			if(layers.size()>1 ) {
				this.moveLayers(Duration.millis(100), Duration.millis(100), true);
			}
		}
	}
	
	//Must be done after node creation
	public String getNameLabel() {
		return tableBO.getTable().toLowerCase();
	}	

	
	public void createRootLayer() {
		text.setText(this.getNameLabel());
		text.setStyle(" -fx-font: 9px Verdana;");
		text.setFill(Color.rgb(100,100,100));
		
		rootStackPane = new StackPane();
		rootStackPane.setPrefWidth(20);
		rootStackPane.setPrefHeight(20);

		toolTip = new Tooltip(this.getNameLabel());
		toolTip.setStyle("-fx-font-size: 9");

		this.setCompactView(!nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected());
		this.styleGray();
		rootStackPane.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.PRIMARY)) {
				nmap.getNFile().getActivity().passNnode(this, e);
			}
			e.consume();
		});
		this.getChildren().add(rootStackPane);
		rootStackPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
			 if (newScene != null) {
			 	Platform.runLater(() -> {
				this.updateRootLines();
					layers.forEach(layer -> {
						layer.getChildJoins().forEach(line -> {
							line.updateLayout();
							line.updateStyle();
						});
						if(layer instanceof DLayer) {
							((DLayer)layer).getJoinLine().updateLayout();
							((DLayer)layer).getJoinLine().updateStyle();
						}
					}); 
			 	});
		    }
		});
		
		rootStackPane.setOnContextMenuRequested(e ->{
			if(nmap.getNFile().getActivityMode() == ActivityMode.CONFIGURE) {
				this.nmap.napp.funcMenuClick(rootStackPane);			
			}
		});
	}
	
	public boolean isSchemaJoin() {
		return
		(nmap.napp.getDBManager().getActiveConnection().getXMLBase().getKeys().filtered(k -> 
		k.getConst().equalsIgnoreCase("FOREIGN KEY") &&		
		k.getSchema().equalsIgnoreCase(this.getSchema()) &&
		k.getTable().equalsIgnoreCase(this.getTable()) && !this.getSchema().equalsIgnoreCase(k.getRSchema())
		).size() + 
		nmap.napp.getDBManager().getActiveConnection().getXMLBase().getKeys().filtered(k -> 
		k.getConst().equalsIgnoreCase("FOREIGN KEY") &&		
		k.getRSchema().equalsIgnoreCase(this.getSchema()) &&
		k.getRTable().equalsIgnoreCase(this.getTable())	&& !this.getSchema().equalsIgnoreCase(k.getSchema())	
		).size()) >0;
	}
	

	
	public void styleOrange() {
		rootStackPane.getStyleClass().clear();
		rootStackPane.getStyleClass().add("configNnode");
	}
	
	public void addRootLines() {
		nmap.napp.getDBManager().getActiveConnection().getXMLBase().getKeys().filtered(k -> 
//		k.getRSchema() != null && // ref not null
		k.getConst().equals("FOREIGN KEY")	&&	
		k.getSchema().equals(this.getSchema()) //schema
		&& k.getTable().equals(this.getTable()) //table
		&& k.getRSchema().equals(this.getSchema())// reff is local schema
		).forEach(key -> {
			Nnode nnode =  nmap.getNnode(key.getRTable());
			if(!rootLines.containsKey(nnode) && !nnode.rootLines.containsKey(this)) {//this will add only one root line per node
				NnodeLine line = new NnodeLine(this, nnode);
				this.rootLines.put(nnode, line);
				nnode.rootLines.put(this, line);
				nmap.add(line);
			}
		});
	}
    
	public ArrayList<LAY> getLayers() {
		return layers;
	}
	
	public void saveLayoutLocation() {
		tableBO.setX(this.getLayoutX());
		tableBO.setY(this.getLayoutY());
	}
	
	public String getSchema() {
		return tableBO.getSchema();
	}
	
	public String getTable() {
		return tableBO.getTable();
	}
	
	public String getTableNameWUnderScr() {
		return tableBO.getTable().replaceAll(" ", "_") ;
	}
	
	public String getSchemaNameWUnderScr() {
		return tableBO.getSchema().replaceAll(" ", "_") ;
	}

	public String getFullNameWithOptionalQuotes() {
		return qt(tableBO.getSchema()) + "." + qt(tableBO.getTable());
	}
	
	private String qt(String str) {
		return str.contains(" ")? "`" + str + "`": str;
	}
	
	public HashMap<Nnode, NnodeLine> getRootLines() {
		return rootLines;
	}
	
	public void clear() {
		layers.forEach(layer -> layer.clear());
		layers.clear();
	}
	
//	public void relocateDirectConnectionsAround() {
//		
//		Nnode[] cons = (Nnode[]) foreignKeyNodes.toArray();
//		
//		//Nnode[] cons = foreignKeyNodes.values().toArray(new Nnode[foreignKeyNodes.values().size()]);
//
//		double slice = 2 * Math.PI / cons.length;
//		double radius = 200;
//		for (int i = 0; i < cons.length; i++) {
//			double angle = slice * i;
//			Nnode connectedGnode = cons[i];
//			connectedGnode.setLayoutX(this.propertyX.get() + radius * Math.cos(angle));
//			connectedGnode.setLayoutY(this.propertyY.get() + radius * Math.sin(angle));
//		}
//	}
//	public void relocateDirectConnectionsAround(){
//		Nnode[] cons = connections.values().toArray(new Nnode[connections.values().size()]);
//		double slice = 2 * Math.PI / cons.length;
//		double radius = 200;
//		for (int i = 0; i < cons.length; i++) {
//			double angle = slice * i;
//			Nnode connectedGnode = cons[i];
//			// connectedUzel.moveSlowlyToNewLocation(this.propertyX.get() + radius * Math.cos(angle), this.propertyY.get() + radius * Math.sin(angle));
//			connectedGnode.setLayoutX(this.propertyX.get() + radius * Math.cos(angle));
//			connectedGnode.setLayoutY(this.propertyY.get() + radius * Math.sin(angle));
//			//connectedGnode.setLayoutX(this.propertyX.get() + (i * 0.5));
//			//connectedGnode.setLayoutY(this.propertyY.get() + (i * 0.5));
//			connectedGnode.shaker.shake();
//		}
//	}
	
	public void moveLayers(Duration durA, Duration durB, boolean expanded) {
		timeline.getKeyFrames().clear();
		layers.forEach(layer -> {
			double selfJoinFix = expanded ? 0 : 10;//fix for self join, affects all lines
			//LAY
			timeline.getKeyFrames().add(new KeyFrame(durA,new KeyValue(layer.getPane().layoutYProperty(), layer.toY(expanded), Interpolator.EASE_BOTH)));

			//line
			layer.getParentJoins().forEach(line -> {
				if(line != null) {
					boolean sameNnode = line.getFromLay().nnode == line.getToLay().nnode;
					double lineToY = layer.toY(expanded)  + (layer.nnode.rootStackPane.getHeight()/2);
					timeline.getKeyFrames().addAll(line.yProperty2(layer, lineToY, durA));
					timeline.getKeyFrames().addAll(line.yControl(layer, lineToY + ((sameNnode) ? selfJoinFix : 0), durB));
				}					
			});
			
			layer.getChildJoins().forEach(line -> {
				if(line != null) {
					boolean sameNnode = line.getFromLay().nnode == line.getToLay().nnode;
					double lineToY = layer.toY(expanded)  + (layer.nnode.rootStackPane.getHeight()/2);
					timeline.getKeyFrames().addAll(line.yProperty2(layer, lineToY, durA));							
					timeline.getKeyFrames().addAll(line.yControl(layer, lineToY - ((sameNnode) ? selfJoinFix : 0), durB));
				}
			});
			
			if(layer instanceof DLayer) {
				JoinLine line =  ((DLayer)layer).getJoinLine();
				boolean sameNnode = line.getFromLay().nnode == line.getToLay().nnode;

				double lineToY = layer.toY(expanded)  + (layer.nnode.rootStackPane.getHeight()/2);
				timeline.getKeyFrames().addAll(line.yProperty2(layer, lineToY, durA));
				timeline.getKeyFrames().addAll(line.yControl(layer, lineToY + ((sameNnode) ? selfJoinFix : 0), durB));
				
				double lineToYz = line.getToLay().toY(expanded)  + (line.getToLay().nnode.rootStackPane.getHeight()/2);
				timeline.getKeyFrames().addAll(line.yProperty2(line.getToLay(), lineToYz, durA));
				timeline.getKeyFrames().addAll(line.yControl(line.getToLay(), lineToYz - ((sameNnode) ? selfJoinFix : 0), durB));						
			}					
		});
		
		timeline.setCycleCount(1);
		timeline.playFromStart();
	}
	
	public double getCenterX(){
		return this.getLayoutX() + (rootStackPane.getWidth()/2);
	}
	
	public double getCenterY(){
		return this.getLayoutY() + (rootStackPane.getHeight()/2);
	}
	
	public TreeSet<String> getColumnsList(){
		if (columnsList == null) {
			columnsList = new TreeSet<String>();
			nmap.napp.getDBManager().getActiveConnection().getXMLBase().getXColumns().filtered(c ->  (c.getSchema().equals(tableBO.getSchema()) && c.getTable().equals(tableBO.getTable()))).forEach(col -> columnsList.add(col.getColumn()));
		}
		return columnsList;
	}
	
	public ArrayList<NColumn> getColumns(){
		if (columns == null) {
			columns = new ArrayList<NColumn>();
			nmap.napp.getDBManager().getActiveConnection().getXMLBase().getXColumns().filtered(c ->  (c.getSchema().equals(tableBO.getSchema()) && c.getTable().equals(tableBO.getTable()))).forEach(col -> columns.add(col));
		}
		return columns;
	}
	
	public ArrayList<String> getStaticValuesList(String column){
		if(distinctSearchList == null) distinctSearchList = new HashMap<String, ArrayList<String>>();
		if(!distinctSearchList.containsKey(column)) {
			String nameWQuotes =  this.getFullNameWithOptionalQuotes();
			SQL sql = new SQL().SELECT().DISTINCT(nameWQuotes + "." +  column + " FROM " + nameWQuotes + " ORDER BY " + nameWQuotes + "." +  column);
			distinctSearchList.put(column, this.getOpenDAO().readDistinctValues(sql));
		}
		return distinctSearchList.get(column);
	}
	
	public void add(LAY lay) {
		layers.add(lay);
	}
	
	public void remove(LAY lay) {
		layers.remove(lay);
	} 

 	public void saveXml(Document doc, Element schema){
 		if(layers.size()>0) {
 			Element eNnode = doc.createElement("nnode");
 	 		eNnode.setAttribute("tableName", this.getTable());
 	 		schema.appendChild(eNnode);
 			layers.forEach(lay -> {
 				lay.saveXml(doc, eNnode);
 			});
 		}
 	}

	public void loopA(OpenContext context, Node xNode) {
		XML.children(xNode).forEach(n ->{
			if(n.getNodeName().equals("layer")) {
				String aliase = XML.atr(n, "aliase");
				String sqlType = XML.atr(n, "sqlType");
				String population = XML.atr(n, "population");
				
				if(sqlType.equals(SqlType.SQL.toString()) || sqlType.equals(SqlType.SQLJ.toString())) {
					SLayer lay = new SLayer(this, SqlType.valueOf(sqlType));
					lay.getPopulation().setValue(Population.valueOf(population));
					lay.updateColorMode();
					context.getAliaseLAYs().put(aliase, lay);
					lay.loopA(context, n);
					lay.addToMap();	
				}else if(sqlType.equals(SqlType.SQLD.toString())) {
					LAY parent = context.getAliaseLAYs().get(XML.atr(n, "parentLay"));
					DLayer lay = new DLayer(this, parent);
					lay.getPopulation().setValue(Population.valueOf(population));
					lay.updateColorMode();
					context.getAliaseLAYs().put(aliase, lay);
					lay.loopA(context, n);
					lay.addToMap();	
				}
			}
		});
	}

	public OpenDAO getOpenDAO() {
		return openDAO;
	}
	
	public void setCompactView(boolean b) {
		if(b) {
			Tooltip.uninstall(rootStackPane, toolTip);
			rootStackPane.getChildren().add(text);
		}else {			
			Tooltip.install(rootStackPane, toolTip);
			rootStackPane.getChildren().remove(text);
		}
		
		layers.forEach(lay ->{
			lay.setCompactView(b);
		});
	}
	
	
	public boolean isSelectable() {
		if(tableBO.getVisability() == Visability.VISIBLE) {
			return true;
		}else {			
			nmap.getNFile().getMessages().add(new Message(nmap.getNFile(), "Table Access", "Can't create layer for hidden table " + this.getTable()));
			return false;
		}
	}
	
	public void styleGray() {
		rootStackPane.getStyleClass().clear();		
		if(this.isSchemaJoin()) {
			rootStackPane.getStyleClass().add("unpopulatedNnodeWithSchemaJoins");
		}else {
			rootStackPane.getStyleClass().add("unpopulatedNnode");
		}
	}

	public void styleHighlighted() {
		rootStackPane.getStyleClass().add("unpopulatedNnodeWithSchemaJoins");
	}

}

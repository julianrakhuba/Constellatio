package generic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import activity.Select;
import application.Indicators;
import application.JoinLine;
import application.Nnode;
import application.XML;
import clientcomponents.NKey;
import elements.ELM;
import file.OpenContext;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Field;
import logic.FormulaField;
import logic.Group;
import logic.Join;
import logic.Level;
import logic.Logic;
import logic.SQL;
import logic.SearchCON;
import pivot.FieldMenu;
import pivot.LayerMenu;
import pivot.NSelector;
import pivot.PivotColumn;
import sidePanel.HeaderLabel;
import sidePanel.Message;
import status.ActivityMode;
import status.ColorMode;
import status.JoinType;
import status.LayerMode;
import status.Population;
import status.Selection;
import status.SqlType;
import status.VersionType;

public abstract class LAY {
	public Nnode nnode;
	private NSheet sheet;
	private ObservableList<JoinLine> parentJoins = FXCollections.observableArrayList();
	private ObservableList<JoinLine> childJoins = FXCollections.observableArrayList();
	
	private VBox layPane;
	private Text text = new Text();
	public NSelector viewLabel;

	private Property<Population> population = new SimpleObjectProperty<Population>(Population.UNPOPULATED); 
	protected Property<SqlType> sqlType = new SimpleObjectProperty<SqlType>(); //Blue Violet
	private Property<Selection> selection = new SimpleObjectProperty<Selection>(Selection.UNSELECTED); //light,medium, dark
	private Property<LayerMode> mode = new SimpleObjectProperty<LayerMode>(LayerMode.BASE); //base, green, pink
	private Property<ColorMode> colorMode = new SimpleObjectProperty<ColorMode>();
	private Indicators indicators = new Indicators();
	private int largeGap = 21;
	private int smallGap = 1;
	private ObservableList<OpenBO> items;
	private DLayer childDLayer;
	private HBox extNnodesHbox = new HBox();
	//should custom fields and selectedFields be in same list?, or
	private ObservableList<Field> fields = FXCollections.observableArrayList();	
	private ObservableList<FormulaField> formulaFields = FXCollections.observableArrayList();	
	private ObservableList<Field> selectedFields = FXCollections.observableArrayList();
	
	private ObservableList<PivotColumn> versions = FXCollections.observableArrayList();
	private ObservableList<SearchCON> searchCONsList = FXCollections.observableArrayList();
	
	private VBox searchListHBox = new VBox(10);
	private VBox formulasListHBox = new VBox(10);
	private VBox joinsListHBox = new VBox(10);
	
	private ObservableList<Region> searchRegion = FXCollections.observableArrayList();
	private ObservableList<Region> optionsRegion = FXCollections.observableArrayList();

	private Timeline dotTimeline = new Timeline();
	private Logic logic = new Logic(this);
	private Level rootLevel;
	private Tooltip toolTip;
	private LayerMenu layerMenu;
	private AnimatedStyler  styler;
	
	private Pane searchLabel = new HeaderLabel("conditions");
	private Pane functionLabel = new HeaderLabel("functions");
	private Pane joinLabel = new HeaderLabel("joins");
	private Pane optionsLabel = new HeaderLabel("options");
	private Pane sideHeader;
			
			
	public NSelector rollup = new NSelector("rollup", true);
	public NSelector orderby = new NSelector("orderby", true);

	public LAY(Nnode nnode, SqlType sqtp) {
		sqlType.setValue(sqtp);
		this.nnode = nnode;
		nnode.add(this);
		viewLabel = new NSelector();

		population.addListener((a,b,c) -> this.updateColorMode());
		selection.addListener((a,b,c) -> this.updateColorMode());
		mode.addListener((a,b,c) -> this.updateColorMode());

		rollup.getPane().setOnMouseClicked(e-> {
			if(!rollup.getValue()) {
				rollup.set(true);
				orderby.set(false);
		    }else {
				rollup.set(false);
			}
		});
		
		orderby.getPane().setOnMouseClicked(e-> {
			if(!orderby.getValue()) {
				orderby.set(true);
				rollup.set(false);
		    }else {
		    	orderby.set(false);
			}
		});

		//
		sideHeader = new HeaderLabel(nnode.getTable(),"#ade0ff");

		searchRegion.addAll(sideHeader,searchLabel, searchListHBox, joinLabel, joinsListHBox,  functionLabel, formulasListHBox);
		optionsRegion.addAll(optionsLabel, rollup.getLabel(), orderby.getLabel());

		rootLevel = new Level(this, null);
		sheet = new NSheet(this);
		layPane = new VBox(indicators.getRoot());
		layPane.setPrefWidth(20);
		layPane.setPrefHeight(20);
		layPane.setAlignment(Pos.BOTTOM_LEFT);
		layPane.setOnMouseEntered(e -> {
			int inx = nnode.getLayers().indexOf(this);
			if(!nnode.nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected()) {
				toolTip.setText(this.getAliase());
			}else {
				toolTip.setText(nnode.getTableNameWUnderScr()  + ((inx >0)? " " + inx  : ""));
			}
			nnode.separateLayers();
		});
		layPane.setOnMouseExited(e -> nnode.overlapLayers());
		layPane.setOnMousePressed(e -> e.consume());
		layPane.setOnMouseReleased(e -> e.consume());
		layPane.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.PRIMARY)) {//MOVE this to ACTIVITY ???
				if (e.isShiftDown()) {
					nnode.nmap.getNFile().getActivity().passNnode(nnode, e);
				}else {
//					this.TEST_CLICK();
					nnode.nmap.getNFile().getActivity().passLAY(this);
				}
			}else if(e.getButton().equals(MouseButton.SECONDARY) && !e.isControlDown()){
				
				if(nnode.nmap.getNFile().getActivityMode() == ActivityMode.SELECT  && this.isRoot()) {
					System.out.println("right click: " + this.getAliase());
					nnode.nmap.getNFile().getActivity().closeActivity();
					nnode.nmap.getNFile().setActivityMode(ActivityMode.VIEW);
					nnode.nmap.getNFile().getActivity().passLAY(this);
					this.nnode.nmap.napp.getUpperPane().funcMenuClick(layPane);
				}else if(nnode.nmap.getNFile().getActivityMode() == ActivityMode.VIEW) {
					nnode.nmap.getNFile().getActivity().closeActivity();
					nnode.nmap.getNFile().setActivityMode(ActivityMode.SELECT);
					nnode.nmap.getNFile().getActivity().passLAY(this);
					this.nnode.nmap.napp.getUpperPane().funcMenuClick(layPane);
				}else {
					if(nnode.nmap.getNFile().getActivity().getActiveLayer() == this) {
						this.nnode.nmap.napp.getUpperPane().funcMenuClick(layPane);
					}
				}
				
			}
			e.consume();
		});	
		

		text.setText(nnode.getNameLabel());
		text.setStyle(" -fx-font: 9px Verdana;");
		text.setFill(Color.rgb(100,100,100));

		toolTip = new Tooltip(this.getAliase());
		toolTip.setStyle("-fx-font-size: 9");

		toolTip.setShowDelay(Duration.millis(200));
//		toolTip.setHideDelay(Duration.millis(0));

		
		
		Tooltip.install(layPane, toolTip);
		this.setCompactView(nnode.nmap.napp.getMenu().getViewMenu().getSimpleViewMenuItem().isSelected());
		
		layPane.setLayoutX(nnode.getLayoutX());
		layPane.setLayoutY((nnode.getLayers().indexOf(this)) * smallGap + nnode.getLayoutY());
		layPane.getStyleClass().addAll("layBase");
		
		
		
		colorMode.addListener((a,b,c) -> this.getStyler().updateLayStyle(c));
				
		parentJoins.addListener((ListChangeListener<? super JoinLine>) (c) -> {
			if(c.next()) {
				c.getAddedSubList().forEach(jl -> {
					joinsListHBox.getChildren().add(jl.parentLabel);
				});
				c.getRemoved().forEach(jl -> {
					joinsListHBox.getChildren().remove(jl.parentLabel);
				});
			}
//			joinsListHBox.getChildren().sort((a,b) -> ((JainLabel) b).getRelationship().compareTo(((JainLabel) a).getRelationship()));
//			combinedJoins.sort((a,b) -> b.getJoinRelationship().compareTo(a.getJoinRelationship()));			
		});
		
		childJoins.addListener((ListChangeListener<? super JoinLine>) (c) -> {
			if(c.next()) {
				c.getAddedSubList().forEach(jl -> {
//					combinedJoins.add(new JoinRelationship(jl, "child"));
					joinsListHBox.getChildren().add(jl.childLabel);
				});
				c.getRemoved().forEach(jl -> {
					joinsListHBox.getChildren().remove(jl.childLabel);
//					combinedJoins.removeIf(jg -> jg.getJoinLine() == jl);
				});
			}
//			joinsListHBox.getChildren().sort((a,b) -> ((JainLabel) b).getRelationship().compareTo(((JainLabel) a).getRelationship()));
//			combinedJoins.sort((a,b) -> b.getJoinRelationship().compareTo(a.getJoinRelationship()));
		});
		
		searchCONsList.addListener((ListChangeListener<? super SearchCON>) (c) -> {
			if(c.next()) {
				c.getAddedSubList().forEach(jl -> {
					searchListHBox.getChildren().add(jl.getLabel());
				});
				c.getRemoved().forEach(jl -> {
					searchListHBox.getChildren().remove(jl.getLabel());
				});
			}
		});
		
		formulaFields.addListener((ListChangeListener<? super FormulaField>) (c) -> {
			if(c.next()) {
				c.getAddedSubList().forEach(jl -> {
					formulasListHBox.getChildren().add(jl.getLabel());
				});
				c.getRemoved().forEach(jl -> {
					formulasListHBox.getChildren().remove(jl.getLabel());
				});
			}
		});
	}
	
	//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public void TEST_CLICK() {
		System.out.println("[TEST_CLICK] "+ this.getAliase());
	}
	//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••

	public void setCompactView(boolean b) {
		if(b) {
			layPane.getChildren().remove(text);
		}else {
			layPane.getChildren().add(0, text);

		}
	}

	public DLayer getChildDLayer() {
		return childDLayer;
	}
	
	public void setChildDLayer(DLayer childDLayer) {
		this.childDLayer = childDLayer;
	}
	
	public double getCenterX(){
		return  layPane.getLayoutX()  + (nnode.rootStackPane.getWidth()/2);
	}
	
	public double getCenterY(){
		return  layPane.getLayoutY()  + (nnode.rootStackPane.getHeight()/2);
	}
	
	public double toX(boolean expanded) {
		if(expanded) return nnode.getLayers().indexOf(this) * largeGap + nnode.getLayoutX();
		return nnode.getLayers().indexOf(this) * smallGap + nnode.getLayoutX();
	}
	
	public double toY(boolean expanded) {
		if(expanded) return nnode.getLayers().indexOf(this) * largeGap + nnode.getLayoutY();
		return nnode.getLayers().indexOf(this) * smallGap + nnode.getLayoutY();
	}
	
	public SqlType getSqlType() {
		return sqlType.getValue();
	}
	
	public Indicators getIndicators() {
		return indicators;
	}
	
	public void setSelection(Selection sl) {
		selection.setValue(sl);
	}
	
	public void setMode(LayerMode md) {
		mode.setValue(md);
	}
		
	public NSheet getSheet() {
		return sheet;
	}
	
	public Property<Population> getPopulation() {
		return population;
	}

	public void addToMap() {
		if(!nnode.nmap.contains(layPane)) nnode.nmap.add(layPane);		
	}
	
	public String getAliase() {
		return nnode.getSchemaNameWUnderScr() + "_" + nnode.getTableNameWUnderScr() + "_" + nnode.getLayers().indexOf(this);
	}
	
	public void updateColorMode() {
		if(mode.getValue() == LayerMode.EDIT) {
			colorMode.setValue(ColorMode.EDIT);
		}else if(mode.getValue() == LayerMode.VIEW) {
			colorMode.setValue(ColorMode.VIEW);
		}else if(mode.getValue() == LayerMode.FORMULA) {
			colorMode.setValue(ColorMode.FORMULA);
		}else if(mode.getValue() == LayerMode.BASE && this.getSqlType() == SqlType.SQL) {
			if(selection.getValue() == Selection.SELECTED) {
				colorMode.setValue(ColorMode.SQLSELECTED);
			}else {
				colorMode.setValue(ColorMode.SQL);
			}
		}else if(mode.getValue() == LayerMode.BASE && this.getSqlType() == SqlType.SQLJ) {
			if(selection.getValue() == Selection.SELECTED) {
				colorMode.setValue(ColorMode.SQLJSELECTED);
			}else {
				colorMode.setValue(ColorMode.SQLJ);
			}
		}else if(mode.getValue() == LayerMode.BASE && this.getSqlType() == SqlType.SQLD) {
			if(selection.getValue() == Selection.SELECTED) {
				colorMode.setValue(ColorMode.SQLDSELECTED);
			}else {
				colorMode.setValue(ColorMode.SQLD);
			}
		}
	}
	
	public ColorMode getColorMode() {
		return colorMode.getValue();
	}
	
	public ObservableList<OpenBO> getItems() {
		return items;
	}
	
	public void setItems(ObservableList<OpenBO> items) {
		 this.items = items;
	}
	
	public Label getViewLabel() {
		viewLabel.getLabel().setText(this.getIndexLabel());
		return viewLabel.getLabel();
	}
	
	public String getIndexLabel() {
		if(nnode.getLayers().stream().filter(e -> e.getSqlType() == SqlType.SQLJ).count()>1) {
			return nnode.getTable() + " (" + nnode.getLayers().indexOf(this) + ")";
		}else {
			return nnode.getTable();
		}		
	}
	
	public ObservableList<JoinLine> getChildJoins() {
		return childJoins;
	}
	
	public ObservableList<JoinLine> getParentJoins() {
		return parentJoins;
	}
	
	public ArrayList<LAY> getChildren() {
		 ArrayList<LAY> ret = new  ArrayList<LAY>();
		 childJoins.forEach(ln -> ret.add(ln.getToLay()));
		return ret;
	}
	
	public ArrayList<LAY> getParents() {
		 ArrayList<LAY> ret = new  ArrayList<LAY>();
		 parentJoins.forEach(ln -> ret.add(ln.getFromLay()));
		return ret;
	}

	public ArrayList<LAY> getFullChildren() {
		ArrayList<LAY> lays = new ArrayList<LAY>();
		childJoins.forEach(line -> {
			lays.add(line.getToLay());
			lays.addAll(line.getToLay().getFullChildren());
		});
		return lays;
	}

	public void setLabelBold() {
		viewLabel.set(true);
//		viewMenuLab.setStyle("-fx-font-weight: bold;");
	}
	
	public void setLabelNormal() {
		viewLabel.set(false);
//		viewMenuLab.setStyle("-fx-font-weight: normal;");
	}
	
	public void refreshFilterIndicator() {		
		if(this.getLogicConditions().stream().filter(con -> con.getRemoteLay() == null).count()>0) {
			indicators.conditionsOn();
		}else {
			indicators.conditionsOff();
		}
	}
	
	public void clear() {
		parentJoins.forEach(line -> nnode.nmap.remove(line.getCubicCurve()));
		childJoins.forEach(line -> nnode.nmap.remove(line.getCubicCurve()));
		nnode.nmap.remove(layPane);
		if(this instanceof DLayer) {
			nnode.nmap.remove(((DLayer)this).getJoinLine().getCubicCurve());
		}
	}
	
	public boolean relatedViaJoinsToLay(LAY lay) {
		ArrayList<Join> list = new ArrayList<Join>();
		lay.getFields().forEach(f ->{
			f.getJoins().forEach(jj->{
				if(jj.getRemoteSchema().equals(nnode.getSchema()) && jj.getRemoteTable().equals(nnode.getTable())) {
					list.add(jj);
				}
			});
		});
		return list.size() > 0;
	}

	public void populate(){
		this.getPopulation().setValue(Population.POPULATED);
		sheet.setCalculateCells(false);
		sheet.clearPopulation();
		items.clear();
		
		//
		this.refreshPivotCache();
		this.recreateVersions();

		SQL sql = null;
		if (this.sqlType.getValue() == SqlType.SQLJ || this.sqlType.getValue() == SqlType.SQLD) {
//			this.recreateVersions();
			sql = this.getSQLJ();
//			items = nnode.getOpenDAO().readDB(this.getSQLJ(), this);
		} else if (this.sqlType.getValue() == SqlType.SQL ){
//			this.recreateVersions();
			sql = this.getSQL();
//			items = nnode.getOpenDAO().readDB(this.getSQL(), this);
		}
		
		items = nnode.getOpenDAO().readDB(sql, this);


        //BUILD COLUMNS
		sheet.getTableView().setItems(items);	
	    sheet.setTooltip(new Tooltip(items.size() + " rows"));
		nnode.nmap.napp.getBottomBar().getRowsCount().setCountValue(items.size());
	    
		sheet.createColumns();
    	sheet.setCalculateCells(true);
    	sheet.getTableView().refresh();
        this.nnode.nmap.napp.getFilemanager().getActiveNFile().getUndoManager().saveUndoAction();        
	}
	
	public void clearPopulation() {
		this.getPopulation().setValue(Population.UNPOPULATED);
		sheet.clearPopulation();
		
		this.nnode.nmap.getNFile().getGridManager().removeTab(sheet);
		this.nnode.nmap.getNFile().getUndoManager().saveUndoAction();
	}
	
	
	public void deleteLayer() {
		this.getSelectedFields().forEach(f ->{
			f.setSelected(false);
			f.setPivot(false);
			f.setGroupBy(false);
		});
		this.getSelectedFields().clear();
		this.getFormulaFields().clear();
		
		childJoins.forEach(joinLine -> {
			nnode.nmap.remove(joinLine.getCubicCurve());//remove join from map
			joinLine.getToLay().getParentJoins().remove(joinLine);// remove from parent list (no need to remove from local child joins because layer is being deleted)
			joinLine.getToLay().getSearchList().forEach(sc ->{	//remove from all groups
				if(sc.getRemoteLay() ==  this) new ArrayList<Group>(sc.getGroups()).forEach(g ->g.remove(sc));
			});
			joinLine.getToLay().getSearchList().removeIf(sc -> sc.getRemoteLay() ==  this); //remove from conditions list
		});
		
		parentJoins.forEach(joinLine -> {
			nnode.nmap.remove(joinLine.getCubicCurve());
			joinLine.getFromLay().getChildJoins().remove(joinLine);
		});
		
		nnode.nmap.remove(layPane);
		nnode.remove(this);
		if (population.getValue() == Population.POPULATED) nnode.nmap.getNFile().gridManager.removeTab(sheet);
		
		if(this instanceof DLayer) {
			nnode.nmap.remove(((DLayer)this).getJoinLine().getCubicCurve());
			((DLayer)this).getParentLay().setChildDLayer(null);			
		}
		
	}
	
	public void disconnect(LAY remoteLAY) {
		this.getSearchList().forEach(sc ->{	
			if(sc.getRemoteLay() == remoteLAY) new ArrayList<Group>(sc.getGroups()).forEach(g -> g.remove(sc));
		});
		ArrayList<SearchCON> arr = new ArrayList<SearchCON>( this.getSearchList().filtered(sc -> sc.getRemoteLay() == remoteLAY));
		if(arr.stream().filter(sc -> sc.getGroups().size()>0).count() == 0) {
			arr.forEach(sc -> this.getSearchList().remove(sc));
			JoinLine jl = remoteLAY.getChildJoins().filtered(cj -> cj.getToLay() == this).get(0);
			remoteLAY.getChildJoins().remove(jl);
			this.parentJoins.remove(jl);
			nnode.nmap.remove(jl.getCubicCurve());//remove join from map
			remoteLAY.setSelection(Selection.UNSELECTED);
		}
	}	
	
//	// CSV Export
//	public void exportToCsv() {
//		final FileChooser fileChooser = new FileChooser();		
//		fileChooser.setTitle("Save CSV");
//		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//		fileChooser.setInitialFileName(nnode.getTableNameWUnderScr() + ".csv");
//		File file = fileChooser.showSaveDialog(nnode.nmap.napp.getStage());
//		if (file != null) {
//			StringBuilder fileString = new StringBuilder();
//			ArrayList<PivotColumn>  vers = new ArrayList<PivotColumn>(this.getVersions());
//	        if(vers.size() > 0) {
//	        	vers.forEach(col -> fileString.append(col.getLabel() + (vers.indexOf(col) < (vers.size()-1) ? "," : "\n")));
//	        	this.sheet.getTableView().getItems().forEach(bo -> {
//	        		vers.forEach(version -> {
//	        			SimpleObjectProperty<?> property = bo.getProperty(version);
//	        			fileString.append((property.get() == null ? "null," : property.get()) + (vers.indexOf(version) < (vers.size()-1) ? "," : "\n"));
//	        		});
//	        	});
//	        }
//			try {
//				Writer writer = new BufferedWriter(new FileWriter(file));
//				writer.write(fileString.toString());
//				writer.flush();
//				writer.close();
//			} catch (Exception ex) { ex.printStackTrace(); } 
//		}
//	}
	
	public ArrayList<LAY> getAllConnectedLayers() {
		ArrayList<LAY> allLays = new ArrayList<LAY>();		
		allLays.add(this);
		this.locateAll(this, allLays);
		return allLays;
	}
	
	public void locateAll(LAY callingLay, ArrayList<LAY> allLays) {
		ArrayList<LAY> localSet = new ArrayList<LAY>();
		parentJoins.forEach(line ->{
			localSet.add(line.getFromLay());
		});
		
		childJoins.forEach(line ->{
			localSet.add(line.getToLay());
		});

		//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		if(this instanceof DLayer) localSet.add(((DLayer) this).getParentLay());
		if(childDLayer != null) localSet.add(childDLayer);
		//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		localSet.forEach(lay -> {
			if(callingLay != lay && !allLays.contains(lay)) {
				allLays.add(lay);
				lay.locateAll(this, allLays);
			}
		});
	}

	
	public boolean isRelatedSQLtoSQL(LAY lay) {
		return 
		this != lay 
		&& this.relatedViaJoinsToLay(lay) 
		&& this.getSqlType() == SqlType.SQL 
		&& lay.getSqlType() == SqlType.SQL;
	}
	
	public boolean isRelatedSQLJtoSQLJ(LAY lay) {
		return 
		this != lay 
		&& this.relatedViaJoinsToLay(lay) 
		&& this.getSqlType() == SqlType.SQLJ 
		&& lay.getSqlType() == SqlType.SQLJ
		&& this.getSelectedFields().size() == 0
		&& this.isRoot();
	}
	public boolean isSQLJ_to_SQLJ(LAY lay) {
		return 
		this != lay
		&& this.getSqlType() == SqlType.SQLJ 
		&& lay.getSqlType() == SqlType.SQLJ
		&& this.getSelectedFields().size() == 0
		&& this.isRoot();
	}
	
	//SHIFT
	public boolean isShiftLink(LAY lay) {
		return 
		this != lay 
		&& this.relatedViaJoinsToLay(lay) 
		&& this.getSqlType() == SqlType.SQLJ 
		&& lay.getSqlType() == SqlType.SQL;
	}

	public void remoteFieldsOn() {
		this.getSelectedFields().forEach(fld -> {
			if(this != fld.getFieldLay()) {
				fld.getFieldLay().getIndicators().remoteFieldsOn();
			}
			fld.getFieldLay().setLabelBold();
		});
	}

	public void remoteFieldsOff() {
		this.getSelectedFields().forEach(field -> {
			field.getFieldLay().getIndicators().remoteFieldsOff();
			field.getFieldLay().setLabelNormal();
		});
	}

	
	public ObservableList<SearchCON> getSearchList(){
		return  searchCONsList;
	}
	
	public void moveDotTo(double toX, double toY) {
		dotTimeline.getKeyFrames().clear(); 
		dotTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(logic.layoutXProperty(), toX, Interpolator.EASE_BOTH)));		
		dotTimeline.setCycleCount(1);
		dotTimeline.playFromStart();
	}
	
	public VBox getLogic() {
		return logic; 
	}
	

	public void showLogic() {
		logic.setLayoutX(nnode.getLayoutX());
		logic.show();
	}
	
	public Level getRootLevel() {
		return rootLevel;
	}
	
	public void closeLogic() {		
		logic.hide();
		this.getRootLevel().close();
	}
	
	public void openLogic() {
		this.getRootLevel().open();
	}

	public Set<SearchCON> getLogicConditions() {
		return this.getRootLevel().getLogicConditions(new HashSet<SearchCON>());
	}
	
	public boolean isRoot() {
		return this.parentJoins.stream().filter(line -> (line.getFromLay().getSqlType() == SqlType.SQLJ || line.getFromLay().getSqlType() == SqlType.SQLD)).count() == 0 && (this.getSqlType() == SqlType.SQLJ || this.getSqlType() == SqlType.SQLD);
	}
	
	public LAY locateSQLJRoot() {
		if(this.isRoot()){
			return this;
		}else {
			return parentJoins.stream().filter(ln -> ln.getFromLay().getSqlType() == SqlType.SQLJ || ln.getFromLay().getSqlType() == SqlType.SQLD).findFirst().get().getFromLay().locateSQLJRoot();
		}
	}
	
	//•••••••••••••••••••••••••••••••••••••••
	public ArrayList<LAY> getMenuLayers_for_Edit() {
		if(this.getSqlType() == SqlType.SQL) {
			return this.getSQLMenusLays();
		}else {
			return this.getSQLJMenuLayers();
		}		
	}
	
	private ArrayList<LAY> getSQLJMenuLayers(){
		ArrayList<LAY> lays = new ArrayList<LAY>();
		LAY sqljMaster = this.locateSQLJRoot();
		if(sqljMaster != this) {
			lays.add(sqljMaster);
			sqljMaster.getChildrenUpTo(lays, this);
		}else {
			lays.add(this);//only current lay
		}
		return lays;
	}
	
	private ArrayList<LAY> getSQLMenusLays(){
		ArrayList<LAY> lays = new ArrayList<LAY>();
		lays.add(this);
		this.childJoins.forEach(cj ->{
			LAY el = cj.getToLay();
			if(el.getSqlType() == SqlType.SQL) {
				lays.addAll(el.getSQLMenusLays());
			}else {
				el.getMenuLayers_for_Edit().forEach(l ->{
					if(!lays.contains(l)) lays.add(l);
				});				
			}
		});
		return lays;
	}
	
	private ArrayList<LAY> getChildrenUpTo(ArrayList<LAY> lays, LAY upToLay) {
			childJoins.forEach(line -> {
				if(!lays.contains(upToLay)) {
					lays.add(line.getToLay());
					line.getToLay().getChildrenUpTo(lays, upToLay);
				} else return;
			});
		return lays;
	}
	
	//••••••••••••••••••••••••••••••••••••••• FIELD USAGE
	public boolean isSQL() {
		return this.sqlType.getValue() == SqlType.SQL;
	}
	
	//Lay Delete
	public List<Field> getChildPathSelectedFields() {
		List<Field> fieldLayers = new ArrayList<Field>();
		fieldLayers.addAll(this.getFields().stream().filter(ff -> ff.isSelected()).toList());
		childJoins.forEach(line -> fieldLayers.addAll(line.getToLay().getChildPathSelectedFields()));
		return fieldLayers;
	}
	
	//DLayer
	public boolean isUsedInDlayer(Field inFld) {
		//TODO toLIST Below  possibly will cause error, on jar on older JAVA
		return this.getFields().stream().filter(locFld -> locFld.getParentVersion() != null
			&& ((locFld.getParentVersion().getField() == inFld || locFld.getParentVersion().getPivotField() == inFld)
				&& (locFld.isSelected() 
					|| this.isUsedInFormulas(locFld).size() > 0
					|| this.isUsedInSearchList(locFld).size() > 0
					|| this.isUsedInSearchParentPath(locFld).size() > 0
					|| this.isUsedInFormulaParentPath(locFld).size() > 0
					|| this.isUsedInSearchChildPath(locFld).size() > 0
					|| this.isUsedInFormulaChildPath(locFld).size() > 0							
					)									
			)).count() > 0;
	}
	
	public List<ELM> getParentPathFormulaELMs() {
		 List<ELM> used = new  ArrayList<ELM>();
		 this.getFields().forEach(locFld ->{
			 used.addAll(this.isUsedInFormulas(locFld));
			 used.addAll(this.isUsedInFormulaParentPath(locFld));
		 });
		 childJoins.forEach(line -> used.addAll(line.getToLay().getParentPathFormulaELMs()));		
		 return used;
	}
	
	//••••••••••••••••••••••••••••••••••••••• Unselect field	
	public List<ELM> isUsedInSearchList(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 this.getSearchList().forEach(i -> used.addAll(i.isUsed(field)));
		 return used;
	}
	
	public List<ELM> isUsedInFormulas(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 this.getFormulaFields().forEach(i -> used.addAll(i.isUsed(field)));
		 return used;
	}
	
	public List<ELM> isUsedInFormulaParentPath(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 parentJoins.forEach(jn -> {
			 if(jn.getFromLay().getSqlType() != SqlType.SQL) {
				 used.addAll(jn.getFromLay().isUsedInFormulas(field));
				 used.addAll(jn.getFromLay().isUsedInFormulaParentPath(field));
			 }			 
		 });
		return used;
	}
	
	public List<ELM> isUsedInSearchChildPath(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 childJoins.forEach(jn -> {
			 used.addAll(jn.getToLay().isUsedInSearchList(field));
			 used.addAll(jn.getToLay().isUsedInSearchChildPath(field));
		 });
		return used;
	}
	
	//Possibly remove two methods below ••••••••••••••••••••••••••••••••••••••••
	//TODO DO I NEED THIS, OR ITS POINTLESS?????
	public  List<ELM> isUsedInSearchParentPath(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 parentJoins.forEach(jn -> {
			 used.addAll(jn.getFromLay().isUsedInSearchList(field));
			 used.addAll(jn.getFromLay().isUsedInSearchParentPath(field));
		 });
		return used;
	}
	
	//TODO DO I NEED THIS???? OR ITS POINTLESS???
	public List<ELM> isUsedInFormulaChildPath(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		 childJoins.forEach(jn -> {
			 used.addAll(jn.getToLay().isUsedInFormulas(field));
			 used.addAll(jn.getToLay().isUsedInFormulaChildPath(field));
		 });
		return used;
	}
	
	public ObservableList<PivotColumn> getVersions() {
		return versions;
	}	
	
	//SEARRCH SQL
	public SQL getSearchSQL(String func_full_name, String full_name) {
		if(this.getSqlType() == SqlType.SQLJ || this.getSqlType() == SqlType.SQLD) {
			return  this.locateSQLJRoot().getSearchSQLJ(func_full_name, full_name);
		}else {
			//ORIGINAL
			SQL sql = new SQL();
			sql.SELECT(" DISTINCT " + func_full_name);
			sql.FROM(this);
			if(this.getRootLevel().getActiveGroup().status.get() != "Closed" ) {
				//Change to check if open contains more than one record
				sql.WHERE();
				this.getRootLevel().getActiveGroup().buildSearchSQL(sql);
			}
			sql.append(" ORDER BY " +  func_full_name);
			return sql;
		}
	}
	
	public SQL getSearchSQLJ(String func_full_name, String full_name) {
		SQL sqlj = new SQL();
		sqlj.SELECT(" DISTINCT " + func_full_name);		
		if(this instanceof DLayer) {
			sqlj.DSUB((DLayer) this);//
		}else {
			sqlj.FROM(this);//
		}
		sqlj.line();
		childJoins.forEach(line -> line.getToLay().join(sqlj, this, line));
		//--------------------------------------------------------
		sqlj.WHERE();
		sqlj.append(full_name + " IS NOT NULL " ); //•• OPTIONAL	(make it optional??)
		if(this.getRootLevel().groups.size() > 0 && this.getRootLevel().groups.get(0).size() > 0) {
			sqlj.AND();
			this.on(this.getRootLevel(), sqlj);
		}
		sqlj.append(" ORDER BY " +  func_full_name);//??
		return sqlj;
	}

	//SQL	
	public SQL getSQL() {
		return this.getSQL(null);
	}
	
	public SQL getSQL(String whatString) {
		SQL sql = new SQL().SELECT();
		if(whatString != null) {
			sql.append(" " + whatString + " ");
		}else {
			ArrayList<Field> lfls = new ArrayList<Field>(this.getFieldsAndFormulas());
			lfls.forEach(fld -> {
				sql.append(fld.getFunction_Column() + " AS " + fld.getAliase() + (((lfls.indexOf(fld) + 1) < lfls.size()) ? "," + System.getProperty("line.separator") : ""));
			});			
		}
		sql.FROM(this);
		if(this.getRootLevel().getGroups().size() > 0 && this.getRootLevel().getGroups().get(0).size() > 0) {
			this.getRootLevel().buildSQL(sql.WHERE());
		}
		return sql;
	}
	
	//SQLJ
	public SQL getSQLJ() {
		SQL sqlj = new SQL().append("").SELECT();
		ArrayList<String>  vers = new ArrayList<String>();
		this.versions.forEach(zv -> {
			vers.add(zv.getFunction_Column() + " AS " + zv.getAliase());
		});
		
		vers.forEach(stringVersion -> {
			sqlj.append(stringVersion + (((vers.indexOf(stringVersion) + 1) < vers.size()) ? "," + System.getProperty("line.separator") : ""));
		});
				
		if(this instanceof DLayer) {
			sqlj.DSUB((DLayer) this);
		}else {
			sqlj.FROM(this);
		}
		
		sqlj.line();		
		childJoins.forEach(line -> line.getToLay().join(sqlj, this, line));
		if(this.getRootLevel().groups.size() > 0 && this.getRootLevel().groups.get(0).size() > 0) {
			sqlj.append("  /* WHERE */ ");
			sqlj.WHERE();
			this.on(this.getRootLevel(), sqlj);
		}
		
		this.groupBy(sqlj);	
		return sqlj;
	}

	//•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public void recreateVersions() {
		ObservableList<Field>  selectedFields = this.getSelectedFields();

		ArrayList<PivotColumn> newVersions = new ArrayList<PivotColumn>();
		if((this.isRoot()) && selectedFields.size()>0) {
			selectedFields.forEach( field -> { //A BASIC GROUPBY
				if(field.isGroupBy()) {
					PivotColumn version = new PivotColumn(VersionType.GROUPBY, field);
					version.setFunction_Column(field.getFunction_Column());
					version.setAlias(field.getAliase());
					version.setLabel(field.getText());
					version.setTip("GROUP BY "+ field.getText());
					newVersions.add(version);//only one
				}
			});
			//C BLANKS
			selectedFields.forEach( field -> {
				if(field.isBlank()) {
					PivotColumn version = new PivotColumn(VersionType.BLANK, field);
					version.setFunction_Column(field.getFunction_Column());
					version.setAlias(field.getAliase());
					version.setLabel(field.getText());	
					version.setTip(field.getText());
					newVersions.add(version);
				}				
			});
		
			//PIVOT + AGRIGATE 
			FilteredList<Field> pivotFields = selectedFields.filtered(field -> field.isPivot());
			selectedFields.forEach(fncField -> {
				if(fncField.isAgrigated() && fncField instanceof FormulaField) {
					pivotFields.forEach(ptField -> {//PIVOT
						ptField.getPivotCache().forEach(val -> {
							PivotColumn version = new PivotColumn(VersionType.PIVOT, fncField);
							version.setPivotField(ptField);
							version.setFunction_Column(((FormulaField) fncField).getPivot_Column(ptField, val));
							version.setAlias(ptField.getAliase() + "_" +  fncField.getAliase() + "_" + ptField.getPivotCache().indexOf(val));
							version.setLabel(val);
							version.setTip(fncField.getText() +" • " + ptField.getText());
							newVersions.add(version);
						});
					});

					//AGRIGATE AND SUBTOTAL
					PivotColumn version = new PivotColumn(VersionType.SUBTOTAL, fncField);
					version.setFunction_Column(fncField.getFunction_Column());
					version.setAlias(fncField.getAliase());
					version.setLabel(fncField.getText());
					version.setTip(fncField.getText());
					newVersions.add(version);
				}
			});
		}else if(((this.isRoot()) && selectedFields.size()==0 ) || this.getSqlType() == SqlType.SQL)  {//F ZERO FIELDS SELECTED, USE ALL COLUMNS
			this.getFieldsAndFormulas().forEach(fld -> {				
				PivotColumn versionA = new PivotColumn(VersionType.BLANK, fld);
				versionA.setFunction_Column(fld.getFunction_Column());
				versionA.setAlias(fld.getAliase());
				versionA.setLabel(fld.getText());
				versionA.setTip(fld.getText());	
				newVersions.add(versionA);
			});
		}

		versions.clear();
		versions.addAll(newVersions);
		
	}
	
	public void refreshPivotCache() {
		selectedFields.filtered(field -> field.isPivot()).forEach(pivotChache -> {//PIVOT ONLY					
			pivotChache.getPivotCache().clear();
			pivotChache.getPivotCache().addAll(nnode.getOpenDAO().readDistinctValues(this.getSearchSQLJ(pivotChache.getFunction_Column(), pivotChache.getFunction_Column())));
		}); 
	}
	
//••••••••••••••••••••••••••••••••••••••••••
//	ROLLUP (YEAR, MONTH, DAY)
//
//	With a ROLLUP, it will have the following outputs:
//
//	YEAR, MONTH, DAY
//	YEAR, MONTH
//	YEAR
//	()
//
//	With CUBE, it will have the following:
//
//	YEAR, MONTH, DAY
//	YEAR, MONTH
//	YEAR, DAY
//	YEAR
//	MONTH, DAY
//	MONTH
//	DAY
//	()

	
	public boolean doShowGroupOptions() {
		ArrayList<String> groupStrings = new ArrayList<String>();
		selectedFields.forEach(f -> {
			if(f.isGroupBy()) {//BASIC GROUPBY
				groupStrings.add(f.getFunction_Column());
			}		
		});
//		System.out.println("SHOW ROLLUP: "+" groupby:" + (groupStrings.size() > 0) + " !SQL: " + (this.getSqlType() != SqlType.SQL) +" isRoot: "+ this.isRoot());
		return groupStrings.size() > 0 && this.getSqlType() != SqlType.SQL && this.isRoot();		
	}
	
	private void groupBy(SQL sql) {		
		ArrayList<String> groupStrings = new ArrayList<String>();
		selectedFields.forEach(f -> {
			if(f.isGroupBy()) {//BASIC GROUPBY
				groupStrings.add(f.getFunction_Column());
			}		
		});
		if(groupStrings.size() > 0) {
			sql.GROUPBY();
			groupStrings.forEach(vers -> {
				sql.append(vers + (((groupStrings.indexOf(vers) + 1) < groupStrings.size()) ? ", " : ""));
			});
			
			if(rollup.get()) {
				sql.WITHROLLUP();
			}
			
			if(orderby.get()) {
				sql.ORDERBY(); //MAKE IT OPTIONAL
				groupStrings.forEach(vers -> {
					sql.append(vers + " ASC "+ (((groupStrings.indexOf(vers) + 1) < groupStrings.size()) ? ", " : ""));
				});	
			}
		}
	}
	
	
	private void join(SQL sql, LAY parentLAY, JoinLine parentLine) {	
		String joinString = new String();
		if(parentLine.getJoinType() == JoinType.JOIN) joinString = "JOIN ";
		else if(parentLine.getJoinType() == JoinType.LEFT) joinString = "LEFT JOIN ";
		else if(parentLine.getJoinType() == JoinType.RIGHT) joinString = "RIGHT JOIN ";
		
		if(this instanceof DLayer) {
			sql.append(joinString + "(" + ((DLayer)this).getParentLay().getSQLJ() + ") " + this.getAliase());
		}else {
			sql.append(joinString + nnode.getFullNameWithOptionalQuotes() + " " + this.getAliase());
		}
		
		if(this.getRootLevel().getGroups().size()>0) {// this will prevent error for manual join with zero conditions
			sql.append(" ON ");
		}
		
		this.on(this.getRootLevel(), sql);		
		sql.line();
		childJoins.forEach(line -> line.getToLay().join(sql, this, line));
	}
	
	private void on(Level level, SQL sql) {		
		List<Group> groups =  level.getGroups();
		if (groups.size() > 1) sql.open();
		groups.forEach(group -> {
			//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
			ArrayList<SearchCON> conditions = new  ArrayList<SearchCON>(group.getItems());
			if(conditions.size() > 1 || group.getChild() != null) sql.open();
				conditions.forEach(sel -> {
					sql.append(sel.getFuncColumn());
					sql.append(((conditions.indexOf(sel) + 1) < conditions.size()) ? " AND " : "");
				});
				if (group.getChild() != null) this.on(group.getChild(), sql.AND());
			if(conditions.size() > 1 || group.getChild() != null) sql.close();
			//•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
			if ((groups.indexOf(group) + 1) < groups.size()) sql.OR();
		});
		if (groups.size() > 1) sql.close();	
	}

// EFFDT TEMPORARY REMOVED
//	if(sel.getCustomJoin().getValue() == CustomJoin.NORMAL) {						
//	sql.append(this.getSqlAliase() + "." + join.getRemoteColumn()  + " = " + join.getLay().getSqlAliase() + "." + join.getSqlColumn());
//}else if(sel.getCustomJoin().getValue() == CustomJoin.EFFDT) {
//	//FIRST EFFDT, WILL NEED MORE OPTIONS
	
	
//	String subSql = "(SELECT MAX(" 
//	+ join.getRemoteColumn() +") FROM " 
//	+ nnode.getFullNameWithOptionalQuotes() + " effdt WHERE "
//	+ " effdt." + join.getRemoteColumn() + " <= "+
//	join.getLay().getSqlAliase() + "." + join.getSqlColumn()
//	+" ) ";
//	sql.append(this.getSqlAliase() + "." + join.getRemoteColumn()  + " = " + subSql);
	
	
//
////	 -- B   sq.QuotaDate <= sh1.sh_OrderDate
////  AND sq.QuotaDate = 
////  (SELECT MAX(QuotaDate)  FROM   salespersonquotahistory effdt WHERE  
////  effdt.SalesPersonID = sh1.sp_SalesPersonID
////  AND  effdt.QuotaDate <= sh1.sh_OrderDate
////  )
//}
	
	
	protected boolean isLocal(NKey key, String string) {
		return nnode.getSchema().equals(key.getSchema()) && nnode.getTable().equals(key.getTable());
	}
	
	protected boolean isRemote(NKey key, String string) {
		return nnode.getSchema().equals(key.getRSchema()) && nnode.getTable().equals(key.getRTable());
	} 
	
	public void addSelectedField(Field field) {
		selectedFields.add(field);
	}

	public void removeSelectedField(Field field) {
		selectedFields.remove(field);
	}

	public LayerMenu rebuildLayerMenu(LAY rootLay) {
		if(layerMenu == null) layerMenu = new LayerMenu(rootLay, this, this.getViewLabel());	
		layerMenu.getItems().clear();
		if(this instanceof DLayer) ((DLayer)this).rebuildDFieldsAndJoins();
		this.getFieldsAndFormulas().forEach(field -> {
			FieldMenu fm = field.getOrCreateFieldMenu();
			fm.setDisable(false);
			layerMenu.getItems().add(fm);
		});	
		return layerMenu;
	}
	
	public ArrayList<Join> getRelatedJoins(Nnode n) {
		ArrayList<Join> list = new ArrayList<Join>();
		this.getFields().forEach(f->{
			f.getJoins().forEach(j->{
				if(j.getRemoteSchema().equals(n.getSchema()) && j.getRemoteTable().equals(n.getTable())) {
					list.add(j);
				}
			});
		});
		return list;
	}
	
	public ArrayList<Join> getRelatedJoins(LAY lay) {
		ArrayList<Join> list = new ArrayList<Join>();
		this.getFields().forEach(f->{
			f.getJoins().forEach(j->{				
				lay.getFields().forEach(f2 ->{
					f2.getJoins().forEach(j2->{
						if(j.getRemoteSchema().equalsIgnoreCase(j2.getSchema()) && j.getRemoteTable().equalsIgnoreCase(j2.getTable())) {							 
							if(j.getRemoteColumn().equalsIgnoreCase(j2.getColumn())
//									|| j2.getRemoteColumn().equals(j.getColumn())
									) {
								if(!list.contains(j)) {
									list.add(j);
								}
							}
						}
					});
				});	
			});
		});
		return list;
	}

	public ArrayList<String> getValuesList(String func_full_name, String full_name, String sql_name) {
		if(nnode.nmap.napp.getMenu().getViewMenu().getDynamicSearchMenuItem().isSelected()) {
			return  nnode.getOpenDAO().readDistinctValues(this.getSearchSQL(func_full_name, full_name));
			//WILL STATIC Values Lsit  WORK ON DLAYER?
		}else {
			return  nnode.getStaticValuesList(sql_name);
		}	
	}
	
	//XML ••••••••••••••••••••••••••••••••••••••••••
	public void saveXml(Document document, Element nnodeE) {
		Element layE = document.createElement("layer");
		layE.setAttribute("aliase", this.getAliase());
		layE.setAttribute("sqlType", sqlType.getValue().name());
		layE.setAttribute("population", population.getValue() + "");
		layE.setAttribute("rollup", rollup.get() + "");
		layE.setAttribute("orderby", orderby.get() + "");
		
		if(this instanceof DLayer) 	{			
			layE.setAttribute("parentLay", ((DLayer) this).getParentLay().getAliase());			
		}
		nnodeE.appendChild(layE);
		if(this.getRootLevel().groups.size()>0) this.getRootLevel().saveLevel(document, layE);

		if(searchCONsList.size()>0) {
			Element searchList = document.createElement("searchList");
			layE.appendChild(searchList);
			searchCONsList.forEach(searchCON -> {
				searchCON.saveXML(document, searchList);
			});
		}
		
		Element fieldsE = document.createElement("fieldsList");
		layE.appendChild(fieldsE);
		this.getFieldsAndFormulas().forEach(fld -> fld.saveXml(document, fieldsE));
		
		Element selectedFields = document.createElement("selectedFields");
		layE.appendChild(selectedFields);
		this.selectedFields.forEach(selFld -> {
			Element fldE = document.createElement("selectedField");			
			fldE.setAttribute("layAliase", selFld.getFieldLay().getAliase());		
			fldE.setAttribute("fieldAliase", selFld.getAliase());
			selectedFields.appendChild(fldE);	
		});
				
		//parents ••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		if(parentJoins.size()>0) {
			Element parentsE = document.createElement("parents");
			layE.appendChild(parentsE);
			parentJoins.forEach(line -> {
				Element parentE = document.createElement("parent");
				parentE.setAttribute("aliase", line.getFromLay().getAliase());
				parentE.setAttribute("joinType", line.getJoinType().toString());
				parentsE.appendChild(parentE);					
			});
		}
		
		//children
		if(childJoins.size()>0) {
			Element childrenE = document.createElement("children");
			layE.appendChild(childrenE);
			childJoins.forEach(line -> {
				Element childE = document.createElement("child");
				childE.setAttribute("aliase",  line.getToLay().getAliase());
				childE.setAttribute("joinType", line.getJoinType().toString());
				childrenE.appendChild(childE);					
			});
		}
		
		//population
		Element tableColumnsE = document.createElement("versions");
		layE.appendChild(tableColumnsE);
		ArrayList<PivotColumn>  vers = new ArrayList<PivotColumn>(this.getVersions());		
		vers.forEach(version -> {
			Element versionE = document.createElement("version");
			tableColumnsE.appendChild(versionE);			
			versionE.setAttribute("versionType", version.getVersionType().toString());
			versionE.setAttribute("fieldAliase", version.getField().getAliase());
			if(version.getPivotField() != null) versionE.setAttribute("pivotfieldAliase", version.getPivotField().getAliase());
			
			versionE.setAttribute("function", version.getFunction_Column());
			versionE.setAttribute("nculumnId", version.getField().getSchema() + version.getField().getTable() + version.getField().getColumn());			
			versionE.setAttribute("label", version.getLabel());
			versionE.setAttribute("aliase", version.getAliase());			
			versionE.setAttribute("tip", version.getTip());
		});
				
		Element populationE = document.createElement("population");
		layE.appendChild(populationE);
		
		if(population.getValue() == Population.POPULATED) {			
			Element openBOsE = document.createElement("bos");
			populationE.appendChild(openBOsE);
			this.getItems().forEach(bo -> {
				Element boX = document.createElement("bo");				
				openBOsE.appendChild(boX);
				vers.forEach(version -> {
					SimpleObjectProperty<?> sp = bo.getProperty(version);
					if(sp != null) {
						Element colX = document.createElement("col");
						boX.appendChild(colX);
						colX.setAttribute("aliase", version.getAliase());
						colX.setAttribute("rowset_type", version.getField().getRowset_type());	

						if(sp.getValue() != null) {
							colX.setAttribute("isNull", "false");
							//Check how it handles of saving big integer
							colX.setTextContent(bo.getProperty(version).getValue().toString());							
						}else {
							colX.setAttribute("isNull", "true");
						}
					}
				}); 
			});	
		}
	}
	
	
	public void loopA(OpenContext context, Node xLayer) {
		rollup.set(Boolean.valueOf(XML.atr(xLayer, "rollup")));
		orderby.set(Boolean.valueOf(XML.atr(xLayer, "orderby")));		
		
		List<Node> lst = XML.children(xLayer);
		lst.forEach(lstItem ->{
			if(lstItem.getNodeName().equals("fieldsList")) {
				XML.children(lstItem).forEach(nn ->{
					if(nn.getNodeName().equals("field")) {
						//existing
						if(this.containsField(XML.atr(nn, "aliase"))) {
							Field field = this.getFieldOrFunction(XML.atr(nn, "aliase"));
							field.setSelected(Boolean.valueOf(XML.atr(nn, "select")));
							field.setPivot(Boolean.valueOf(XML.atr(nn, "pivot")));
							field.setGroupBy(Boolean.valueOf(XML.atr(nn, "group")));
							field.setAgrigated(Boolean.valueOf(XML.atr(nn, "agrigate")));
							field.setRowset_type(XML.atr(nn, "rowset_type"));
							field.loopAChache(nn);
							field.loopAFormat(nn);
							context.getFields().put(field.getAliase(), field);
						}else {
							if (XML.atr(nn, "fieldType").equals("CALCULATED")) {
								FormulaField formulaField = new FormulaField(this);
								formulaField.setRowset_type(XML.atr(nn, "rowset_type"));
								formulaField.setAliase(XML.atr(nn, "aliase"));
								formulaField.setSelected(Boolean.valueOf(XML.atr(nn, "select")));
								formulaField.setPivot(Boolean.valueOf(XML.atr(nn, "pivot")));
								formulaField.setGroupBy(Boolean.valueOf(XML.atr(nn, "group")));
								formulaField.setAgrigated(Boolean.valueOf(XML.atr(nn, "agrigate")));
								formulaField.loopAFormat(nn);

								this.addFormulaField(formulaField);
								context.getFields().put(formulaField.getAliase(), formulaField);
							} else {
								String rootFld = XML.atr(nn, "rootFieldColumnId");
								boolean stillExistInDb = true;
								if(rootFld != null) {
									stillExistInDb = nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getXColumns().filtered(c -> 
									(c.getSchema() + "." + c.getTable() + "." + c.getColumn()).equals(rootFld)
									).size() > 0;
								}

								if(stillExistInDb && this instanceof DLayer) {
									Field field = new Field(this);
									field.setSchema(XML.atr(nn, "schema_name"));
									field.setTable(XML.atr(nn, "table_name"));
									field.setColumn(XML.atr(nn, "column_name"));
									field.setRowset_type(XML.atr(nn, "rowset_type"));
									field.setSQL_Column_name(XML.atr(nn, "sql_column_name"));
									field.setAliase(XML.atr(nn, "aliase"));
									field.setText(XML.atr(nn, "label"));
									
									field.setSelected(Boolean.valueOf(XML.atr(nn, "select")));
									field.setPivot(Boolean.valueOf(XML.atr(nn, "pivot")));
									field.setGroupBy(Boolean.valueOf(XML.atr(nn, "group")));
									field.setAgrigated(Boolean.valueOf(XML.atr(nn, "agrigate")));
									field.setColumn_key(XML.atr(nn, "column_key"));
									field.loopAChache(nn);//??
									field.loopAJoins(nn);
									field.loopAFormat(nn);
									
									this.addField(field);
									context.getFields().put(field.getAliase(), field);
								}else {									
									nnode.nmap.getNFile().getMessages().add(new Message(nnode.nmap.getNFile(), "missing", "Column: " + XML.atr(nn, "schema_name") +"."+XML.atr(nn, "table_name") + "." + XML.atr(nn, "column_name")));
								}
							}							
						}
					}
				});
			}
		});
		
		//Section SearchList
		lst.forEach(n ->{
			if(n.getNodeName().equals("searchList")) {
				XML.children(n).forEach(sc->{
					if(sc.getNodeName().equals("searchCON")) {
						SearchCON searchCON = new SearchCON(this);
						searchCON.setUniqueId(XML.atr(sc, "uniqueId"));
						context.getAliaseCONS(nnode.getSchema(), this).put(searchCON.getUniqueId(),searchCON);
						this.addSearchCONtoSearchList(searchCON);
					}
				});
			}
		});
		
		lst.forEach(n ->{
			if(n.getNodeName().equals("population")) {
				XML.children(n).forEach(bos ->{
					if (bos.getNodeName().equals("bos")) {
						ObservableList<OpenBO> xmlBos = FXCollections.observableArrayList();
						XML.children(bos).forEach(bo ->{
							if (bo.getNodeName().equals("bo")) {
								xmlBos.add(new OpenBO(bo));
							}
						});
						this.setItems(xmlBos);
						this.getSheet().setCalculateCells(true);	
						this.getSheet().getTableView().setItems(xmlBos);
					    this.getSheet().setTooltip(new Tooltip(xmlBos.size() + " rows"));
						nnode.nmap.napp.getBottomBar().getRowsCount().setCountValue(xmlBos.size());
					}
				});
			}
		});
		
		lst.forEach(n ->{
			if(n.getNodeName().equals("versions")) {
				XML.children(n).forEach(v ->{
					if(v.getNodeName().equals("version")) {
						PivotColumn version = new PivotColumn(VersionType.valueOf(XML.atr(v, "versionType")));
						version.setFunction_Column(XML.atr(v, "function"));
						version.setAlias(XML.atr(v, "aliase"));
						version.setLabel(XML.atr(v, "label"));
						version.setTip(XML.atr(v, "tip"));
						context.getVersions().put(XML.atr(v, "aliase"), version);
					}
				});
			}
		});
	}	

	public void loopB(OpenContext context, Node xLayer) {
		List<Node> lst = XML.children(xLayer);
		lst.forEach(n ->{
			if(n.getNodeName().equals("level")) {
				this.loopB_level(context, n, this.getRootLevel());
			}
		});
		
		lst.forEach(n ->{
			if(n.getNodeName().equals("children")) {
				XML.children(n).forEach(nn ->{
					if(nn.getNodeName().equals("child")) {
						String joinType = XML.atr(nn, "joinType");
						LAY toLay = context.getAliaseLAYs().get(XML.atr(nn, "aliase"));	
						if(toLay != null) {//Should be null only if db was modified 
							JoinLine line = new JoinLine(this, toLay, JoinType.valueOf(joinType));
							toLay.parentJoins.add(line);
							this.childJoins.add(line);
						}else {
							nnode.nmap.getNFile().getMessages().add(new Message(nnode.nmap.getNFile(), "missing", "Join Layer: " + XML.atr(nn, "aliase")));
						}
					}
				});
			}
		});
		
		
		lst.forEach(n ->{
			if(n.getNodeName().equals("selectedFields")) {
				XML.children(n).forEach(nn ->{
					if(nn.getNodeName().equals("selectedField")) { //add error here on missing fields
						LAY lay =  context.getAliaseLAYs().get(XML.atr(nn, "layAliase"));
						Field field = null;
						if(lay != null) field =  lay.getFieldOrFunction(XML.atr(nn, "fieldAliase"));
						if(lay != null && field != null) {
							this.addSelectedField(field);
						}else {
							System.out.println("[• missing •] lay: " + lay  +" field: " + field );
							nnode.nmap.getNFile().getMessages().add(new Message(nnode.nmap.getNFile(), "missing", "Selected Field  Layer: " + XML.atr(nn, "fieldAliase")));
						}
					}
				});
			}
		});
		
		lst.forEach(n ->{
			if(n.getNodeName().equals("versions")) {
				XML.children(n).forEach(nn ->{
					if(nn.getNodeName().equals("version")) {
						Field vfld = context.getFields().get(XML.atr(nn, "fieldAliase"));
						Field pfld = context.getFields().get(XML.atr(nn, "pivotfieldAliase"));					
						PivotColumn version =  context.getVersions().get(XML.atr(nn, "aliase"));
						if((version.getVersionType() == VersionType.PIVOT && pfld != null && vfld != null) || (version.getVersionType() != VersionType.PIVOT && vfld != null)) {							
							version.setField(vfld);
							version.setPivotField(pfld);
							versions.add(version);
						}else {
							nnode.nmap.getNFile().getMessages().add(new Message(nnode.nmap.getNFile(), "missing", "version field is missing: " + XML.atr(nn, "fieldAliase")));
						}
					}
				});
			}
		});
		
		
		//attach parent version to field
		lst.forEach(n ->{
			if(n.getNodeName().equals("fieldsList")) {
				XML.children(n).forEach(nn->{
					if(nn.getNodeName().equals("field")) {
						String parAls = XML.atr(nn, "parentVersionAliase");
						Field field = context.getFields().get(XML.atr(nn, "aliase"));
						if(parAls != null) {
							PivotColumn version = context.getVersions().get(parAls);
							field.setParentVersion(version);
						}
						if(field instanceof FormulaField) {
							((FormulaField)field).loopB(context, nn);
						}
					}
				});
			}
		});
		
		//SearchCOM loopB
		lst.forEach(n ->{
			if(n.getNodeName().equals("searchList")) {
				XML.children(n).forEach(sc->{
					if(sc.getNodeName().equals("searchCON")) {
						context.getAliaseCONS(nnode.getSchema(), this).get(XML.atr(sc, "uniqueId")).openB(context, sc);
					}
				});
			}
		});
	}

	private void loopB_level(OpenContext context, Node xlevel, Level level) {
		List<Node> nodes = XML.children(xlevel);
		nodes.forEach(n ->{
			if (n.getNodeName().equals("group")) {
				Group group = new Group(level);//NEW GROUP
				level.setActiveGroup(group);// DO I NEED THIS???????????????
				level.getGroups().add(group);
				this.loopB_group(context, n, group);
				level.getLevelHBox().getChildren().add(group.getPane());  
				group.close();
			}
		});
	}
	
	private void loopB_group(OpenContext context, Node xGroup, Group group) {
		List<Node> nodes = XML.children(xGroup);
		nodes.forEach(n ->{
			if (n.getNodeName().equals("searchCON")) {
				group.add(context.getAliaseCONS(nnode.getSchema(), this).get(XML.atr(n, "uniqueId")));
			}
		});
		
		nodes.forEach(n ->{
			if (n.getNodeName().equals("level")) {
				Level newLevel = new Level(this, group);
				group.setChildLevel(newLevel);
				this.loopB_level(context, n,newLevel);
			}
		});
	}
	
	public String toString() {
		return this.getAliase();
	}

	public boolean sameSchema(LAY inLay) {
		return inLay.nnode.getSchema().equals(nnode.getSchema());
	}

	public boolean isNotConnectedTo(LAY inLay) {
		return 
			!this.getParents().contains(inLay) &&
		  !this.getChildren().contains(inLay) &&
		  !inLay.getParents().contains(this) &&
		  !inLay.getChildren().contains(this);
	}

	public ObservableList<FormulaField> getFormulaFields() {
		return formulaFields;
	}
	
	public ObservableList<Field> getSelectedFields() {
		return selectedFields;
	}
	
	//FIELDS
	public ObservableList<Field> getFields() {
		return fields;
	}
	
	public void addSearchCONtoSearchList(SearchCON searchCON) {
		searchCONsList.add(searchCON);
		this.nnode.nmap.getNFile().getSidePaneManager().activateSearch(this);
	}
	
	
	public ArrayList<Field> getFieldsAndFormulas() {
		ArrayList<Field> fieldsAndFunctions = new ArrayList<Field>();
		fieldsAndFunctions.addAll(fields);
		fieldsAndFunctions.addAll(formulaFields);
		return fieldsAndFunctions;
	}
	
	public void addField(Field field) {
		fields.add(field);
	}
	
	public void addFormulaField(FormulaField field) {
		formulaFields.add(field);
	}
	
	public void removeField(String als) {
		fields.removeIf(f -> f.getAliase().equals(als));
		formulaFields.removeIf(f -> f.getAliase().equals(als));
		selectedFields.removeIf(f -> f.getAliase().equals(als));
	}

	public Field getFieldOrFunction(String atr) {
		List<Field> fl = this.getFieldsAndFormulas().stream().filter(f -> f.getAliase().equals(atr)).toList();
		if(fl.size() == 0) {
			return null;
		}else {
			return fl.get(0);
		}
	}

	public boolean containsField(String fieldAliase) {
		return fields.filtered(f -> f.getAliase().equals(fieldAliase)).size()>0;
	}
	
	public boolean containsFormulaField(String fieldAliase) {
		return formulaFields.filtered(f -> f.getAliase().equals(fieldAliase)).size()>0;
	}

	public void showExternalLinks() {
		ArrayList<String> externaNnodesList = new ArrayList<String>();
		extNnodesHbox.setLayoutX(layPane.getLayoutX() + 25);
		extNnodesHbox.setLayoutY(layPane.getLayoutY() - 25);
		extNnodesHbox.setSpacing(3);
		extNnodesHbox.getStyleClass().add("externaNnodes");		
		this.getFields().forEach(sf ->{
			 sf.getJoins().forEach(jn->{
				 if(!jn.getRemoteSchema().equals(this.nnode.getSchema())) {
					if (!externaNnodesList.contains(jn.getRemoteSchema() + "." + jn.getRemoteTable())) {
						externaNnodesList.add(jn.getRemoteSchema() + "." + jn.getRemoteTable());
						StackPane sp = new StackPane();
						sp.getStyleClass().add("unpopulatedNnode");
						sp.setPrefWidth(12);
						sp.setPrefHeight(12);
						sp.setOnMouseClicked(e -> {
							ACT act = nnode.nmap.getNFile().getActivity();
							if(act instanceof Select) ((Select) act).passExternalJoin(jn);
							e.consume();
						});
						Tooltip toolTip = new Tooltip(jn.getRemoteSchema() + "." + jn.getRemoteTable());
						toolTip.setStyle("-fx-font-size: 9");
						Tooltip.install(sp, toolTip);
						extNnodesHbox.getChildren().add(sp);
					}
				}
			 });
		});
		if(!nnode.nmap.contains(extNnodesHbox) && extNnodesHbox.getChildren().size()>0) nnode.nmap.add(extNnodesHbox);	
	}

	public void hideExternalLinks() {
		extNnodesHbox.getChildren().clear();
		if(nnode.nmap.contains(extNnodesHbox)) nnode.nmap.remove(extNnodesHbox);	
	}

	public Pane getPane() {
		return layPane;
	}

	public AnimatedStyler getStyler() {
		if(styler == null) {
			styler = new AnimatedStyler(this);
		}
		return styler;
	}

	public boolean isValidJoin(LAY inLay) {//need some cleanup work, might have duplicate logic
		if(this.isRelatedSQLtoSQL(inLay) 
				|| this.isRelatedSQLJtoSQLJ(inLay) 
				|| this.isShiftLink(inLay) 
				|| (inLay instanceof DLayer && this.getSqlType() == SqlType.SQLJ)) {
			if((!this.getAllConnectedLayers().contains(inLay) && this != inLay) 
					|| (this.isNotConnectedTo(inLay) && this.isShiftLink(inLay))
					|| (this.isNotConnectedTo(inLay) && this.isRelatedSQLJtoSQLJ(inLay))
					|| (this.isNotConnectedTo(inLay) && this.isRelatedSQLtoSQL(inLay))) {
				return true;
			}
		}
		return false;			
	}

	public boolean isValidSQLDjoin(LAY inLay) {
		if(this.isNotConnectedTo(inLay) && this != inLay && !this.getAllConnectedLayers().contains(inLay) && this.getSqlType() == SqlType.SQLD && this.getSelectedFields().size()==0) {
			if((inLay.getSqlType() != SqlType.SQL && this.getParentJoins().filtered(jl -> jl.getFromLay().getSqlType() != SqlType.SQL).size() == 0) ||
					inLay.getSqlType() == SqlType.SQL) {
				return true;
			}
		}
		return false;
	}

	public void updateRowCount() {
		if(this.getItems() !=null) {
			this.nnode.nmap.napp.getBottomBar().getRowsCount().setCountValue(this.getItems().size());
		}else {
			this.nnode.nmap.napp.getBottomBar().getRowsCount().clear();
		}
	}

	public ObservableList<Region> getSearchRegion() {
		return searchRegion;
	}

	public ObservableList<Region> getOptionsRegion() {
		return optionsRegion;
	}
	
	
}






//
//
//	public void pulseold(String string) {
////		-fx-background-color: linear-gradient(#fff5eb, #ffa061), radial-gradient(center 50% -40%, radius 200%, #ffefad 45%, #ffcf0f 50%);
//
//		DropShadow dropShadow = new DropShadow();
//		dropShadow.setRadius(3);
//		dropShadow.setSpread(0.1);
////		dropShadow.setOffsetX(0);
////		dropShadow.setOffsetY(2.0);
////	    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);
//
//		dropShadow.setColor(Color.FORESTGREEN);		 
//		layPane.setEffect(dropShadow);
//				
////	    KeyFrame keyFrame1 = new KeyFrame(Duration.millis(200), new KeyValue(dropShadow.radiusProperty(), 25));
//        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(200), new KeyValue(dropShadow.radiusProperty(), 15));
//        Timeline timeline = new Timeline(keyFrame2);
//
//        timeline.setAutoReverse(true);
//        timeline.setCycleCount(2);
//        timeline.setOnFinished(e -> {
////        	layPane.setStyle("");
////        	this.updateColorMode();
//        });
//        timeline.play();         
//	}
	
	
//	public void gredient() {
//		ObjectProperty<Color> c1 = new SimpleObjectProperty<>(Color.valueOf("#ffffff"));
//		ObjectProperty<Color> c2 = new SimpleObjectProperty<>(Color.valueOf("#ff8ac4"));
//		ObjectProperty<Color> c3 = new SimpleObjectProperty<>(Color.valueOf("#ffe5f2"));
//		ObjectProperty<Color> c4 = new SimpleObjectProperty<>(Color.valueOf("#ff99cc"));
//		Color to1 = Color.valueOf("#ffffff");
//		Color to2 = Color.valueOf("#7fd4ff");
//		Color to3 = Color.valueOf("#e5f6ff");
//		Color to4 = Color.valueOf("#99ddff");
//		
//    	layPane.setStyle("-fx-background-color: linear-gradient(" + web(c1.get()) + ", " + web(c2.get()) + "), radial-gradient(center 50% -40%, radius 200%, " + web(c3.get()) + " 45%, " + web(c4.get()) + " 50%);");
//    	layPane.scaleXProperty().set(1.2);
//    	layPane.scaleYProperty().set(1.2);
//
//	    c4.addListener((obs, oldColor, n) -> {
//	    	layPane.setStyle("-fx-background-color: linear-gradient(" + web(c1.get()) + ", " + web(c2.get()) + "), radial-gradient(center 50% -40%, radius 200%, " + web(c3.get()) + " 45%, " + web(c4.get()) + " 50%);");
//	    });
//	    
//	    KeyFrame kf1 = new KeyFrame(Duration.millis(2000), new KeyValue(c1, to1, Interpolator.EASE_BOTH));
//	    KeyFrame kf2 = new KeyFrame(Duration.millis(2000), new KeyValue(c2, to2, Interpolator.EASE_BOTH));
//	    KeyFrame kf3 = new KeyFrame(Duration.millis(2000), new KeyValue(c3, to3, Interpolator.EASE_BOTH));
//	    KeyFrame kf4 = new KeyFrame(Duration.millis(2000), new KeyValue(c4, to4, Interpolator.EASE_BOTH));
//	    KeyFrame sx = new KeyFrame(Duration.millis(2000), new KeyValue(layPane.scaleXProperty(), 1, Interpolator.EASE_BOTH));
//	    KeyFrame sy = new KeyFrame(Duration.millis(2000), new KeyValue(layPane.scaleYProperty(), 1, Interpolator.EASE_BOTH));
//	    
//	    Timeline timeline = new Timeline(kf1, kf2, kf3, kf4, sx, sy);
//	    timeline.setCycleCount(1);
//	    timeline.setOnFinished(e -> {
////	    	layPane.setStyle(null);
////        	this.updateColorMode();
//        });
//	    timeline.play();
//	}
//	
//	public String web( Color color){
//		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
//	}
//	
	
//	public void pulse() {
////		this.scale();
////		this.shadow();
////		this.event();
//		this.gredient();
////		getStyler().updateStyle(ColorMode.EDIT, ColorMode.EDIT);
//	}
//	
	
//	private void event() {
//		DropShadow dropShadow = new DropShadow();
//		dropShadow.setRadius(10);
//		dropShadow.setSpread(0.2);
//		dropShadow.setColor(Color.ORANGERED);		 
//		layPane.setEffect(dropShadow);
//        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(1000), new KeyValue(dropShadow.colorProperty(), Color.TRANSPARENT));
//        Timeline timeline = new Timeline(keyFrame2);
//        timeline.setAutoReverse(true);
//        timeline.setCycleCount(1);
//        timeline.setOnFinished(e -> {
////        	this.updateColorMode();
//        });
//        timeline.play(); 
//	}



//	private void shadow() {
//		DropShadow dropShadow = new DropShadow();
//		dropShadow.setRadius(5);
//		dropShadow.setSpread(0.5);
//		dropShadow.setColor(Color.ORANGERED);		 
//		layPane.setEffect(dropShadow);
//        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(200), new KeyValue(dropShadow.colorProperty(), Color.TRANSPARENT, Interpolator.EASE_BOTH));
//        Timeline timeline = new Timeline(keyFrame2);
//        timeline.setAutoReverse(true);
//        timeline.setCycleCount(6);
//        timeline.setOnFinished(e -> {
////        	this.updateColorMode();
//        });
//        timeline.play(); 
//	}
//
//
//
//	private void scale() {
////		nnode.separateLayers();
//		ScaleTransition st = new ScaleTransition(Duration.millis(400));
//		st.setNode(layPane);
//		st.setToX(2);
//		st.setToY(2);
//		st.setCycleCount(2);
//		st.setAutoReverse(true);
//		st.setOnFinished(e ->{
////			nnode.overlapLayers();
//		});		
//		st.play();
//	}


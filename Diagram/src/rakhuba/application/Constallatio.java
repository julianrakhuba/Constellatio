package rakhuba.application;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
//import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import rakhuba.activity.Select;
import rakhuba.generic.DLayer;
import rakhuba.generic.LAY;
import rakhuba.login.ConnectionStage;
import rakhuba.managers.DBManager;
import rakhuba.managers.FileManager;
import rakhuba.search.Search;
import rakhuba.sidePanel.InfoStage;
import rakhuba.status.ActivityMode;
import rakhuba.status.ConnectionStatus;
import rakhuba.status.Population;
import rakhuba.status.SqlType;
import rakhuba.status.VisualStatus;

public class Constallatio extends Application {	
	public Constallatio() {
		super();
		if (Desktop.getDesktop().isSupported(Action.APP_OPEN_FILE)) {
			Desktop.getDesktop().setOpenFileHandler(e -> {
				e.getFiles().forEach(autoOpenFile -> {
					Platform.runLater(() -> {
						if(this.getDBManager() != null && this.getDBManager().getActiveConnection() != null) {
							filemanager.openFile(autoOpenFile);
						}else {
							filemanager.setAutoOpenFile(autoOpenFile);
						}
					});
				});
			});
		}
	}

//	public AudioClip beep = new AudioClip(getClass().getResource("/app2.m4a").toExternalForm());
	private Stage stage;
//	public Label runLabel = new HeaderLabel("run","#1fff66");
	public ContextMenu funcContext = new ContextMenu();
	private Button testBtn = new Button("•");
	private final HBox centerBar = new HBox();
	private HBox centerBarA = new HBox();
	public HBox formaters = new HBox();
	public HBox bottomHideShowButtons = new HBox();
	public final Pane spacerA = new Pane();
	public final Pane spacerB = new Pane();
	public MultiFunctionButton multiFunctionButton = new MultiFunctionButton("", this);
	public FileManager filemanager = new FileManager(this);
	private DBManager dbManager;
	public ToolBar bottomToolBar = new ToolBar();
	private NScene nscene;
	public StringProperty title =  new SimpleStringProperty();
	public InfoStage infoStage;
	private MenuItem zoomInBtn = new MenuItem("In");
	private MenuItem zoomCenterBtn = new MenuItem("Center");
	private MenuItem zoomOutBtn = new MenuItem("Out");
	public CheckMenuItem autoFold =  new CheckMenuItem("Disable Auto Fold");
	public CheckMenuItem compactView =  new CheckMenuItem("Simple View");
	public MenuItem consoleMI =  new MenuItem("Console");
	public MenuBar menuBar  = new MenuBar();
	private CheckMenuItem editSchema = new CheckMenuItem("Edit Schema");
	private MenuItem saveSchema = new MenuItem("Save Schema");
	public CheckMenuItem dynamicSearch = new CheckMenuItem("Dynamic Search");
	private Menu menuFile = new Menu("File");
	public Menu menuEdit = new Menu("Edit");
	public Menu menuSchema = new Menu("Schema");
	public Menu menuView = new Menu("View");
	private MenuItem exit = new MenuItem("Exit");
	public Menu newFile = new Menu("New");
	public MenuItem open = new MenuItem("Open");
	public MenuItem export = new MenuItem("Export");
	public MenuItem close = new MenuItem("Close");
	public MenuItem closeAll = new MenuItem("Close All");
	public MenuItem save = new MenuItem("Save");
	public MenuItem saveAs = new MenuItem("Save As");
	public CheckMenuItem savePassword = new CheckMenuItem("Save Password");
	public MenuItem logout = new MenuItem("Logout");
	public MenuItem undo = new MenuItem("Undo");
	public MenuItem redo = new MenuItem("Redo");
	public MenuItem copy = new MenuItem("Copy");
	public MenuItem clear = new MenuItem("Clear");	
	public Menu addschema = new Menu("Add");
	public Menu removeschema = new Menu("Delete");
	private StackPane rootStackPane = new StackPane();
	public HBox searchHBox = new HBox(-15);
	public BorderPane borderPane = new BorderPane();
	private VBox fileMenuVBox = new VBox();
	private VBox searchPane = new VBox();
	private ConnectionStage connectionStage;
	public Search search = new Search(this);
	public Pane searchPlaceHolder = new Pane(search);	
	private ObservableList<MenuItem> disableMenus = FXCollections.observableArrayList();
	public  InfoLabel rowsCount = new InfoLabel("rows:");
	public  InfoLabel sumLabel = new InfoLabel("sum:");
	public  InfoLabel countLabel = new InfoLabel("count:");
	public  ConnectionLight light = new ConnectionLight();

	
	public Console console;
	public LocalDate expdt = LocalDate.of(2023, Month.DECEMBER, 31);
	
	public void  playSound() {
//		beep.play(0.03);
	}
	
	@Override
	public void start(Stage stage)  { 
		this.nscene = new NScene(rootStackPane, this);	
		this.stage = stage;
		console = new Console(this);		
//		SEPATE BUILDER 
		HBox.setHgrow(spacerA, Priority.SOMETIMES);
		HBox.setHgrow(spacerB, Priority.SOMETIMES);
		this.setTitle("[" + System.getProperty("java.home") + "]");

		consoleMI.setOnAction(e -> console.show());
		borderPane.setOnMouseClicked(e -> borderPane.requestFocus());
				
		this.configureMenu();
		bottomToolBar.getItems().addAll(rowsCount, sumLabel, countLabel,new Separator(), spacerA, centerBar, spacerB,new Separator(), bottomHideShowButtons, new Separator(), light);
//		[KEEP ORDER OR RELOAD]
		searchHBox.getChildren().addAll(multiFunctionButton, searchPlaceHolder);
		
		//•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••	
		funcContext.setOnHiding(e ->  {
			funcContext.getItems().clear();
			if(filemanager.getActiveNFile().getActivityMode() == ActivityMode.VIEW) {
				filemanager.getActiveNFile().getActivity().closeActivity();
				filemanager.getActiveNFile().setActivityMode(ActivityMode.SELECT);
				filemanager.getActiveNFile().infoPaneManager.deactivate();
			}
		});
		
		multiFunctionButton.setMinHeight(30);
		multiFunctionButton.setMaxHeight(30);
		multiFunctionButton.setMinWidth(40);
		multiFunctionButton.setMaxWidth(40);
	
		//•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		searchHBox.setAlignment(Pos.CENTER);
		disableMenus.addAll(addschema,removeschema,  export, close, closeAll, save, saveAs, undo, redo,  copy,clear, dynamicSearch, autoFold, zoomInBtn, zoomCenterBtn, zoomOutBtn, editSchema);	
		this.disableMenus(true);
		open.setDisable(true);
		newFile.setDisable(true);
		logout.setDisable(true);

		searchPane.getChildren().add(searchHBox);
		searchPane.getStyleClass().add("newSearchBar");
		
		fileMenuVBox.getChildren().addAll(menuBar, searchPane);
		borderPane.setTop(fileMenuVBox);
		borderPane.setBottom(bottomToolBar);
		rootStackPane.getChildren().add(borderPane);
		
		borderPane.setStyle("-fx-effect: innershadow(one-pass-box, gray, 5, 0.5, 0, 0);  -fx-background-color: #f5f5f5,  linear-gradient(from 0.0px 0.0px to 5.1px  0.0px, repeat, #ededed 5%, transparent 5%), linear-gradient(from 0.0px 0.0px to  0.0px 5.1px, repeat, #ededed 5%, transparent 5%);");
		centerBar.setSpacing(3.0);

		clear.setOnAction(e -> {
			if (filemanager.size() > 0) {
				filemanager.getActiveNFile().getActiveNmap().clear();
				filemanager.getActiveNFile().getUndoManager().saveUndoAction();
			}
		});
		
		testBtn.getStyleClass().add("layTestBtn");
		testBtn.setOnAction(e -> {
			boolean printdb = false;			
			if(printdb) {
				try {
					Connection con = this.getDBManager().getActiveConnection().getJDBC();
					DBTablePrinter.printResultSet(con.getMetaData().getTables("sakila", null, null, null));
				} catch (SQLException ee) {ee.printStackTrace();}
			}else {
//				TEST_CLICK
				LAY lay = filemanager.getActiveNFile().getActivity().getActiveLayer();
				if(lay != null) {
					lay.TEST_CLICK();
				}
			}
		});
		
		compactView.setSelected(true);
		compactView.setOnAction(e -> {
			if(!compactView.isSelected()) {
				filemanager.setCompactView(true);
			}else {
				filemanager.setCompactView(false);
			}
		});
		
		
		undo.setAccelerator( KeyCodeCombination(KeyCode.Z));
		undo.setOnAction(e -> {
			if(filemanager.size()>0) filemanager.getActiveNFile().getUndoManager().undo();
		});
		
		if(System.getProperty("os.name").startsWith("Mac")) {
			redo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN));
		}else {
			redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		}
		
		redo.setOnAction(e -> {
			if(filemanager.size()>0) filemanager.getActiveNFile().getUndoManager().redo();
		});
		
		copy.setAccelerator(KeyCodeCombination(KeyCode.C));
		copy.setOnAction(e -> {
			
			LAY lay = filemanager.getActiveNFile().getActivity().getActiveLayer();
			ClipboardContent content = new ClipboardContent();			
			if(lay != null && (lay.getSqlType() == SqlType.SQL || lay.isRoot())) {
				if(lay.getPopulation().getValue() == Population.UNPOPULATED) {
					if(lay instanceof DLayer) ((DLayer)lay).rebuildDFieldsAndJoins(); 
				}
				if(lay.getSqlType() == SqlType.SQL) {
					lay.recreateVersions();
					content.putString(lay.getSQL().toString());
				}else if (lay.isRoot()) {
					lay.refreshPivotCache();
					lay.recreateVersions();
					content.putString(lay.getSQLJ().toString());
				}
			}else {
				if(lay != null) content.putString("Can't copy to clipboard lay:  " + lay);
			}	
			Clipboard.getSystemClipboard().setContent(content);
		});
		
		zoomInBtn.setOnAction(e -> this.zoom(filemanager.getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 1.5));
		zoomCenterBtn.setOnAction(e -> this.zoom(1.0));
		zoomOutBtn.setOnAction(e -> this.zoom(filemanager.getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 0.75));
		savePassword.setSelected(true);
		
        stage.setResizable(true);
		stage.titleProperty().bindBidirectional(title);
//		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() *  0.85);
//		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.75);
		
		stage.setWidth(1600 * 0.8);
		stage.setHeight(900 * 0.8);

		open.setOnAction((e) ->  filemanager.openFileChooser());
		open.setAccelerator( KeyCodeCombination(KeyCode.O));
			
        save.setOnAction((e) -> filemanager.save());
        save.setAccelerator( KeyCodeCombination(KeyCode.S));
        
        saveAs.setOnAction((e) -> filemanager.saveAs());
		close.setOnAction(e -> filemanager.closeActiveFile());
		close.setAccelerator( KeyCodeCombination(KeyCode.W));
		closeAll.setOnAction(e -> filemanager.closeAllFiles());
		
		//
		this.stage.setOnCloseRequest(e -> {
			this.getDBManager().closeUserConnectionIfOpen();
		});
		
		stage.setScene(nscene);
		stage.setY(0);
		stage.setOnShown(e ->{
			this.getConnectionStage().show();
		});
		stage.show();
		infoStage = new InfoStage(stage);	
		
		infoStage.setOnCloseRequest(e ->{
			this.filemanager.getActiveNFile().infoPaneManager.setStatus(VisualStatus.HIDE);
			this.filemanager.getActiveNFile().infoPaneManager.hideSidePane();
    	});

		centerBarA.setSpacing(3.0);
		centerBarA.setAlignment(Pos.CENTER_LEFT);
//		centerBarA.getChildren().add(runLabel);
				
		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);
		
		if (!System.getProperty("os.name").startsWith("Mac")) {
			this.getParameters().getRaw().forEach((s) -> {
				filemanager.setAutoOpenFile(new File(s));
			});
		}
	}
	
	public void funcMenuClick(Node anchor) {
		if(!funcContext.isShowing()) {
			if(filemanager.getActiveNFile() != null) {
				filemanager.getActiveNFile().getActivity().rebuildFieldMenu();
			}
			if (!funcContext.isShowing() && funcContext.getItems().size() > 0 ) {
				
				if(anchor == null) {
					funcContext.show(searchPlaceHolder, Side.BOTTOM, 15, 2);
				}else {
					funcContext.show(anchor, Side.BOTTOM, 15, 2);
				}
			}
		}else {
			funcContext.hide();
		}
	}
	
	public void disableMenus(boolean b) {
    	disableMenus.forEach(m -> m.setDisable(b)); //HOW TO REFRESH APPLE MENU, JAVA ISSUE???
	}
	
	public ConnectionStage getConnectionStage() {
		if(connectionStage == null) {
			connectionStage = new ConnectionStage(this.getDBManager(), stage, this);
		}
		return connectionStage;
	}

	private void zoom(double zoomValue) {
		Timeline timeline = new Timeline(); 
		timeline.getKeyFrames().clear(); 
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(filemanager.getActiveNFile().getActiveNmap().schemaPane.scaleXProperty(), zoomValue, Interpolator.EASE_BOTH)));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(filemanager.getActiveNFile().getActiveNmap().schemaPane.scaleYProperty(), zoomValue, Interpolator.EASE_BOTH)));
		timeline.setCycleCount(1);
		timeline.playFromStart();
	}
	
	public void clearFileNewMenu() {
		 newFile.getItems().clear();
	}
	
	public void addNewSchemaToMenu(String schemaName){
        MenuItem schemaMenuItem = new MenuItem(schemaName);
        schemaMenuItem.setOnAction((e) -> {
        	filemanager.createNewFile(schemaName);
        });
        if(schemaName.equalsIgnoreCase("sakila")) {
        	schemaMenuItem.setAccelerator(KeyCodeCombination(KeyCode.N));
        }
        newFile.getItems().add(schemaMenuItem);
	}
	
	private KeyCodeCombination KeyCodeCombination(KeyCode key) {
		if(System.getProperty("os.name").startsWith("Mac")) {
			return new KeyCodeCombination(key, KeyCombination.META_DOWN);
		}else {
			return new KeyCodeCombination(key, KeyCombination.CONTROL_DOWN);
		}
	}
	
	private void configureMenu() {
		exit.setOnAction(e -> {try {this.stop();Platform.exit();System.exit(0);} catch (Exception e1) {e1.printStackTrace();}});
		export.setOnAction(e -> {
			if(this.filemanager.getActiveNFile().getActivity() instanceof Select && this.filemanager.getActiveNFile().getActivity().getActiveLayer() != null) this.filemanager.getActiveNFile().getActivity().getActiveLayer().exportToCsv();
		});
        editSchema.setOnAction(e -> {
        	if (editSchema.isSelected()){
        		filemanager.getActiveNFile().setActivityMode(ActivityMode.CONFIGURE);
        	}else{
        		filemanager.getActiveNFile().getActivity().closeActivity(); 
        		filemanager.getActiveNFile().setActivityMode(ActivityMode.SELECT);
        	}
        });
        saveSchema.setOnAction(e->{
        	filemanager.getActiveNFile().getActiveNmap().saveNnodeCoordinates();
    		this.getDBManager().getActiveConnection().getXMLBase().save_existing_or_crate_new();    		
        	filemanager.getActiveNFile().getActivity().closeActivity(); 
        	filemanager.getActiveNFile().setActivityMode(ActivityMode.SELECT);
        	editSchema.setSelected(false);
        });
        dynamicSearch.setSelected(true);
//        tableCount.setSelected(true);
        
    	logout.setOnAction(e -> {
			this.getDBManager().closeUserConnectionIfOpen();
			this.getConnectionStage().show();//login screen
		});
    	
		menuFile.getItems().addAll(newFile, open,close, closeAll, new SeparatorMenuItem(), save, saveAs, export,new SeparatorMenuItem(),savePassword,logout, new SeparatorMenuItem(), exit);
		menuEdit.getItems().addAll(undo, redo, new SeparatorMenuItem(), copy,new SeparatorMenuItem(),  clear);
		menuView.getItems().addAll(dynamicSearch, new SeparatorMenuItem(),autoFold, new SeparatorMenuItem(), zoomInBtn, zoomCenterBtn,zoomOutBtn,new SeparatorMenuItem(),compactView,new SeparatorMenuItem(), new SeparatorMenuItem(), this.consoleMI);
		menuSchema.getItems().addAll( addschema, removeschema , new SeparatorMenuItem(), editSchema,saveSchema);
		
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuSchema);
		menuBar.setUseSystemMenuBar(true);		
		
		menuFile.setOnShowing(e -> this.reconfigureFileMenu());//FILE MENU IS RECONFIGURED EVERY TIME IT OPENS
		menuSchema.setOnShowing(e -> this.reconfigureOnShowingSchemaMenu());
	}
	
	
	private void reconfigureFileMenu() {
		menuFile.getItems().clear();
		menuFile.getItems().addAll(newFile, open, close, closeAll, new SeparatorMenuItem());
		if(light.getStatus() == ConnectionStatus.CONNECTED) {
			filemanager.getOpenFiles().forEach(nfile -> {
				CheckMenuItem menuItem = new CheckMenuItem(nfile.getXMLFile().getName());
				menuItem.setSelected(nfile == filemanager.getActiveNFile());
				menuItem.setOnAction(mie ->  filemanager.selectNFile(nfile));
				menuFile.getItems().add(menuItem);
			});
		}
		menuFile.getItems().addAll(new SeparatorMenuItem(), save, saveAs, export, new SeparatorMenuItem(), savePassword,logout, new SeparatorMenuItem(), exit);
	}
	
	private void reconfigureOnShowingSchemaMenu() {
		menuSchema.getItems().clear();
		menuSchema.getItems().addAll( new SeparatorMenuItem());
		if(filemanager.getActiveNFile()!= null) {
			filemanager.getActiveNFile().getMaps().forEach((name,map) ->{
				CheckMenuItem mapMenuItem = new CheckMenuItem(name);
				if(map == filemanager.getActiveNFile().getActiveNmap() )  mapMenuItem.setSelected(true);
				mapMenuItem.setSelected(map == filemanager.getActiveNFile().getActiveNmap());
				mapMenuItem.setOnAction(e -> {
					filemanager.getActiveNFile().activateNmap(name);
				});
				menuSchema.getItems().add(mapMenuItem);
			});
		}
		addschema.getItems().clear();
		
		this.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema ->{
			if(filemanager.getActiveNFile() != null && !filemanager.getActiveNFile().getMaps().containsKey(schema)) {
				MenuItem menuItem = new MenuItem(schema);
				menuItem.setOnAction(e -> {
					filemanager.getActiveNFile().createNewMap(schema);
				});
				addschema.getItems().add(menuItem);
			}
		});
		removeschema.getItems().clear();
		this.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema ->{
			if(filemanager.getActiveNFile() != null && filemanager.getActiveNFile().getMaps().size() > 1 && filemanager.getActiveNFile().getMaps().containsKey(schema)) {
				MenuItem menuItem = new MenuItem(schema);
				menuItem.setOnAction(e -> {
					filemanager.getActiveNFile().removeSchema(schema);
				});
				removeschema.getItems().add(menuItem);
			}
		});
		menuSchema.getItems().addAll( new SeparatorMenuItem(), addschema, removeschema, new SeparatorMenuItem(), editSchema, saveSchema);
		
	}

	public NScene getNscene() {
		return nscene;
	}
	
	public void setTitle(String string) {
		title.setValue("Cōnstēllātiō 1.1c  " + string);
	}
	
	public Stage getStage() {
		return this.stage;
	}

	public CheckMenuItem getConfigureLayout() {
		return editSchema;
	}

	//test field
	public void setRegularSearch() {
		searchPlaceHolder.getChildren().clear();
		searchPlaceHolder.getChildren().add(search);
	}

	public void setFormulaSearch(Node formulaHBox) {
		searchPlaceHolder.getChildren().clear();
		searchPlaceHolder.getChildren().add(formulaHBox);		
	}
	
	public DBManager getDBManager() {
		if(dbManager == null) {
			dbManager = new DBManager(this);
		}
		return dbManager;
	}
}		
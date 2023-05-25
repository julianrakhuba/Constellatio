package application;


import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;


import launcher.FXapp;



import activity.Select;
import generic.DLayer;
import generic.LAY;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import login.ConnectionStage;
import managers.DBManager;
import managers.FileManager;
import search.Search;
import sidePanel.InfoStage;
import status.ActivityMode;
import status.ConnectionStatus;
import status.Population;
import status.SqlType;
import status.VisualStatus;
//@SuppressWarnings("exports")

public class Constellatio  {
	// public AudioClip beep = new
	// AudioClip(getClass().getResource("/app2.m4a").toExternalForm());
	private Stage stage;

	public ContextMenu funcContext = new ContextMenu();
	private Button testBtn = new Button("•");

	private DBManager dbManager;
	private NScene nscene;
	public StringProperty title = new SimpleStringProperty();
	private MenuItem zoomInBtn = new MenuItem("In");
	private MenuItem zoomCenterBtn = new MenuItem("Center");
	private MenuItem zoomOutBtn = new MenuItem("Out");
	public CheckMenuItem autoFold = new CheckMenuItem("Disable Auto Fold");
	public CheckMenuItem compactView = new CheckMenuItem("Simple View");
	public MenuItem consoleMI = new MenuItem("Console");
	public MenuBar menuBar = new MenuBar();
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
	private ObservableList<MenuItem> disableMenus = FXCollections.observableArrayList();
	private Console console;
	public LocalDate expdt = LocalDate.of(2023, Month.DECEMBER, 31);
	
	public MultiFunctionButton multiFunctionButton = new MultiFunctionButton("", this);
	private FileManager filemanager = new FileManager(this);
	public InfoStage infoStage;
	private Search search = new Search(this);

	public Pane searchPlaceHolder = new Pane(getSearch());

	

//•••••••••••••••••••••••••••••••••••••••••••••••••••••• TOOLBAR	
	private ToolBar bottomToolBar = new BottomToolBar();
	
	public InfoLabel rowsCount = new InfoLabel("rows:");
	public InfoLabel sumLabel = new InfoLabel("sum:");
	public InfoLabel countLabel = new InfoLabel("count:");	
	public ConnectionLight light = new ConnectionLight();
	
	private HBox centerBar = new HBox();
	private HBox centerBarA = new HBox();
	
	public Pane spacerA = new Pane();
	public Pane spacerB = new Pane();
	public HBox formaters = new HBox();
	public HBox bottomHideShowButtons = new HBox();
	
	
	private FXapp startFX;
	public Constellatio(FXapp startFX) {
		this.startFX = startFX;
	}
//	public Constellatio() {
//		super();
//		if (Desktop.getDesktop().isSupported(Action.APP_OPEN_FILE)) {
//			Desktop.getDesktop().setOpenFileHandler(e -> {
//				e.getFiles().forEach(autoOpenFile -> {
//					Platform.runLater(() -> {
//						if (this.getDBManager() != null && this.getDBManager().getActiveConnection() != null) {
//							filemanager.openFile(autoOpenFile);
//						} else {
//							filemanager.setAutoOpenFile(autoOpenFile);
//						}
//					});
//				});
//			});
//		}
//	}

	public void addNewSchemaToMenu(String schemaName) {
		MenuItem schemaMenuItem = new MenuItem(schemaName);
		schemaMenuItem.setOnAction((e) -> {
			getFilemanager().createNewFile(schemaName);
		});
		if (schemaName.equalsIgnoreCase("sakila")) {
			schemaMenuItem.setAccelerator(KeyCodeCombination(KeyCode.N));
		}
		newFile.getItems().add(schemaMenuItem);
	}

	public void clearFileNewMenu() {
		newFile.getItems().clear();
	}

	private void configureMenu() {
		exit.setOnAction(e -> {
			try {
				startFX.stop();
				Platform.exit();
				System.exit(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		export.setOnAction(e -> {
			if (this.getFilemanager().getActiveNFile().getActivity() instanceof Select
					&& this.getFilemanager().getActiveNFile().getActivity().getActiveLayer() != null)
				this.getFilemanager().getActiveNFile().getActivity().getActiveLayer().exportToCsv();
		});
		editSchema.setOnAction(e -> {
			if (editSchema.isSelected()) {
				getFilemanager().getActiveNFile().setActivityMode(ActivityMode.CONFIGURE);
			} else {
				getFilemanager().getActiveNFile().getActivity().closeActivity();
				getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
			}
		});
		saveSchema.setOnAction(e -> {
			getFilemanager().getActiveNFile().getActiveNmap().saveNnodeCoordinates();
			this.getDBManager().getActiveConnection().getXMLBase().save_existing_or_crate_new();
			getFilemanager().getActiveNFile().getActivity().closeActivity();
			getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
			editSchema.setSelected(false);
		});
		dynamicSearch.setSelected(true);
//        tableCount.setSelected(true);

		logout.setOnAction(e -> {
			this.getDBManager().closeUserConnectionIfOpen();
			this.getConnectionStage().show();// login screen
		});

		menuFile.getItems().addAll(newFile, open, close, closeAll, new SeparatorMenuItem(), save, saveAs, export,
				new SeparatorMenuItem(), savePassword, logout, new SeparatorMenuItem(), exit);
		menuEdit.getItems().addAll(undo, redo, new SeparatorMenuItem(), copy, new SeparatorMenuItem(), clear);
		menuView.getItems().addAll(dynamicSearch, new SeparatorMenuItem(), autoFold, new SeparatorMenuItem(), zoomInBtn,
				zoomCenterBtn, zoomOutBtn, new SeparatorMenuItem(), compactView, new SeparatorMenuItem(),
				new SeparatorMenuItem(), this.consoleMI);
		menuSchema.getItems().addAll(addschema, removeschema, new SeparatorMenuItem(), editSchema, saveSchema);

		menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuSchema);
		menuBar.setUseSystemMenuBar(true);

		menuFile.setOnShowing(e -> this.reconfigureFileMenu());// FILE MENU IS RECONFIGURED EVERY TIME IT OPENS
		menuSchema.setOnShowing(e -> this.reconfigureOnShowingSchemaMenu());
	}

	public void disableMenus(boolean b) {
		disableMenus.forEach(m -> m.setDisable(b)); // HOW TO REFRESH APPLE MENU, JAVA ISSUE???
	}

	public void funcMenuClick(Node anchor) {
		if (!funcContext.isShowing()) {
			if (getFilemanager().getActiveNFile() != null) {
				getFilemanager().getActiveNFile().getActivity().rebuildFieldMenu();
			}
			if (!funcContext.isShowing() && funcContext.getItems().size() > 0) {

				if (anchor == null) {
					funcContext.show(searchPlaceHolder, Side.BOTTOM, 15, 2);
				} else {
					funcContext.show(anchor, Side.BOTTOM, 15, 2);
				}
			}
		} else {
			funcContext.hide();
		}
	}

	public CheckMenuItem getConfigureLayout() {
		return editSchema;
	}

	private ConnectionStage getConnectionStage() {
		if (connectionStage == null) {
			connectionStage = new ConnectionStage(this.getDBManager(), stage, this);
		}
		return connectionStage;
	}

	public DBManager getDBManager() {
		if (dbManager == null) {
			dbManager = new DBManager(this);
		}
		return dbManager;
	}

	public NScene getNscene() {
		return nscene;
	}

	public Stage getStage() {
		return this.stage;
	}

	private KeyCodeCombination KeyCodeCombination(KeyCode key) {
		if (System.getProperty("os.name").startsWith("Mac")) {
			return new KeyCodeCombination(key, KeyCombination.META_DOWN);
		} else {
			return new KeyCodeCombination(key, KeyCombination.CONTROL_DOWN);
		}
	}

	public void playSound() {
//		beep.play(0.03);
	}

	private void reconfigureFileMenu() {
		menuFile.getItems().clear();
		menuFile.getItems().addAll(newFile, open, close, closeAll, new SeparatorMenuItem());
		if (light.getStatus() == ConnectionStatus.CONNECTED) {
			getFilemanager().getOpenFiles().forEach(nfile -> {
				CheckMenuItem menuItem = new CheckMenuItem(nfile.getXMLFile().getName());
				menuItem.setSelected(nfile == getFilemanager().getActiveNFile());
				menuItem.setOnAction(mie -> getFilemanager().selectNFile(nfile));
				menuFile.getItems().add(menuItem);
			});
		}
		menuFile.getItems().addAll(new SeparatorMenuItem(), save, saveAs, export, new SeparatorMenuItem(), savePassword,
				logout, new SeparatorMenuItem(), exit);
	}

	private void reconfigureOnShowingSchemaMenu() {
		menuSchema.getItems().clear();
		menuSchema.getItems().addAll(new SeparatorMenuItem());
		if (getFilemanager().getActiveNFile() != null) {
			getFilemanager().getActiveNFile().getMaps().forEach((name, map) -> {
				CheckMenuItem mapMenuItem = new CheckMenuItem(name);
				if (map == getFilemanager().getActiveNFile().getActiveNmap())
					mapMenuItem.setSelected(true);
				mapMenuItem.setSelected(map == getFilemanager().getActiveNFile().getActiveNmap());
				mapMenuItem.setOnAction(e -> {
					getFilemanager().getActiveNFile().activateNmap(name);
				});
				menuSchema.getItems().add(mapMenuItem);
			});
		}
		addschema.getItems().clear();

		this.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema -> {
			if (getFilemanager().getActiveNFile() != null && !getFilemanager().getActiveNFile().getMaps().containsKey(schema)) {
				MenuItem menuItem = new MenuItem(schema);
				menuItem.setOnAction(e -> {
					getFilemanager().getActiveNFile().createNewMap(schema);
				});
				addschema.getItems().add(menuItem);
			}
		});
		removeschema.getItems().clear();
		this.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema -> {
			if (getFilemanager().getActiveNFile() != null && getFilemanager().getActiveNFile().getMaps().size() > 1
					&& getFilemanager().getActiveNFile().getMaps().containsKey(schema)) {
				MenuItem menuItem = new MenuItem(schema);
				menuItem.setOnAction(e -> {
					getFilemanager().getActiveNFile().removeSchema(schema);
				});
				removeschema.getItems().add(menuItem);
			}
		});
		menuSchema.getItems().addAll(new SeparatorMenuItem(), addschema, removeschema, new SeparatorMenuItem(),
				editSchema, saveSchema);

	}

	public void setFormulaSearch(Node formulaHBox) {
		searchPlaceHolder.getChildren().clear();
		searchPlaceHolder.getChildren().add(formulaHBox);
	}

	// test field
	public void setRegularSearch() {
		searchPlaceHolder.getChildren().clear();
		searchPlaceHolder.getChildren().add(getSearch());
	}

	public void setTitle(String string) {
		title.setValue("Constellatio 1.1 modular  " + string);
	}

	public void start(Stage stage) {
		
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
		bottomToolBar.getItems().addAll(rowsCount, sumLabel, countLabel, new Separator(), spacerA, centerBar, spacerB,
				new Separator(), bottomHideShowButtons, new Separator(), light);
//		[KEEP ORDER OR RELOAD]
		searchHBox.getChildren().addAll(multiFunctionButton, searchPlaceHolder);

		// •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		funcContext.setOnHiding(e -> {
			funcContext.getItems().clear();
			if (getFilemanager().getActiveNFile().getActivityMode() == ActivityMode.VIEW) {
				getFilemanager().getActiveNFile().getActivity().closeActivity();
				getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
				getFilemanager().getActiveNFile().infoPaneManager.deactivate();
			}
		});

		multiFunctionButton.setMinHeight(30);
		multiFunctionButton.setMaxHeight(30);
		multiFunctionButton.setMinWidth(40);
		multiFunctionButton.setMaxWidth(40);

		// •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		searchHBox.setAlignment(Pos.CENTER);
		disableMenus.addAll(addschema, removeschema, export, close, closeAll, save, saveAs, undo, redo, copy, clear,
				dynamicSearch, autoFold, zoomInBtn, zoomCenterBtn, zoomOutBtn, editSchema);
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

		borderPane.setStyle(
				"-fx-effect: innershadow(one-pass-box, gray, 5, 0.5, 0, 0);  -fx-background-color: #f5f5f5,  linear-gradient(from 0.0px 0.0px to 5.1px  0.0px, repeat, #ededed 5%, transparent 5%), linear-gradient(from 0.0px 0.0px to  0.0px 5.1px, repeat, #ededed 5%, transparent 5%);");
		centerBar.setSpacing(3.0);

		clear.setOnAction(e -> {
			if (getFilemanager().size() > 0) {
				getFilemanager().getActiveNFile().getActiveNmap().clear();
				getFilemanager().getActiveNFile().getUndoManager().saveUndoAction();
			}
		});

		testBtn.getStyleClass().add("layTestBtn");
		testBtn.setOnAction(e -> {
			boolean printdb = false;
			if (printdb) {
				try {
					Connection con = this.getDBManager().getActiveConnection().getJDBC();
					DBTablePrinter.printResultSet(con.getMetaData().getTables("sakila", null, null, null));
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			} else {
//				TEST_CLICK
				LAY lay = getFilemanager().getActiveNFile().getActivity().getActiveLayer();
				if (lay != null) {
					lay.TEST_CLICK();
				}
			}
		});

		compactView.setSelected(true);
		compactView.setOnAction(e -> {
			if (!compactView.isSelected()) {
				getFilemanager().setCompactView(true);
			} else {
				getFilemanager().setCompactView(false);
			}
		});

		undo.setAccelerator(KeyCodeCombination(KeyCode.Z));
		undo.setOnAction(e -> {
			if (getFilemanager().size() > 0)
				getFilemanager().getActiveNFile().getUndoManager().undo();
		});

		if (System.getProperty("os.name").startsWith("Mac")) {
			redo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN, KeyCombination.META_DOWN));
		} else {
			redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		}

		redo.setOnAction(e -> {
			if (getFilemanager().size() > 0)
				getFilemanager().getActiveNFile().getUndoManager().redo();
		});

		copy.setAccelerator(KeyCodeCombination(KeyCode.C));
		copy.setOnAction(e -> {

			LAY lay = getFilemanager().getActiveNFile().getActivity().getActiveLayer();
			ClipboardContent content = new ClipboardContent();
			if (lay != null && (lay.getSqlType() == SqlType.SQL || lay.isRoot())) {
				if (lay.getPopulation().getValue() == Population.UNPOPULATED) {
					if (lay instanceof DLayer)
						((DLayer) lay).rebuildDFieldsAndJoins();
				}
				if (lay.getSqlType() == SqlType.SQL) {
					lay.recreateVersions();
					content.putString(lay.getSQL().toString());
				} else if (lay.isRoot()) {
					lay.refreshPivotCache();
					lay.recreateVersions();
					content.putString(lay.getSQLJ().toString());
				}
			} else {
				if (lay != null)
					content.putString("Can't copy to clipboard lay:  " + lay);
			}
			Clipboard.getSystemClipboard().setContent(content);
		});

		zoomInBtn.setOnAction(e -> this
				.zoom(getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 1.5));
		zoomCenterBtn.setOnAction(e -> this.zoom(1.0));
		zoomOutBtn.setOnAction(e -> this
				.zoom(getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty().getValue() * 0.75));
		savePassword.setSelected(true);

		stage.setResizable(true);
		stage.titleProperty().bindBidirectional(title);
//		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() *  0.85);
//		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.75);

		stage.setWidth(1600 * 0.8);
		stage.setHeight(900 * 0.8);

		open.setOnAction((e) -> getFilemanager().openFileChooser());
		open.setAccelerator(KeyCodeCombination(KeyCode.O));

		save.setOnAction((e) -> getFilemanager().save());
		save.setAccelerator(KeyCodeCombination(KeyCode.S));

		saveAs.setOnAction((e) -> getFilemanager().saveAs());
		close.setOnAction(e -> getFilemanager().closeActiveFile());
		close.setAccelerator(KeyCodeCombination(KeyCode.W));
		closeAll.setOnAction(e -> getFilemanager().closeAllFiles());

		//
		this.stage.setOnCloseRequest(e -> {
			this.getDBManager().closeUserConnectionIfOpen();
		});

		stage.setScene(nscene);
		stage.setY(0);
		stage.setOnShown(e -> {
			this.getConnectionStage().show();
		});
		stage.show();
		infoStage = new InfoStage(stage);

		infoStage.setOnCloseRequest(e -> {
			this.getFilemanager().getActiveNFile().infoPaneManager.setStatus(VisualStatus.HIDE);
			this.getFilemanager().getActiveNFile().infoPaneManager.hideSidePane();
		});

		centerBarA.setSpacing(3.0);
		centerBarA.setAlignment(Pos.CENTER_LEFT);
//		centerBarA.getChildren().add(runLabel);

		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);

		if (!System.getProperty("os.name").startsWith("Mac")) {
			startFX.getParameters().getRaw().forEach((s) -> {
				getFilemanager().setAutoOpenFile(new File(s));
			});
		}
	}

	private void zoom(double zoomValue) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().clear();
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(400),
						new KeyValue(getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleXProperty(),
								zoomValue, Interpolator.EASE_BOTH)));
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.millis(400),
						new KeyValue(getFilemanager().getActiveNFile().getActiveNmap().schemaPane.scaleYProperty(),
								zoomValue, Interpolator.EASE_BOTH)));
		timeline.setCycleCount(1);
		timeline.playFromStart();
	}

	public Search getSearch() {
		return search;
	}

	public FileManager getFilemanager() {
		return filemanager;
	}
}
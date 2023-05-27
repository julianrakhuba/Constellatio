package application;


import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import generic.LAY;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
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
import launcher.FxApp;
import login.ConnectionStage;
import managers.DBManager;
import managers.FileManager;
import search.Search;
import sidePanel.InfoStage;
import status.ActivityMode;
import status.VisualStatus;

public class Constellatio  {
	public NMenu menuBar;
	
	private ToolBar bottomToolBar = new BottomToolBar();
	
	public InfoLabel rowsCount = new InfoLabel("rows:");
	public InfoLabel sumLabel = new InfoLabel("sum:");
	public InfoLabel countLabel = new InfoLabel("count:");
	public Pane spacerA = new Pane();
	private HBox centerBar = new HBox();
	public Pane spacerB = new Pane();
	public HBox bottomHideShowButtons = new HBox();
	public ConnectionLight light = new ConnectionLight();
	
	
	private HBox centerBarA = new HBox();
	public HBox formaters = new HBox();


//	bottomToolBar.getItems().addAll(rowsCount, sumLabel, countLabel, new Separator(), spacerA, centerBar, spacerB,
//			new Separator(), bottomHideShowButtons, new Separator(), light);
	
	//•••••••••••••••••••••••••••••••••••••••••••••••••••••	
	public ContextMenu funcContext = new ContextMenu();

	public BorderPane borderPane = new BorderPane();
	//•••••••••••••••••••••••••••••••••••••••••••••••••••••• TOOLBAR	
	private ConnectionStage connectionStage;
	public Console console;
	private DBManager dbManager;
	public LocalDate expdt = LocalDate.of(2023, Month.DECEMBER, 31);
	private FileManager filemanager = new FileManager(this);
	private VBox fileMenuVBox = new VBox();
	public InfoStage infoStage;
	public MultiFunctionButton multiFunctionButton = new MultiFunctionButton("", this);
	private NScene nscene;
	private StackPane rootStackPane = new StackPane();
	public HBox searchHBox = new HBox(-15);
	private Search search = new Search(this);
	private VBox searchPane = new VBox();
	public Pane searchPlaceHolder = new Pane(getSearch());

	private Stage stage;	
	private FxApp startFX;
	private Button testBtn = new Button("•");
	private StringProperty title = new SimpleStringProperty();
	// public AudioClip beep = new
	// AudioClip(getClass().getResource("/app2.m4a").toExternalForm());
	
	
	public Constellatio(FxApp startFX) {
		this.startFX = startFX;
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

	ConnectionStage getConnectionStage() {
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

	public FileManager getFilemanager() {
		return filemanager;
	}

	public NScene getNscene() {
		return nscene;
	}

	public Search getSearch() {
		return search;
	}

	public Stage getStage() {
		return this.stage;
	}

	public KeyCodeCombination KeyCodeCombination(KeyCode key) {
		if (System.getProperty("os.name").startsWith("Mac")) {
			return new KeyCodeCombination(key, KeyCombination.META_DOWN);
		} else {
			return new KeyCodeCombination(key, KeyCombination.CONTROL_DOWN);
		}
	}

	public void playSound() {
//		beep.play(0.03);
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
		title.setValue("Constellatio 1.0.m3  " + string);
	}

	public void start(Stage stage) {
		
		this.nscene = new NScene(rootStackPane, this);
		this.stage = stage;
		console = new Console(this);
//		SEPATE BUILDER 
		HBox.setHgrow(spacerA, Priority.SOMETIMES);
		HBox.setHgrow(spacerB, Priority.SOMETIMES);
		this.setTitle("[" + System.getProperty("java.home") + "]");

		borderPane.setOnMouseClicked(e -> borderPane.requestFocus());
		
		
		menuBar = new NMenu(this);
		
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
		searchPane.getChildren().add(searchHBox);
		searchPane.getStyleClass().add("newSearchBar");

		fileMenuVBox.getChildren().addAll(menuBar, searchPane);
		borderPane.setTop(fileMenuVBox);
		borderPane.setBottom(bottomToolBar);
		rootStackPane.getChildren().add(borderPane);

		borderPane.setStyle(
				"-fx-effect: innershadow(one-pass-box, gray, 5, 0.5, 0, 0);  -fx-background-color: #f5f5f5,  linear-gradient(from 0.0px 0.0px to 5.1px  0.0px, repeat, #ededed 5%, transparent 5%), linear-gradient(from 0.0px 0.0px to  0.0px 5.1px, repeat, #ededed 5%, transparent 5%);");
		centerBar.setSpacing(3.0);

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



		stage.setResizable(true);
		stage.titleProperty().bindBidirectional(title);
//		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() *  0.85);
//		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.75);

		stage.setWidth(1600 * 0.8);
		stage.setHeight(900 * 0.8);

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

		formaters.setSpacing(3.0);
		formaters.setAlignment(Pos.CENTER_LEFT);
		centerBar.getChildren().addAll(centerBarA, formaters);

		if (!System.getProperty("os.name").startsWith("Mac")) {
			startFX.getParameters().getRaw().forEach((s) -> {
				getFilemanager().setAutoOpenFile(new File(s));
			});
		}
	}

	

	public NMenu getMenu() {
		return menuBar;
	}

	public FxApp getStartFX() {
		return startFX;
	}
}
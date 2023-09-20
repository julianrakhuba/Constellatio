package application;

import java.io.File;

import file.NFile;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import launcher.ConstellatioStart;
import login.ConnectionStage;
import managers.DBManager;
import managers.FileManager;
import status.VisualStatus;

public class Constellatio {
	private static String configurationPath = System.getProperty("user.home") + "/Library/Application Support/Constellatio/";
	private NMenu menuBar;
	private BottomBar bottomBar = new BottomBar(this);
	private SearchStackPane upperPane;
	public BorderPane appBorderPane = new BorderPane();

	private ConnectionStage connectionStage;
	private Console console;
	private Property<VisualStatus> showConsole = new SimpleObjectProperty<VisualStatus>(VisualStatus.HIDE);

	private DBManager dbManager;
	private FileManager filemanager = new FileManager(this);
	private StackPane consoleSP;

	private VBox fileMenuVBox = new VBox();
	private VBox vbox = new VBox(fileMenuVBox);
	private NScene nscene;

	private Stage stage;
	private ConstellatioStart startFX;
	private StringProperty title = new SimpleStringProperty();
	// public AudioClip beep = new AudioClip(getClass().getResource("/app2.m4a").toExternalForm());

	// for transparent stage only ??
	private double initX;
	private double initY;

	public Constellatio(ConstellatioStart startFX) {
		this.startFX = startFX;
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

	public Stage getStage() {
		return this.stage;
	}

	public void playSound() {
//		beep.play(0.03);
	}

	public void setTitle(String string) {
		title.setValue("Constellatio 1.0 Beta " + string);
	}

	public void start(Stage stg) {

		StackPane sp = new StackPane(appBorderPane);
		sp.setStyle("-fx-background-color: rgba(255,255,255, 0);");
		stage = stg;
//		this.setTitle("[" + System.getProperty("java.home") + "]");
		menuBar = new NMenu(this);
		this.getDBManager();// this is just to get confoguration earlier

		consoleSP = new StackPane();
		upperPane = new SearchStackPane(this);
		fileMenuVBox.getChildren().addAll(menuBar, upperPane);
		appBorderPane.setTop(vbox);
		appBorderPane.setBottom(bottomBar);
		appBorderPane.setOnMouseClicked(e -> appBorderPane.requestFocus());
		nscene = new NScene(sp, this);

		
		if (this.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			stage.initStyle(StageStyle.TRANSPARENT);
			appBorderPane.setStyle("-fx-background-color: rgba(255,255,255, 0);");
			vbox.setPadding(new Insets(1, 5, 0, 5));
			stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
			stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
//			stage.setWidth(1600 * 0.8);
//			stage.setHeight(900 * 0.8);
			nscene.setFill(Color.rgb(0, 0, 0, 0.5));// black faded
			fileMenuVBox.setStyle(""
					+ "-fx-padding: 5 5 5 5;"
					+ " -fx-background-color: rgba(0, 0, 0, 0.5); "
	        		+ "-fx-border-width: 0.5;"
	        		+ "-fx-border-color: derive(#1E90FF, 50%);"
	        		+ "-fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 8, 0.2, 0.0, 0.0);"
	        		+ "-fx-background-radius: 3;"
	        		+ "-fx-border-radius: 3;");
		} else {
			appBorderPane.setStyle("-fx-background-color: -fx-background;");
			stage.setWidth(1600 * 0.8);
			stage.setHeight(900 * 0.8);
			consoleSP.setStyle(" -fx-padding: 5 5 5 5; -fx-background-color: transparent;");
		}
		
		console = new Console(this);
		consoleSP.getChildren().add(console);

		stage.setResizable(true);
		stage.titleProperty().bindBidirectional(title);
		stage.setOnCloseRequest(e -> this.getDBManager().closeUserConnectionIfOpen());
		stage.setScene(nscene);
		stage.setY(0);
		stage.setOnShown(e -> this.getConnectionStage().show());
		stage.show();

		// TRANSPARENT STAGE ONLY •••••••••••••••••••••••••••••••••••••••••••
		// when mouse button is pressed, save the initial position of screen
		appBorderPane.setOnMousePressed(me -> {
			initX = me.getScreenX() - stage.getX();
			initY = me.getScreenY() - stage.getY();
		});

		// when screen is dragged, translate it accordingly
		appBorderPane.setOnMouseDragged(me -> {
			stage.setX(me.getScreenX() - initX);
			stage.setY(me.getScreenY() - initY);
		});

		if (!System.getProperty("os.name").startsWith("Mac")) {
			startFX.getParameters().getRaw().forEach((s) -> {
				getFilemanager().setAutoOpenFile(new File(s));
			});
		}
	}

	public NMenu getMenu() {
		return menuBar;
	}

	public ConstellatioStart getStartFX() {
		return startFX;
	}

	public BottomBar getBottomBar() {
		return bottomBar;
	}

	public SearchStackPane getUpperPane() {
		return upperPane;
	}

	public Console getConsole() {
//		if(console == null) console = new Console(this);
		return console;
	}

	public void updateGlassMode(boolean selected) {
		this.getDBManager().getConfiguration().save();// save every time?? bad design??		
	}

	public String getConfigurationPath() {
		return configurationPath;
	}

	public String getConfigurationBackUpPath() {
		return configurationPath + "backup/";
	}

	public void toggleConsole() {
		NFile file = this.getFilemanager().getActiveNFile();
		if(file != null) {			
			if(showConsole.getValue() == VisualStatus.SHOW ) {
				file.getQuadSplit().setBottomRight(null);
				showConsole.setValue(VisualStatus.HIDE);
				this.getConsole().routeBackToSystem();
			}else {
				file.getQuadSplit().setBottomRight(consoleSP);
				this.getConsole().routeToConsole();
				showConsole.setValue(VisualStatus.SHOW);
			}
		}
	}

	public void attachConsoleToFile(NFile nFile) {
		//this is ugly workaround because console is part of file :(, need to redesign
		if(showConsole.getValue() == VisualStatus.SHOW ) {
			nFile.getQuadSplit().setBottomRight(consoleSP);
		}		
	}
	

}
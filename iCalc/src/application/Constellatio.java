package application;


import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import launcher.FxApp;
import login.ConnectionStage;
import managers.DBManager;
import managers.FileManager;
import sidePanel.InfoStage;
import status.VisualStatus;

public class Constellatio  {
	private static String configurationPath = System.getProperty("user.home") + "/Library/Application Support/Constellatio/";

	private NMenu menuBar;
	private BottomBar bottomBar = new BottomBar(this);
	private UpperPane upperPane = new UpperPane(this);
	public BorderPane appBorderPane = new BorderPane();
	
	private ConnectionStage connectionStage;
	private  Console console;
	private DBManager dbManager;
	private FileManager filemanager = new FileManager(this);
	
	private VBox fileMenuVBox = new VBox();
	public InfoStage infoStage;
	private NScene nscene;

	private Stage stage;	
	private FxApp startFX;
	private StringProperty title = new SimpleStringProperty();
	// public AudioClip beep = new
	// AudioClip(getClass().getResource("/app2.m4a").toExternalForm());
	
	
	//for transparent stage only
    private double initX;
    private double initY;
    
	
	public Constellatio(FxApp startFX) {
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
		title.setValue("Constellatio 1.0.m3  " + string);
	}

	public void start(Stage stg) {
//		ImageView imageView = new ImageView( new Image(getClass().getResource("/myglass.png").toExternalForm()));
		ImageView imageView = new ImageView( new Image(getClass().getResource("/cyber.jpg").toExternalForm()));
		imageView.setOpacity(0.05);
		imageView.fitHeightProperty().bind(appBorderPane.heightProperty());
		ColorAdjust grayscale = new ColorAdjust();
		grayscale.setSaturation(-0.9);
//		GaussianBlur gaus = new GaussianBlur(2);
//		gaus.setInput(grayscale);		
//		imageView.setEffect(gaus);
		
		StackPane sp = new StackPane(imageView, appBorderPane);
		sp.setStyle("-fx-background-color: rgba(255,255,255, 0);");
		
//		schemaScrollPane.setStyle("-fx-background-image: url(\"myglass.png\");"
//		+ ""
//		+ ""
//		+ "-fx-opacity: 0.5;"
//		+ "  -fx-background-repeat: stretch;\r\n" n
//		+ "  -fx-background-position: center center;"
//		+ "");

		nscene = new NScene(sp, this);
		stage = stg;
		console = new Console(this);
//		this.setTitle("[" + System.getProperty("java.home") + "]");
		menuBar = new NMenu(this);
//		upperPane.getStyleClass().add("newSearchBar");
		fileMenuVBox.getChildren().addAll(menuBar, upperPane);
		
		appBorderPane.setTop(fileMenuVBox);
		appBorderPane.setBottom(bottomBar);
		appBorderPane.setOnMouseClicked(e -> appBorderPane.requestFocus());
		
		nscene.setFill(Color.rgb(255, 255, 255, 0.95));
		
//		nscene.setFill(Color.rgb(0, 0, 0, 0.0));

		
		this.getDBManager();//this is just to get confoguration earlier
		if(this.getMenu().getViewMenu().getTranslucentMenuItem().isSelected()) {
			stage.initStyle(StageStyle.TRANSPARENT);
			appBorderPane.setStyle("-fx-background-color: rgba(255,255,255, 0);");
//			borderPane.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.7), 20, 0, 0, 0);");

			stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
			stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
//			stage.setWidth(1600 * 0.8);
//			stage.setHeight(900 * 0.8);
		}else {
			appBorderPane.setStyle("-fx-effect: innershadow(one-pass-box, gray, 5, 0.5, 0, 0);  -fx-background-color: #f5f5f5,  linear-gradient(from 0.0px 0.0px to 5.1px  0.0px, repeat, #ededed 5%, transparent 5%), linear-gradient(from 0.0px 0.0px to  0.0px 5.1px, repeat, #ededed 5%, transparent 5%);");

//			borderPane.setStyle("-fx-background-color: rgba(255,255,255, 0);");

			stage.setWidth(1600 * 0.8);
			stage.setHeight(900 * 0.8);
		}
		
//		boolean transparen =  false;
//		if(transparen) {
//			stage.initStyle(StageStyle.TRANSPARENT);
//			borderPane.setStyle("-fx-background-color: rgba(255,255,255, 0);");
//		}else {
//			stage.initStyle(StageStyle.DECORATED);
//			borderPane.setStyle("-fx-effect: innershadow(one-pass-box, gray, 5, 0.5, 0, 0);  -fx-background-color: #f5f5f5,  linear-gradient(from 0.0px 0.0px to 5.1px  0.0px, repeat, #ededed 5%, transparent 5%), linear-gradient(from 0.0px 0.0px to  0.0px 5.1px, repeat, #ededed 5%, transparent 5%);");
//		}
		

		stage.setResizable(true);
		stage.titleProperty().bindBidirectional(title);
//		stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() *  0.85);
//		stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight() * 0.75);
//		stage.setWidth(1600 * 0.8);
//		stage.setHeight(900 * 0.8);
		stage.setOnCloseRequest(e -> this.getDBManager().closeUserConnectionIfOpen());
		stage.setScene(nscene);
		stage.setY(0);
		stage.setOnShown(e -> this.getConnectionStage().show());
		stage.show();
		
		
		//TRANSPARENT STAGE ONLY •••••••••••••••••••••••••••••••••••••••••••
        //when mouse button is pressed, save the initial position of screen
		appBorderPane.setOnMousePressed(me -> {
            initX = me.getScreenX() - stage.getX();
            initY = me.getScreenY() - stage.getY();
        });

        //when screen is dragged, translate it accordingly
		appBorderPane.setOnMouseDragged( me -> {
            stage.setX(me.getScreenX() - initX);
            stage.setY(me.getScreenY() - initY);
        });
		//TRANSPARENT STAGE ONLY •••••••••••••••••••••••••••••••••••••••••••

		
		infoStage = new InfoStage(stage);
		infoStage.setOnCloseRequest(e -> {
			this.getFilemanager().getActiveNFile().infoPaneManager.setStatus(VisualStatus.HIDE);
			this.getFilemanager().getActiveNFile().infoPaneManager.hideSidePane();
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

	public FxApp getStartFX() {
		return startFX;
	}

	public BottomBar getBottomBar() {
		return bottomBar;
	}

	public UpperPane getUpperPane() {
		return upperPane;
	}


	public Console getConsole() {
		return console;
	}


	public void updateTransluentMode(boolean selected) {
		System.out.println("set view mode: " + selected);
		this.getDBManager().getConfiguration().save();// save every time?? bad design??
	}


	public String getConfigurationPath() {
		return configurationPath;
	}


	public String getConfigurationBackUpPath() {
		return configurationPath + "backup/";
	}

}
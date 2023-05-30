package login;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import application.Constellatio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import managers.DBManager;

public class ConnectionStage extends Stage {
	private Rectangle background = new Rectangle(500, 250);
	private Rectangle backgroundB = new Rectangle(500, 300);

	private VBox vBox = new VBox();
	private StackPane rootpane = new StackPane(backgroundB, background, vBox);
	private Scene scene = new Scene(rootpane, Color.TRANSPARENT);
	private ComboBox<Login> dropDown = new ComboBox<Login>();

	private Button connectBtn = new Button("Connect");
	private Text title = new Text("login");
	private TextField username = new TextField();
	private PasswordField password = new PasswordField();
	private DropShadow shadow = new DropShadow();
	private Constellatio napp;
	
	public ConnectionStage(DBManager connectionManager, Stage primaryStage, Constellatio napp) {
		this.initStyle(StageStyle.TRANSPARENT);
		this.initModality(Modality.NONE);// to lock parent stage
		this.initOwner(primaryStage);// to lock parent stage
		this.napp = napp;
		
		background.setFill(new RadialGradient(-0.3, 135, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.TRANSPARENT), new Stop(1, Color.BLACK) }));	
		backgroundB.setFill(new RadialGradient(-0.3, 135, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.TRANSPARENT), new Stop(1, Color.valueOf("#e5f6ff")) }));		

//		scene.getStylesheets().add(getClass().getResource("/Graph.css").toExternalForm());
//		background.getStyleClass().add("newSearchBar");

		rootpane.setStyle("-fx-background-color: transparent;");
		this.setScene(scene);
//		this.centerOnScreen();
		
        // Calculate the center position of the parent Stage
        double centerXPosition = primaryStage.getX() + primaryStage.getWidth()/2d;
        double centerYPosition = primaryStage.getY() + primaryStage.getHeight()/2d;

        // Hide the pop-up stage before it is shown and becomes relocated
//        popUpStage.setOnShowing(ev -> popUpStage.hide());

        // Relocate the pop-up Stage
        this.setOnShown(ev -> {
        	this.setX(centerXPosition - this.getWidth()/2d);
        	this.setY(centerYPosition - this.getHeight()/2d);
//        	this.show();
        });
        
		// MOVEMENT
//        vBox.setOnMousePressed(mp -> { initX = mp.getScreenX() - this.getX(); initY = mp.getScreenY() - this.getY();});
//        vBox.setOnMouseDragged(md -> {this.setX(md.getScreenX() - initX); this.setY(md.getScreenY() - initY);});
		dropDown.setCellFactory(param -> new LoginCell());
		dropDown.setPromptText("select connection");
//		napp.getDataBaseManager().getLogins();
		dropDown.setItems(this.napp.getDBManager().getConfiguration().getLoginList());
		dropDown.setMaxWidth(400);
		dropDown.setMinWidth(400);
		
		connectBtn.setDefaultButton(true);
		connectBtn.setOnAction(ed -> {
			if(!this.isExpired() 
//					|| System.getProperty("user.name").equals("julianrakhuba")
					) {
				Login login = dropDown.getSelectionModel().getSelectedItem();
				if(login!= null) {
					login.setUsername(username.getText());
					login.setPassword(password.getText());
					connectionManager.activateConnection(login);
					//Close on successful login
					if(connectionManager.getActiveConnection().getJDBC() != null) {
						this.close();
						napp.getFilemanager().openAutoFile();
					}else {
						//Handle login fail here??;
					}
				}
			}else {
				connectBtn.setText("expired");
			}						
		});

		title.setFill(Color.WHITE);
//		title.setEffect(new Lighting());
//		title.setBoundsType(TextBoundsType.VISUAL);
		title.setStyle("-fx-font-size: 19;");
//		title.setFont(Font.font(Font.getDefault().getFamily(), 20));

		username.setPromptText("username");
		username.setStyle(" -fx-prompt-text-fill: #ade0ff;");
		username.setMaxWidth(400);
		
		password.setPromptText("password");
		password.setStyle(" -fx-prompt-text-fill: #ade0ff;");
		password.setMaxWidth(400);

		shadow.setColor(Color.valueOf("#61c2ff"));
		shadow.setBlurType(BlurType.GAUSSIAN);
		shadow.setSpread(1);
		shadow.setRadius(1);

		dropDown.valueProperty().addListener((a, b, c) -> {
			if(c.getPassword().equals("")) {
				napp.getMenu().getFileMenu().getSavePasswordMenuItem().setSelected(false);
			}else {
				napp.getMenu().getFileMenu().getSavePasswordMenuItem().setSelected(true);
			}
			username.setText(c.getUsername());
			password.setText(c.getPassword());
		});

		vBox.getChildren().addAll(title, dropDown, username, password, connectBtn);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(20, 10, 10, 10));
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle("-fx-background-color: transparent; -fx-border-width: 0.5 ;-fx-border-color: white ;");
    	vBox.setEffect(shadow);
	}
	
	private boolean isExpired() {
		LocalDate webdt = LocalDate.now();
		LocalDate expdt = LocalDate.of(2023, Month.DECEMBER, 31);

//		new Date(new NTPUDPClient().getTime(InetAddress.getByName("time-a.nist.gov")).getMessage().getTransmitTimeStamp().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();			
		int years = Period.between(webdt, expdt).getYears();
	    int months = Period.between(webdt, expdt).getMonths();
	    int days = Period.between(webdt, expdt).getDays();
	    long p2 = ChronoUnit.DAYS.between(webdt, expdt);
		napp.setTitle("("+ p2 + " days left)  [" + System.getProperty("java.home") + "]" );
		if(years < 0 || months < 0 || days < 0) {
			return true;
		}else {
			return false;
		}
	}
	
}

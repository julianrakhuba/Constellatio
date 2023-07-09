package application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import status.VisualStatus;

public class Console {
	private TextArea textArea = new TextArea();	
	private VBox vbox = new VBox(textArea);
	private Tab console = new Tab("console", vbox);

	private PrintStream errorStream;
	private PrintStream outStream;
	private Constellatio napp;

	public Console(Constellatio napp) {
		this.napp = napp;
		VBox.setVgrow(textArea, Priority.ALWAYS);
		this.outStream = System.out;
		this.errorStream = System.err;
		
		console.setOnCloseRequest(e -> {
			console.getTabPane().requestFocus();
			napp.getFilemanager().getActiveNFile().tabManager.removeTab(console);
			this.routeBackToSystem();
		});
	}
	
	public void clear() {
		textArea.clear();
	}

	private void routeToConsole() {
		
	    OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendText(String.valueOf((char)b));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
	}
	
    public void appendText(String valueOf) {
        Platform.runLater(() -> textArea.appendText(valueOf));
    }
	
	private void routeBackToSystem() {
		System.setOut(outStream);
		System.setErr(errorStream);
		
		
		System.out.println("db info  ------------------------------------------------------------------------------------------");
		try {
			DatabaseMetaData dbmeta = napp.getDBManager().getActiveConnection().getJDBC().getMetaData();
			System.out.println("db name: " + dbmeta.getDatabaseProductName());
			System.out.println("db version: " + dbmeta.getDatabaseProductVersion());
			System.out.println("db driver: " + dbmeta.getDriverName());
			System.out.println("db user: " + dbmeta.getUserName());
			
//			DBTablePrinter.printResultSet(dbmeta.getTablePrivileges());
//			
		} catch (SQLException e) {e.printStackTrace();}
		System.out.println("end db info  ------------------------------------------------------------------------------------------");
	}

	public void show() {
		if(napp.getFilemanager().getActiveNFile() != null) {
			napp.getFilemanager().getActiveNFile().tabManager.selectTab(console);
			if(napp.getFilemanager().getActiveNFile().tabManager.getStatus() == VisualStatus.UNAVALIBLE) napp.getFilemanager().getActiveNFile().tabManager.showGrid();
			this.routeToConsole();
		}
		System.out.println("java info  ------------------------------------------------------------------------------------------");
		System.getProperties().forEach((a,b) -> System.out.println(a+" | " + b));
		System.out.println("end java info  ------------------------------------------------------------------------------------------");
	}
}

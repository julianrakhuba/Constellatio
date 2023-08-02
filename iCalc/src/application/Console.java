package application;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
//import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Console extends VBox {
	private TextArea textArea = new TextArea();	
	private PrintStream errorStream;
	private PrintStream outStream;
	@SuppressWarnings("unused")
	private Constellatio napp;
	
	//new queue
	private ObservableList<String> queue = FXCollections.observableArrayList();
	private SequentialTransition sequentialTransition = new SequentialTransition();

	//
	boolean reroutelogs = true;
	
	public Console(Constellatio napp) {
		this.napp = napp;
		VBox.setVgrow(textArea, Priority.ALWAYS);
		this.outStream = System.out;
		this.errorStream = System.err;
		textArea.setWrapText(true);
		textArea.setEditable(true);
		this.getChildren().addAll(textArea);
	}
	
	public void clear() {
		textArea.clear();
	}

	public void routeToConsole() {
		if(reroutelogs) {
			OutputStream out = new OutputStream() {
	            @Override
	            public void write(int b) throws IOException {
	                appendText(String.valueOf((char)b));
	            }
	        };
	        System.setOut(new PrintStream(out, true));
	        System.setErr(new PrintStream(out, true));
	        
	        addTextToQue("Forward system output to Constellatio");
//			System.getProperties().forEach((a,b) ->  addTextToQue(a+":  " + b));
//	        addTextToQue("java info  ----------------------------------------------------------------------------------------------");
		}
	}
	
	public void appendText(String str) {
		textArea.appendText(str);
//        Platform.runLater(() -> textArea.appendText(str));
	}
	
	public void routeBackToSystem() {
		if(reroutelogs) {
			System.setOut(outStream);
			System.setErr(errorStream);
	        addTextToQue("Route Output to System");

//			System.out.println("db user");
//			try {
//				DatabaseMetaData dbmeta = napp.getDBManager().getActiveConnection().getJDBC().getMetaData();
//				System.out.println("db name: " + dbmeta.getDatabaseProductName());
//				System.out.println("db version: " + dbmeta.getDatabaseProductVersion());
//				System.out.println("db driver: " + dbmeta.getDriverName());
//				System.out.println("db user: " + dbmeta.getUserName());
//			} catch (SQLException e) {e.printStackTrace();}
		}
	}

//	public void show() {
//		if(napp.getFilemanager().getActiveNFile() != null) {
//			this.routeToConsole();
//		}
//		System.out.println("java info  ----------------------------------------------------------------------------------------------");
//		System.getProperties().forEach((a,b) -> System.out.println(a+" | " + b));
//		System.out.println("end java info  ------------------------------------------------------------------------------------------");
//	}

	public void addTextToQue(String query) {
		if(sequentialTransition.getStatus() == Status.STOPPED && queue.size() == 0) {
			queue.add(query);
			this.feedFirst();
		}else {
			queue.add(query);
		}
	}

	private void feedFirst() {
		if(queue.size()>0) {
//			String item = queue.remove(queue.size() -1);
//			String item = queue.remove(0);
			this.feedItem(queue.remove(0));
//	      	textArea.appendText("\n");
		}
	}
	
	private void feedItem(String str) {
		for (String line : str.split("\n")) {//new line will be put back on in here
			if(str.length() < 2000) {
		        for (char c : line.toCharArray()) {
					sequentialTransition.getChildren().add(new Timeline(new KeyFrame(Duration.seconds(0.005), e -> textArea.appendText(Character.toString(c)))));
		        }
				sequentialTransition.getChildren().add(new Timeline(new KeyFrame(Duration.seconds(0.005), e -> textArea.appendText("\n"))));
			}else {
				sequentialTransition.getChildren().add(new Timeline(new KeyFrame(Duration.seconds(0.03), e -> textArea.appendText(line + "\n"))));
			}			
		}
		
		sequentialTransition.setOnFinished(e ->{
//	      	textArea.appendText("\n");
	      	sequentialTransition.getChildren().clear();
	      	this.feedFirst();
		});  	
		sequentialTransition.play();
		
	}
}







//new
//package application;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import elements.ELM;
//import javafx.animation.Animation;
//import javafx.animation.KeyFrame;
//import javafx.animation.SequentialTransition;
//import javafx.animation.Timeline;
//import javafx.application.Platform;
////import javafx.application.Platform;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ListChangeListener;
//import javafx.collections.ObservableList;
//import javafx.scene.control.TextArea;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import javafx.util.Duration;
//
//public class Console extends VBox {
//	private TextArea textArea = new TextArea();	
//
//	private PrintStream errorStream;
//	private PrintStream outStream;
//	private Constellatio napp;
//	
//	private ObservableList<String> queue = FXCollections.observableArrayList();
//
//
//	public Console(Constellatio napp) {
//		this.napp = napp;
//		VBox.setVgrow(textArea, Priority.ALWAYS);
//		this.outStream = System.out;
//		this.errorStream = System.err;
//		textArea.setWrapText(true);
//		textArea.setEditable(true);
//		this.getChildren().addAll(textArea);
////		close.setOnAction(e -> {
////			napp.getFilemanager().getActiveNFile().getQuadSplit().setBottomRight(null);
////			this.routeBackToSystem();
////		});
//		
////		textArea.setStyle("-fx-effect: innershadow(three-pass-box, red, 10, 0, 0, 0);"
////				+ "-fx-background-color: white; "
////	    		+ "-fx-border-color: transparent;"
////	    		+ "-fx-background-radius: 7;"
////	    		+ "-fx-border-radius: 7;");
//		
//		queue.addListener((ListChangeListener<? super String>) change -> {
//			change.next();			
//			textArea.appendText("\n •••••••••••••••••••••••  Added to que: " + change.getAddedSize() );
//		});
//
//	
//	}
//	
//	public void clear() {
//		textArea.clear();
//	}
//
//	public void routeToConsole() {
//		
////	    OutputStream out = new OutputStream() {
////            @Override
////            public void write(int b) throws IOException {
////                appendText(String.valueOf((char)b));
////            }
////        };
////        System.setOut(new PrintStream(out, true));
////        System.setErr(new PrintStream(out, true));
//	}
//	
//	private void appendText(String str) {
//		textArea.appendText(str.length()  + " [append]\n");
////		textArea.clear();
////    	textArea.appendText(valueOf);
////        Platform.runLater(() -> textArea.appendText(str));
//
////		if(queue.size() >0) {
////			queue.remove(queue.size() - 1);
////		}
//		
//		
//        SequentialTransition sequentialTransition = new SequentialTransition();
//		String[] lines = str.split("\n");
//		for (String line : lines) {
////			char[] charArray = line.toCharArray();
////	        for (char c : charArray) {
////				sequentialTransition.getChildren().add(new Timeline(new KeyFrame(Duration.seconds(0.1), e -> textArea.appendText(Character.toString(c)))));
////	        }
//			sequentialTransition.getChildren().add(new Timeline(new KeyFrame(Duration.seconds(0.1), e -> textArea.appendText(line + "\n"))));
//		}
//		
//    	sequentialTransition.setOnFinished(e ->{
//        	textArea.appendText("•••\n");
//
//    	});
////    	sequentialTransition.gets
//    	
//		sequentialTransition.play();
//	}
//	
//	public void routeBackToSystem() {
////		System.setOut(outStream);
////		System.setErr(errorStream);
////		
////		
////		System.out.println("db info  ------------------------------------------------------------------------------------------");
////		try {
////			DatabaseMetaData dbmeta = napp.getDBManager().getActiveConnection().getJDBC().getMetaData();
////			System.out.println("db name: " + dbmeta.getDatabaseProductName());
////			System.out.println("db version: " + dbmeta.getDatabaseProductVersion());
////			System.out.println("db driver: " + dbmeta.getDriverName());
////			System.out.println("db user: " + dbmeta.getUserName());
//////			DBTablePrinter.printResultSet(dbmeta.getTablePrivileges());			
////		} catch (SQLException e) {e.printStackTrace();}
////		System.out.println("end db info  ------------------------------------------------------------------------------------------");
//	}
//
//	public void show() {
//		if(napp.getFilemanager().getActiveNFile() != null) {
////			napp.getFilemanager().getActiveNFile().tabManager.selectTab(console);
////			napp.getFilemanager().getActiveNFile().getQuadSplit().setBottomRight(vbox);
////			if(napp.getFilemanager().getActiveNFile().tabManager.getStatus() == VisualStatus.UNAVALIBLE) napp.getFilemanager().getActiveNFile().tabManager.showGrid();
//			this.routeToConsole();
//		}
//		System.out.println("java info  ----------------------------------------------------------------------------------------------");
//		System.getProperties().forEach((a,b) -> System.out.println(a+" | " + b));
//		System.out.println("end java info  ------------------------------------------------------------------------------------------");
//	}
//
//	public void addTextToQue(String query) {
//		queue.add(query);
//		
//		this.appendText(query);
//	}
//}

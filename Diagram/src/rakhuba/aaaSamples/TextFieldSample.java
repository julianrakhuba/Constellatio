package rakhuba.aaaSamples;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 

public class TextFieldSample extends Application {
    TextArea text = new TextArea("Text");

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        
        VBox vbox = new VBox();
        Button btn = new Button("test");
        btn.setOnAction(e ->{
        	System.out.println("button click!");
        });
        
//        TextArea text = new TextArea("Text");
        vbox.getChildren().addAll(btn, text);
        root.getChildren().add(vbox);
        
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
        Platform.runLater(() -> text.appendText(valueOf));
    }
 
    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
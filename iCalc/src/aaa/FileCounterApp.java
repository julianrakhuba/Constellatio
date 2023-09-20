package aaa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class FileCounterApp extends Application {

    private Label countLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Counter App");

        countLabel = new Label("Files Count: 0");

        Button countButton = new Button("Count Files");
        countButton.setOnAction(e -> countFiles());

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(countLabel, countButton);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void countFiles() {
//        String homeFolder = System.getProperty("user.home"+ "/test");
//        File folder = new File(homeFolder);
        
        String destination = System.getProperty("user.home") + "/test";
		File folder = new File(destination);
        
        int fileCount = countFilesInFolder(folder);
        countLabel.setText("Files Count: " + fileCount);
    }

    private int countFilesInFolder(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    count++;
                	System.out.println(count + ") " + file.getName() );
                } else if (file.isDirectory()) {
                    count += countFilesInFolder(file);
                }
            }
        }
        return count;
    }
}

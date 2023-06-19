package aaa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("unused")
public class TransparentResizableStage extends Application {

    private static final int RESIZE_MARGIN = 5;

    private double xOffset = 0;
    private double yOffset = 0;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage primaryStage) {
        // Create a transparent stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Create a resizable region as the root node
        Region root = new Region();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        // Create the scene with a transparent fill
        Scene scene = new Scene(root, Color.TRANSPARENT);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Enable resizing by dragging the edges
        enableResize(primaryStage, root);

        // Show the stage
        primaryStage.show();
    }

    private void enableResize(Stage stage, Region region) {
        region.setOnMousePressed(event -> {
            xOffset = event.getScreenX() - stage.getX();
            yOffset = event.getScreenY() - stage.getY();
        });

        region.setOnMouseDragged(event -> {
//            if (event.getX() >= region.getWidth() - RESIZE_MARGIN && event.getY() >= region.getHeight() - RESIZE_MARGIN) {
                stage.setWidth(event.getScreenX() - stage.getX());
                stage.setHeight(event.getScreenY() - stage.getY());
//            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

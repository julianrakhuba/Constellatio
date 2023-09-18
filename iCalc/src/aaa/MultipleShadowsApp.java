package aaa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class MultipleShadowsApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 400);

        Path path = new Path();
        path.setStroke(Color.BLACK);

        // Define the starting and ending points
        MoveTo start = new MoveTo(50, 50); // Starting point (x=50, y=50)
        LineTo end = new LineTo(250, 250); // Ending point (x=250, y=250)

        // Define intermediate points
        LineTo middle1 = new LineTo(100, 100);
        LineTo middle2 = new LineTo(150, 150);
        LineTo middle3 = new LineTo(200, 200);

        // Add all segments to the path
        path.getElements().addAll(start, middle1, middle2, middle3, end);

        // Create multiple DropShadow effects
        DropShadow shadow1 = new DropShadow();
        shadow1.setColor(Color.RED);
        shadow1.setOffsetX(5);
        shadow1.setOffsetY(5);

        DropShadow shadow2 = new DropShadow();
        shadow2.setColor(Color.BLUE);
        shadow2.setOffsetX(-5);
        shadow2.setOffsetY(-5);

        // Apply the shadows to the path
        path.setEffect(shadow1);
        path.setStrokeWidth(6); // Set the stroke width for the entire path

        // Create a second path for the thin end
        Path thinEnd = new Path();
        thinEnd.getElements().addAll(start, middle1);
        thinEnd.setStrokeWidth(2); // Set the stroke width for the thin end
        thinEnd.setStroke(Color.BLACK); // Set the color for the thin end
        thinEnd.setEffect(shadow2);

        root.getChildren().addAll(path, thinEnd);

        primaryStage.setTitle("Multiple Shadows");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

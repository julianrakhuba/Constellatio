package aaa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class CustomCurvedShapeExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create a custom shape using Path with CubicCurveTo
        Path customShape = new Path();
        customShape.getElements().addAll(
            new MoveTo(50, 0),
            new CubicCurveTo(100, 0, 100, 100, 50, 100), // Cubic Bezier curve
            new CubicCurveTo(0, 100, 0, 0, 50, 0),     // Cubic Bezier curve
            new ClosePath()
        );
        customShape.setFill(Color.ORANGE);
        customShape.setStroke(Color.BLACK);

        Pane root = new Pane();
        root.getChildren().add(customShape);

        Scene scene = new Scene(root, 100, 100);
        primaryStage.setTitle("CubicCurveTo Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

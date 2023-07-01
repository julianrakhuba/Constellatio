//package aaa;
//
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
////import javafx.scene.paint.Color;
////import javafx.scene.shape.Circle;
//import javafx.stage.Stage;
//import logic.LogicArcs;
//
//public class HollowCircleSlices extends Application {
//
//    private static final int WINDOW_WIDTH = 400;
//    private static final int WINDOW_HEIGHT = 400;
////    private static final double CENTER_X = WINDOW_WIDTH / 2;
////    private static final double CENTER_Y = WINDOW_HEIGHT / 2;
//
//    @SuppressWarnings("exports")
//	public void start(Stage primaryStage) {
//    	Group gp = new Group();
//    	
////    	LogicArcs level = new LogicArcs(CENTER_X, CENTER_Y, 20, 1);
////    	LogicArcs level2 = new LogicArcs(CENTER_X, CENTER_Y, 30, 2);
////    	LogicArcs level = new LogicArcs(CENTER_X, CENTER_Y, 10, 1);
////    	LogicArcs level2 = new LogicArcs(CENTER_X, CENTER_Y, 20, 2);
////    	LogicArcs level = new LogicArcs(40, 40, 10, 1);
////    	LogicArcs level2 = new LogicArcs(40, 40, 20, 2);
//
////    	Circle cir = new Circle(CENTER_X, CENTER_Y, 15);
////    	cir.setFill(Color.WHITESMOKE);
////		cir.setStyle("-fx-background-radius: 20px; -fx-border-color: white; -fx-border-width: 2px; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-effect: dropshadow(gaussian, derive(#1E90FF, 5%) , 7, 0.4, 0.0, 0.0); -fx-border-radius: 20px;");
//
////    	cir.setStyle();
//    	gp.getChildren().addAll(level2, level);
//        Scene scene = new Scene(gp, WINDOW_WIDTH, WINDOW_HEIGHT);
//        
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
////
////package aaa;
////
////import javafx.application.Application;
////import javafx.scene.Group;
////import javafx.scene.Scene;
////import javafx.scene.paint.Color;
////import javafx.scene.shape.Arc;
////import javafx.scene.shape.ArcType;
////import javafx.scene.shape.StrokeLineCap;
////import javafx.stage.Stage;
////
////public class HollowCircleSlices extends Application {
////
////    private static final int WINDOW_WIDTH = 400;
////    private static final int WINDOW_HEIGHT = 400;
////    private static final double CENTER_X = WINDOW_WIDTH / 2;
////    private static final double CENTER_Y = WINDOW_HEIGHT / 2;
////    private static final double OUTER_RADIUS = 20;
//////    private static final double INNER_RADIUS = 50;
////    private static final int NUM_SLICES = 3;
////    private static final double STROKE_WIDTH = 15;
////
////    @SuppressWarnings("exports")
////	public void start(Stage primaryStage) {
////        Group root = new Group();
////        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
////
////        // Calculate the angle for each slice
////        double anglePerSlice = 360.0 / NUM_SLICES;
////
////        // Create and add the slices to the root group
////        for (int i = 0; i < NUM_SLICES; i++) {
////            double startAngle = i * anglePerSlice - anglePerSlice / 2;
////            double length = anglePerSlice * 0.98; // Length of each slice (90% of anglePerSlice)
////
////            Arc slice = new Arc(CENTER_X, CENTER_Y, OUTER_RADIUS, OUTER_RADIUS, startAngle, length);
////            slice.setType(ArcType.OPEN);
////            slice.setFill(null);
////            slice.setStroke(Color.DODGERBLUE);
////            slice.setStrokeWidth(STROKE_WIDTH);
////            slice.setStrokeLineCap(StrokeLineCap.BUTT);
////            
////            
////            
////            
////            root.getChildren().add(slice);
////        }
////
////
////        primaryStage.setTitle("Hollow Circle Slices");
////        primaryStage.setScene(scene);
////        primaryStage.show();
////    }
////
////    public static void main(String[] args) {
////        launch(args);
////    }
////}
////
//
//

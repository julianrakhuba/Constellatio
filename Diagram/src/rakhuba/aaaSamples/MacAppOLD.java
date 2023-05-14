package rakhuba.aaaSamples;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

/** Example of how a cubic curve works, drag the anchors around to change the curve. */
public class MacAppOLD extends Application {
  public static void main(String[] args) throws Exception { launch(args); }
  @Override public void start(final Stage stage) throws Exception {
    CubicCurve curve = createStartingCurve();

    Line controlLine1 = new BoundLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
    Line controlLine2 = new BoundLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(),   curve.endYProperty());

    Anchor start    = new Anchor(Color.PALEGREEN, curve.startXProperty(),    curve.startYProperty());
    Anchor control1 = new Anchor(Color.GOLD,      curve.controlX1Property(), curve.controlY1Property());
    Anchor control2 = new Anchor(Color.GOLDENROD, curve.controlX2Property(), curve.controlY2Property());
    Anchor end      = new Anchor(Color.TOMATO,    curve.endXProperty(),      curve.endYProperty());
    
    stage.setTitle("Cubic Curve Manipulation Sample"); 
    
    Button bb = new Button("Test");
	  bb.setOnAction(e ->{
//		  TranslateTransition tt = new TranslateTransition(Duration.seconds(4), control1);		  
//		  tt.setFromX(30);
//		  tt.setToX(300);
//		  tt.setCycleCount(Timeline.INDEFINITE);
//	      tt.setAutoReverse(true);
//	      tt.play();
	  });
	  
	  
    Path path = new Path();
    for (int i = 0; i < 10; i++) {
    	
    	 MoveTo mto =  new MoveTo();
    	 mto.setX(100 );
    	 mto.setY(100 + i);
//    	 mto.xProperty()
    	
    	CubicCurveTo cbto = new CubicCurveTo();
    	cbto.controlX1Property().bind(curve.controlX1Property());
    	cbto.controlY1Property().bind(curve.controlY1Property());
    	cbto.controlX2Property().bind(curve.controlX2Property());
    	cbto.controlY2Property().bind(curve.controlY2Property());

    	
    	
//    	cbto.setControlX1( curve.getControlX1());
//    	cbto.setControlY1( curve.getControlY1());
//    	cbto.setControlX2( curve.getControlX2());
//    	cbto.setControlY2( curve.getControlY2());
    	cbto.xProperty().bind(curve.endXProperty());
    	cbto.yProperty().bind(curve.endYProperty());
//    	cbto.setX( curve.getEndX());
//    	cbto.setY(curve.getEndY());
    	
    	 path.getElements().addAll(mto, cbto);
    	 path.setStrokeWidth(0.5);
    }
    path.setStroke(Color.CADETBLUE);
    stage.setScene(new Scene(new Group(bb, controlLine1, controlLine2, path, start, control1, control2, end), 400, 400, Color.ALICEBLUE));
    stage.show();
  }

  private CubicCurve createStartingCurve() {
    CubicCurve curve = new CubicCurve();
    curve.setStartX(100);
    curve.setStartY(100);
    curve.setControlX1(150);
    curve.setControlY1(50);
    curve.setControlX2(250);
    curve.setControlY2(150);
    curve.setEndX(300);
    curve.setEndY(100);
//    curve.setStroke(Color.FORESTGREEN);
//    curve.setStrokeWidth(4);
//    curve.setStrokeLineCap(StrokeLineCap.ROUND);
//    curve.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));
    curve.setFill(null);
    
    return curve;
  }

  class BoundLine extends Line {
    BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
      startXProperty().bind(startX);
      startYProperty().bind(startY);
      endXProperty().bind(endX);
      endYProperty().bind(endY);
      setStrokeWidth(2);
      setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
      setStrokeLineCap(StrokeLineCap.BUTT);
      getStrokeDashArray().setAll(10.0, 5.0);
    }
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle { 
    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }  
          double newY = mouseEvent.getY() + dragDelta.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }  
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }  
}
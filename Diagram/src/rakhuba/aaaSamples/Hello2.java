package rakhuba.aaaSamples;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Hello2 extends Application {
  public static void main(String[] args) throws Exception { launch(args); }
  
  public void start(final Stage stage) throws Exception { 
	  JoinLine2 jl = new JoinLine2();
	  Button bb = new Button(" jj ");
	  bb.setOnAction(e ->{
		  jl.test();
	  });
	  
    stage.setScene(new Scene(new Group(jl.getPath(), bb), 400, 400, Color.ALICEBLUE));
    stage.show();
  }
  
  private class JoinLine2 {
		private Path path = new Path();
	    private Property<Double> x1 = new SimpleObjectProperty<Double>();
	    private Property<Double> y1 = new SimpleObjectProperty<Double>();
	    private Property<Double> x2 = new SimpleObjectProperty<Double>();
	    private Property<Double> y2 = new SimpleObjectProperty<Double>();
	    private Property<Double> cx1 = new SimpleObjectProperty<Double>();
	    private Property<Double> cy1 = new SimpleObjectProperty<Double>();
	    private Property<Double> cx2 = new SimpleObjectProperty<Double>();
	    private Property<Double> cy2 = new SimpleObjectProperty<Double>();
	    private ArrayList<MoveTo> motos = new ArrayList<MoveTo>();
	    private ArrayList<CubicCurveTo> cctos = new ArrayList<CubicCurveTo>();

		public JoinLine2() {
			x1.setValue(100.0);
			y1.setValue(100.0);
			x2.setValue(100.0);
			y2.setValue(120.0);
			
			cx1.setValue(300.0);
			cy1.setValue(100.0);
			cx2.setValue(300.0);
			cy2.setValue(300.0);
			
		    for (int i = 0; i < 5; i++) {
		    	 MoveTo mTo =  new MoveTo();
		    	 mTo.xProperty().bind(x1);
		    	 
		    	 mTo.setY(y1.getValue() + i*1.5);
		    	 motos.add(mTo);
		    	CubicCurveTo ccTo = new CubicCurveTo();
		    	
		    	ccTo.controlX1Property().bind(cx1);
		    	ccTo.controlY1Property().bind(cy1);
		    	ccTo.controlX2Property().bind(cx2);
		    	ccTo.controlY2Property().bind(cy2);
		    	ccTo.xProperty().bind(x2);
		    	ccTo.yProperty().bind(y2);
		    	cctos.add(ccTo);
		    	path.getElements().addAll(mTo, ccTo);    	 
		    }
		    
		 	path.setStyle("-fx-fill: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);  -fx-stroke-width: 1; -fx-stroke-line-cap: butt;");
		 	LinearGradient lg = new LinearGradient(x1.getValue(), y1.getValue(), x2.getValue(), y2.getValue(),  false, CycleMethod.NO_CYCLE,new Stop(1,Color.valueOf("#fff")), new Stop(0.1,Color.valueOf( "#7cbbf9")));
		 	path.setStroke(lg);
		}

		public Node getPath() {
			return path;
		}

		public void test() {
//			timeline.getKeyFrames().clear(); 
			if(x1.getValue() == 100.0) {
				Timeline timeline = new Timeline(); 
				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(x1, 200.0, Interpolator.EASE_BOTH)));
				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(y1, 200.0, Interpolator.EASE_BOTH)));
				motos.forEach(moto ->{
					//Can't be bonded, like other?? 
					timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(moto.yProperty(), 200.0 + motos.indexOf(moto) * 2, Interpolator.EASE_BOTH)));				
				});
				

				timeline.setCycleCount(1);
				timeline.playFromStart();
			}else {
				x1.setValue(100.0);
				y1.setValue(100.0);
				
				motos.forEach(moto ->{
					moto.setY(y1.getValue() + motos.indexOf(moto) * 2);
				});

//				timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(x1, 100.0, Interpolator.EASE_BOTH)));
			}
//			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(x1, 200.0, Interpolator.EASE_BOTH)));
//			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400),new KeyValue(filemanager.getActiveNFile().getActiveNmap().schemaPane.scaleYProperty(), zoomValue, Interpolator.EASE_BOTH)));

			
			

//			x1.setValue(200.0);
//			y1.setValue(200.0);
		}
  }
}
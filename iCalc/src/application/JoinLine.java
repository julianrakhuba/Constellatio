package application;

import java.util.ArrayList;

import activity.Edit;
import generic.ACT;
import generic.LAY;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
//import rakhuba.application.JainLabel;
import status.JoinType;
import status.SqlType;

public class JoinLine  {
	private Path path = new Path();
    private Property<Double> x1 = new SimpleObjectProperty<Double>();
    private Property<Double> y1 = new SimpleObjectProperty<Double>();
    private Property<Double> x2 = new SimpleObjectProperty<Double>();
    private Property<Double> y2 = new SimpleObjectProperty<Double>();
    private Property<Double> cx1 = new SimpleObjectProperty<Double>();
    private Property<Double> cy1 = new SimpleObjectProperty<Double>();
    private Property<Double> cx2 = new SimpleObjectProperty<Double>();
    private Property<Double> cy2 = new SimpleObjectProperty<Double>();
    private ArrayList<MoveTo> xy1 = new ArrayList<MoveTo>();
    private ArrayList<CubicCurveTo> cubicCurveParts = new ArrayList<CubicCurveTo>();
	
	private LAY fromLay;
	private LAY toLay;
	
	private Property<JoinType> joinType = new SimpleObjectProperty<JoinType>();
	
	
	public JainLabel parentLabel;	
	public JainLabel childLabel;	

	public JoinLine(LAY startLAY, LAY endLAY, JoinType jtype) {
		this.fromLay = startLAY;
		this.toLay = endLAY;
	
//    	parentLabel.setStyle(" -fx-font-size: 10;");
//    	childLabel.setStyle(" -fx-font-size: 10; -fx-padding: 0 5 0 0; -fx-background-radius: 15 15 15 15;  -fx-text-fill: #ababab;");

//    	parentLabel.setGraphic(parentPane);
//    	childLabel.setGraphic(childPane);
    	
//    	parentLabel.setTooltip(new Tooltip(fromLay.getAliase()));
//    	childLabel.setTooltip(new Tooltip(toLay.getAliase()));

//    	parentLabel.setText(" " + fromLay.nnode.getTable());
//    	childLabel.setText(" " + toLay.nnode.getTable());
    	
		joinType.addListener((c,f,g )-> {
			this.updateLayout();
			this.updateStyle();
		});	
		
		parentLabel = new JainLabel(this, "parent");	
		childLabel = new JainLabel(this, "child");	

	    for (int i = 0; i < 4; i++) {
	    	 MoveTo mTo =  new MoveTo();
	    	 mTo.xProperty().bind(x1);
//	    	 mTo.setY(y1.getValue() + i*1.5);// yProperty will be set manualy
	    	 xy1.add(mTo);
	    	 
	    	CubicCurveTo ccTo = new CubicCurveTo();
	    	ccTo.controlX1Property().bind(cx1);
	    	ccTo.controlY1Property().bind(cy1);
	    	ccTo.controlX2Property().bind(cx2);
	    	ccTo.controlY2Property().bind(cy2);
	    	ccTo.xProperty().bind(x2);
	    	ccTo.yProperty().bind(y2);
	    	cubicCurveParts.add(ccTo);
	    	path.getElements().addAll(mTo, ccTo);    	 
	    }
		this.joinType.setValue(jtype);

	    
	}

	// ••••••••••••••••••••••••••
	public void updateLayout() {
		x1.setValue(fromLay.getCenterX());
		y1.setValue(fromLay.getCenterY());
		x2.setValue(toLay.getCenterX());
		y2.setValue(toLay.getCenterY());
		
		cx1.setValue(isDline()? fromLay.getCenterX() - 50 : isSelfJoin()?  fromLay.getCenterX() + 50 : (fromLay.getCenterX() + toLay.getCenterX())/2);
		cy1.setValue(isDline()? fromLay.getCenterY() + 10: isSelfJoin()? fromLay.getCenterY() - 10 : fromLay.getCenterY());
		cx2.setValue(isDline()? toLay.getCenterX() - 50 : isSelfJoin()?  toLay.getCenterX() + 50 : (fromLay.getCenterX() + toLay.getCenterX())/2);
		cy2.setValue(isDline()? toLay.getCenterY() - 10 :isSelfJoin()? toLay.getCenterY() + 10: toLay.getCenterY());
		
//		cx1.setValue((isDline() || isSelfJoin())? fromLay.getCenterX() - 50 : (fromLay.getCenterX() + toLay.getCenterX())/2);
//		cy1.setValue((isDline() || isSelfJoin())? fromLay.getCenterY() + 10 : fromLay.getCenterY());
//		cx2.setValue((isDline() || isSelfJoin())? toLay.getCenterX() - 50 : (fromLay.getCenterX() + toLay.getCenterX())/2);
//		cy2.setValue((isDline() || isSelfJoin())? toLay.getCenterY() - 10 : toLay.getCenterY());

		
		//FLAT LINE
//		cx1.setValue(fromLay.getCenterX());
//		cy1.setValue(fromLay.getCenterY());
//		cx2.setValue(toLay.getCenterX());
//		cy2.setValue(toLay.getCenterY());
		xy1.forEach(moto -> moto.setY(y1.getValue() + ((xy1.indexOf(moto) * 1) - 1.5)));// manual update of multiline
	}
	
	private boolean isDline() {
		return fromLay.nnode == toLay.nnode && joinType.getValue() == JoinType.DLINE;
	}
	
	private boolean isSelfJoin() {
		return fromLay.nnode == toLay.nnode;
	}

	public ArrayList<KeyFrame> yProperty2(LAY layer, double lineToY, Duration dur) {
		ArrayList<KeyFrame> kfs = new ArrayList<KeyFrame>();
		if (layer == fromLay) {
			xy1.forEach(moto ->{
				KeyFrame kf = new KeyFrame(dur,new KeyValue(moto.yProperty(), lineToY  + ((xy1.indexOf(moto) * 1) - 1.5), Interpolator.EASE_BOTH));
				kfs.add(kf);
			});
		}else {
			KeyFrame kf = new KeyFrame(dur,new KeyValue(y2, lineToY, Interpolator.EASE_BOTH));
			kfs.add(kf);
		}
		return kfs;
	}
	
	public KeyFrame yControl(LAY layer, double lineToY, Duration dur) {
		if (layer == fromLay) {
			return new KeyFrame(dur,new KeyValue(cy1, lineToY, Interpolator.EASE_BOTH));
		}else {
			return new KeyFrame(dur,new KeyValue(cy2, lineToY, Interpolator.EASE_BOTH));
		}
	}
	
	public void createGredient(String fromColor) {
		String gredient;
		if(this.isSelfJoin()) {
			gredient = fromColor;
		}else {
			gredient = "linear-gradient(from " + x1.getValue().intValue() +"px " + y1.getValue().intValue() +"px to "+ x2.getValue().intValue()+ "px " + y2.getValue().intValue()+ "px, "+ fromColor + " 0%, #fff 90%)";		
		}
		path.setStyle("-fx-stroke:"+ gredient+";" + "-fx-fill: transparent; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 3, 0.1, 0, 2);  -fx-stroke-width: 1.5; -fx-stroke-line-cap: butt;");
	}

	public void updateStyle() {	
		if(this.getJoinType() == JoinType.JOIN) {
			if(this.fromLay.getSqlType() == SqlType.SQL) {//NEED WORK 
				createGredient("#99ddff");//Blue
			}else {
				createGredient("#7cbbf9");//darkblue
			}
		}else if(this.getJoinType() == JoinType.LEFT) {
			createGredient("#ebe5ff");//lavander
		}else if(this.getJoinType() == JoinType.RIGHT) {
			createGredient("#ffd6eb");//red
		}else if(this.getJoinType() == JoinType.SHIFT) {
			createGredient("#99ddff");//Blue
		}else if(joinType.getValue() == JoinType.DLINE) {
			path.getStyleClass().add("dotedDLine");
		}
		parentLabel.styleAsParent();
		childLabel.styleAsChild();

//		String parcss = this.getFromLay().getSqlType().toString() + "_" +this.getJoinType().toString();
//		String childcss = this.getToLay().getSqlType().toString() + "_" +this.getJoinType().toString();
		
		
//		parentPane.getStyleClass().clear();
//		parentPane.getStyleClass().add("joinBase");
//    	parentPane.getStyleClass().add(parcss);
//    	
//    	childPane.getStyleClass().clear();
//    	childPane.getStyleClass().add("joinBase");
//    	childPane.getStyleClass().add(childcss);
    	
    	
		//baby colors
//		if(this.getJoinType() == JoinType.JOIN) {
//			if(this.fromLay.getSqlType() == SqlType.SQL) {//NEED WORK 
//				createGredient("#8fd4ff");//Blue
//			}else {
//				createGredient("#94c7fa");//darkblue
//			}
//		}else if(this.getJoinType() == JoinType.LEFT) {
//			createGredient("#ebe5ff");//lavander
//		}else if(this.getJoinType() == JoinType.RIGHT) {
//			createGredient("#ffd6eb");//red
//		}else if(this.getJoinType() == JoinType.SHIFT) {
//			createGredient("#8fd4ff");//Blue
//		}else if(joinType.getValue() == JoinType.DLINE) {
//			path.getStyleClass().add("dotedDLine");
//		}
	}
	
	public void setJoinType(JoinType joinType) {
		this.joinType.setValue(joinType);
	}
	
	public JoinType getJoinType() {
		return joinType.getValue();
	}
	

	public LAY getFromLay() {
		return fromLay;
	}

	public LAY getToLay() {
		return toLay;
	}
	
	public String toString() {
		return joinType.getValue() + " [" + fromLay + "] to [" + toLay + "] x1:" + x1.getValue().intValue()  + " y1:" + y1.getValue().intValue()  + " x2:" + x2.getValue().intValue()  + " y2:" + y2.getValue().intValue() + " dl:" + this.isDline() +" slf:"+ this.isSelfJoin() ;
//		return joinType.getValue() +" "+ fromLay + " [" + fromLay.getCenterX()+"-" +fromLay.getCenterY()  + "] to "+toLay+" [" +  toLay.getCenterX()+"-" +toLay.getCenterY() + "]" ;
	}

	public Node getCubicCurve() {
		return path;
	}
	
	public void joinClick(MouseEvent e) {
		ACT act = this.getFromLay().nnode.nmap.getNFile().getActivity();
		if(act instanceof Edit && e.isControlDown()
//				this.getFromLay().nnode.nmap.napp.getNscene().getHoldKeys().contains("CONTROL")
				
				) {
			((Edit)act).disconnectJoin(this);				
		}else if(act instanceof Edit && this.getFromLay().getSqlType() != SqlType.SQL && this.getToLay().getSqlType() != SqlType.SQL) {
			if (this.getJoinType() == JoinType.JOIN) {
				this.setJoinType(JoinType.LEFT);
			} else if (this.getJoinType() == JoinType.LEFT) {
				this.setJoinType(JoinType.RIGHT);
			} else if (this.getJoinType() == JoinType.RIGHT) {
				this.setJoinType(JoinType.JOIN);
			}
		}		
	}
}

//Boolean isCapital = city.isCapital(); //Object Boolean (not boolean)
//String isCapitalName = isCapital ? "Capital" : "City";  IF
//String isCapitalName = isCapital == null ? "" : isCapital ? "Capital" : "City";  ELSE iF

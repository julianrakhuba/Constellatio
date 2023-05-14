package rakhuba.elements;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CursorBox extends HBox {	
	private ELM elm;
	public int index = 0;
	public ObservableList<ELM> elements = FXCollections.observableArrayList();
	
	public CursorBox(ELM el) {
		this.elm = el;
		this.setFocusTraversable(false);
		this.setOnKeyPressed(e -> {			
			if(e.getCode() == KeyCode.RIGHT) {
				this.rightClick();
				e.consume();
			}else if(e.getCode() == KeyCode.LEFT) {
				this.leftClick();
				e.consume();
			}else if(e.getCode() == KeyCode.BACK_SPACE) {
				this.back_space();
				e.consume();
			}else if(e.getCode() == KeyCode.EQUALS && e.isShiftDown()) {
				el.createStringELM("+", el.getRootELM(), true).focusAtEnd();
				e.consume();
			}else if(e.getCode() == KeyCode.EQUALS && !e.isShiftDown()) {
				el.createStringELM("=", el.getRootELM(), true).focusAtEnd();
				e.consume();
			}else if(e.getCode() == KeyCode.MINUS) {
				el.createStringELM("-", el.getRootELM(), true).focusAtEnd();
				e.consume();
			}else if(e.getCode() == KeyCode.SLASH) {
				el.createStringELM("/", el.getRootELM(), true).focusAtEnd();
				e.consume();
			}else if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
				e.consume();
			}else {				

			}
		});
		
		this.focusedProperty().addListener((a,b,c) ->{
			if (c) {
				elm.styleFocused();
				this.getCursor(index).styleFocused();
			} else {
				elm.styleUnfocused();
				this.getCursor(index).styleUnfocused();
			}
		});	
		
		this.setOnMouseClicked(e-> {
			this.focusBetweenClick(e);
			e.consume();
		});
	}


	public ELM getElm() {
		return elm;
	}
	
	public void addELM(ELM addELM, boolean useCursor) {// need to exclude childless ELMs
		addELM.setParent(elm);
		if(useCursor) {
			elements.add(index, addELM);
			int cur2 = index * 2;
			this.getChildren().add(cur2 + 1 ,addELM.getNode());			
			this.getChildren().add(cur2 + 2 , new Cursor(elm));			
			this.moveCursorTo(index + 1);
		}else {
			elements.add(addELM);
			this.getChildren().add(addELM.getNode());			
			this.getChildren().add(new Cursor(elm));
		}
	}

	public void activateFocus() {
		this.requestFocus();
	}
	
	public void focusBetweenClick(MouseEvent e) {		
		if(elements.size()>0) {
			ELM nearestELM = this.findClosestElm(elements, e.getX(), e.getY());
			if( (nearestELM.getNode().getLayoutX() > e.getX())) {
				this.moveCursorTo(elements.indexOf(nearestELM));
			}else {
				this.moveCursorTo(elements.indexOf(nearestELM) + 1);			
			}
		}
		this.requestFocus();
	}

	
	private ELM findClosestElm(ObservableList<ELM> elms, double x, double y) {
	    Point2D clk = new Point2D(x, y);
	    ELM nearestELM = null;
	    double dist = Double.POSITIVE_INFINITY;
	    for (ELM elm : elms) {
    		Bounds bounds = elm.getNode().getBoundsInParent();
	        Point2D[] corners = new Point2D[] {
	                new Point2D(bounds.getMinX(), bounds.getMinY()),
	                new Point2D(bounds.getMaxX(), bounds.getMinY()),
	                new Point2D(bounds.getMaxX(), bounds.getMaxY()),
	                new Point2D(bounds.getMinX(), bounds.getMaxY()),
	        };
	        for (Point2D pCompare: corners) {
	            double nextDist = clk.distance(pCompare);
	            if (nextDist < dist) {
	                dist = nextDist;
	                nearestELM = elm;
	            }
	        }
	    }
	    return nearestELM;
	}
	
	
	public void focusNextTo(MouseEvent e, FieldELM fld) {
	    Point2D clk = new Point2D(e.getX(), e.getY());
		Bounds bounds = fld.getNode().getBoundsInLocal();
		Point2D p1 = new Point2D(bounds.getMinX(), bounds.getMinY());
		Point2D p2 = new Point2D(bounds.getMaxX(), bounds.getMinY());
		 if(p1.distance(clk) < p2.distance(clk)) {
			this.moveCursorTo(elements.indexOf(fld));				
		 }else {
			this.moveCursorTo(elements.indexOf(fld) + 1);			
		 }
		 this.requestFocus();
	}
	


	public Cursor getCursor(int inx) {
		return ((Cursor) this.getChildren().get(inx * 2));
	}
	
	//NAVIGATION
	private void back_space() {
		if(index > 0) {
			this.getChildren().remove((index * 2) -2, (index * 2));
			index = index -1;	
			elements.remove(index);
		}
	}
	
	public void showLastCursor() {
		this.moveCursorTo(elm.getElements().size());		
	}
	
	public void showFirstCursor() {
		this.moveCursorTo(0);		
	}
	
	public void moveCursorTo(int idx) {
		this.getCursor(index).styleUnfocused();
		index = idx;
		this.getCursor(index).styleFocused();
	}

	public void rightClick() {
		if(index < elements.size()) {//movable to right
			ELM right = elements.get(index);
			if(!right.isParent()) {//NEXT
				this.moveCursorTo(index + 1); //this.moveRight();
			}else {//UP		
				right.cursorBox.activateFocus();
				right.cursorBox.moveCursorTo(0);
			}
		}else {
			if(elm.parent != null) {//DOWN 
				elm.parent.cursorBox.activateFocus();
				int parentIndex = elm.parent.getElements().indexOf(elm);
				elm.parent.cursorBox.moveCursorTo(parentIndex + 1);
			}
		}
	}
	
	public void leftClick() {
		if(index > 0) {
			ELM left = elements.get(index - 1);
			if(!left.isParent()) {//NEXT
				this.moveCursorTo(index - 1);
			}else {//UP	
				left.cursorBox.activateFocus();
				left.cursorBox.showLastCursor();
			}			
		}else {
			if(elm.parent != null) {//DOWN 
				elm.parent.cursorBox.activateFocus();
				int parentIndex = elm.parent.getElements().indexOf(elm);
				elm.parent.cursorBox.moveCursorTo(parentIndex);
			}
		}
	}
	
}

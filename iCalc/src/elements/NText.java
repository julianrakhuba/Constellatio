package elements;

import generic.LAY;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Field;
import pivot.FieldVersion;

public class NText {
	private Text text = new Text();
	private Property<Boolean> lmep = new SimpleObjectProperty<Boolean>(false); 
	private LAY lay;
	private StageStyle stageStyle = StageStyle.DECORATED;
//	2. convert LAY to extend layPane???

	
	public NText(String string, Object object) {		
		text.setText(string);
		text.setFont(new Font(12));

		if(object instanceof LAY) lay = (LAY)object;
		else if(object instanceof FieldVersion) lay = ((FieldVersion)object).getField().getFieldLay();
		else if(object instanceof Field) lay = ((Field)object).getFieldLay();
		
		if(lay != null) lmep.bind(lay.getMouseEnteredProperty());			
		styleUnactive();
		lmep.addListener((a,b,c)->{
			if (c) {
				styleActive();
				
				if(object instanceof LAY) {
					ScrollPane sp = ((LAY)object).nnode.nmap.napp.getConsole().getScrollPane();
					TextFlow textFlow = ((LAY)object).nnode.nmap.napp.getConsole().getTextFlow();
					sp.setVvalue(textFlow.getChildren().indexOf(text) / (double) textFlow.getChildren().size());
				}
		
			}else {
				this.styleUnactive();
			}
		});
		
		text.setOnMouseEntered(e ->{
			if (object != null) {
				styleActive();
			}else {
				text.setFill(Color.GRAY);
			}
			if(object instanceof LAY) {
				this.mouseEnterLay(((LAY)object));				
			}else if(object instanceof FieldVersion) {
				this.mouseEnterLay(((FieldVersion)object).getField().getFieldLay());
			}else if(object instanceof Field) {
				this.mouseEnterLay(((Field)object).getFieldLay());
			}
		});
	
		text.setOnMouseExited(e ->{
			this.styleUnactive();
			if(object instanceof LAY) {
				this.mouseExitedLay(((LAY)object));				
			}else if(object instanceof FieldVersion) {
				this.mouseExitedLay(((FieldVersion)object).getField().getFieldLay());
			}else if(object instanceof Field) {
				this.mouseExitedLay(((Field)object).getFieldLay());
			}
		});
		
		text.sceneProperty().addListener((a,b,c) ->{
			if(text.getScene() != null) {
				stageStyle = ((Stage) text.getScene().getWindow()).getStyle();
				this.styleUnactive();				
			}
		});
	}


	private void styleUnactive() {
		if(stageStyle == StageStyle.TRANSPARENT) {
			text.setFill(Color.WHITE);
		}else {
//			text.setFill(Color.BLACK);
			text.setFill(Color.valueOf("#525e6b"));
//			text.setFont(new Font(12));
//			text.setStyle("-fx-text-fill: #9DA1A1;");
		}
	}
	
	private void styleActive() {
//		text.setFill(Color.valueOf("#7cbbf9"));
		text.setFill(Color.valueOf("#7cbbf9"));
//		text.setFont(new Font(12));


//		
	}


	private void mouseEnterLay(LAY lay) {
//		lay.setMode(LayerMode.VIEW);
		lay.getBlueNeon().show(200);
		lay.nnode.separateLayers();
		int inx = lay.nnode.getLayers().indexOf(lay);
		lay.nnode.nmap.getNFile().getCenterMessage().setMessage(lay.nnode, lay.nnode.getTableNameWUnderScr()  + ((inx >0)? " " + inx  : ""));		
	}
	
	private void mouseExitedLay(LAY lay) {
		lay.nnode.nmap.getNFile().getCenterMessage().setMessage(null, null);
		lay.nnode.overlapLayers();
		lay.getBlueNeon().hide(200);
//		lay.setMode(LayerMode.BASE);
	}

	public String getString() {
		return text.getText();
	}

	public Text getText() {
		return text;
	}
	
	

}

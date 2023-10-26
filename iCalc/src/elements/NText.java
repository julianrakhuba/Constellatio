/*******************************************************************************
 * /*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *  *******************************************************************************/
 *******************************************************************************/
package elements;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import layer.LAY;
import logic.Field;
import pivot.FieldVersion;

public class NText {
	private Text text = new Text();
	private Property<Boolean> lmep = new SimpleObjectProperty<Boolean>(false); 
	private LAY lay;
	private StageStyle stageStyle = StageStyle.DECORATED;
	
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
					ScrollPane sp = ((LAY)object).getNnode().getNmap().getNapp().getConsole().getScrollPane();
					TextFlow textFlow = ((LAY)object).getNnode().getNmap().getNapp().getConsole().getTextFlow();
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
			text.setFill(Color.valueOf("#525e6b"));
		}
	}
	
	private void styleActive() {
		text.setFill(Color.valueOf("#7cbbf9"));
	}


	private void mouseEnterLay(LAY lay) {
		lay.getBlueNeon().show(200);
		lay.getNnode().separateLayers();
		int inx = lay.getNnode().getLayers().indexOf(lay);
		lay.getNnode().getNmap().getNFile().getCenterMessage().setMessage(lay.getNnode(), lay.getNnode().getTableNameWUnderScr()  + ((inx >0)? " " + inx  : ""));		
	}
	
	private void mouseExitedLay(LAY lay) {
		lay.getNnode().getNmap().getNFile().getCenterMessage().setMessage(null, null);
		lay.getNnode().overlapLayers();
		lay.getBlueNeon().hide(200);
	}

	public String getString() {
		return text.getText();
	}

	public Text getText() {
		return text;
	}
	
	

}

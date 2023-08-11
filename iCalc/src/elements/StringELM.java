package elements;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import logic.Field;
//import rakhuba.elements.ELM;
//import rakhuba.elements.RootELM;

public class StringELM extends ELM{
	private TextField textField = new TextField();
	private String acStyle = "-fx-effect: innershadow(three-pass-box, #99ddff, 4, 0.1, 0, 0); -fx-text-fill: #9DA1A1; -fx-background-color: white; -fx-background-radius: 4 4 4 4";
	private String unStyle = "-fx-text-fill: #9DA1A1; -fx-background-color: white; -fx-background-radius: 4 4 4 4";
	
	public StringELM(String string, RootELM rootELM) {
		super(rootELM);
		textField.setPadding(new Insets(0,0,0,0));
		this.styleUnfocused();
		textField.setAlignment(Pos.CENTER);
		textField.textProperty().addListener((ov, prevText, currText) -> {
			 Platform.runLater(() -> {
		        Text tmpTxt = new Text(currText);
		        tmpTxt.setFont(textField.getFont());
		        double txWdt = tmpTxt.getLayoutBounds().getWidth();
		        textField.setPrefWidth(Math.max(txWdt + 10, 12));	        
		        this.getRootELM().refreshSideLableText();
			 });
		});
		
		textField.setText(string);
		textField.focusedProperty().addListener((a, b, c) -> {
			if (c) {
				this.styleFocused();
			}else {
				this.styleUnfocused();
			}
		});
	}
	
	public void styleFocused() {
			textField.setStyle(acStyle);
	}

	public void styleUnfocused() {
			textField.setStyle(unStyle);
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public String getLabelText() {
		return " " + textField.getText() + " ";
	}
	
	public String getSideLabelText() {
		return " " + textField.getText() + " ";
	}
	
	public String getStringSql() {
		return " " + textField.getText() + " ";
	}
	
	public Collection<? extends NText> getTextSql() {
		ArrayList<NText> ret = new ArrayList<NText>();
		ret.add(new NText(" " + textField.getText() + " "));
		return ret;
	}
	
	public String getPivotStringSQL(Field pvtFld, String val) {
		return textField.getText();
	}

	
	public Node getNode() {
		return textField;
	}

	public void saveXml(Document doc, Element parentE) {
		Element fldE = doc.createElement("StringELM");
		parentE.appendChild(fldE);
		fldE.setAttribute("string", textField.getText());
	}
	

	public void focusAtEnd() {
		textField.requestFocus();
		textField.positionCaret(textField.getText().length());
	}
	
	public List<ELM> isUsedInElm(Field field) {	
		return new  ArrayList<ELM>();
	}
	
}

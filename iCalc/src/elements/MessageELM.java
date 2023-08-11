package elements;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import logic.Field;
//import rakhuba.elements.ELM;
//import rakhuba.elements.RootELM;
import sidePanel.Message;

public class MessageELM extends ELM{
	private Label label = new Label();
	private Message error;
	private String errorStyle = "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 2, 0.2, 0, 0); -fx-text-fill: white; -fx-background-color: linear-gradient(#ffebeb, #ff6161), radial-gradient(center 50% -40%, radius 200%, #ff6161 45%, #ff3d3d 50%); -fx-background-radius: 10 10 10 10";
	
	public MessageELM(Message message, RootELM rootELM) {
		super(rootELM);
		this.error = message;
		label.setPadding(new Insets(0,5,0,5));
		this.styleUnfocused();
		label.setAlignment(Pos.CENTER);
		label.setText(message.getError());
		label.setTooltip(new Tooltip(message.getDescription()));
		label.focusedProperty().addListener((a, b, c) -> {
			if (c) {
				this.styleFocused();
			}else {
				this.styleUnfocused();
			}
		});
		label.setStyle(errorStyle);
	}
	
	public void styleFocused() {
		label.setStyle(errorStyle);
	}

	public void styleUnfocused() {
		label.setStyle(errorStyle);
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public String getLabelText() {
		return " [" + label.getText() + "] ";
	}
	
	public String getSideLabelText() {
		return " [" + label.getText() + "] ";
	}
	
	public String getStringSql() {
		return "";
	}
	public Collection<? extends NText> getTextSql() {
		ArrayList<NText> ret = new ArrayList<NText>();
		ret.add(new NText(""));
		return ret;
	}
	
	public String getPivotStringSQL(Field pvtFld, String val) {
		return "";
	}

	
	public Node getNode() {
		return label;
	}

	public void saveXml(Document doc, Element parentE) {
		Element fldE = doc.createElement("MessageELM");
		parentE.appendChild(fldE);
		fldE.setAttribute("message", error.getError());
		fldE.setAttribute("description",error.getDescription());
	}
	
	public List<ELM> isUsedInElm(Field field) {	
		return new  ArrayList<ELM>();
	}
}

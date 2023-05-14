package rakhuba.elements;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import rakhuba.application.XML;
import rakhuba.file.OpenContext;
import rakhuba.logic.Field;

public class FunctionELM extends ELM{
	private String activeStyle = "-fx-background-color: white; -fx-padding: 0 0 0 0; -fx-text-fill: #7cd0f9;";
	private String unactiveStyle = "-fx-background-color: white; -fx-padding: 0 0 0 0; -fx-text-fill: #708090;";
	private String cusrorHboxStyle = "-fx-padding: 0 0 0 0; -fx-spacing: 2;  -fx-alignment:CENTER;" ;
	private BorderPane bpane = new BorderPane();
	private Label oLabel;
	private Label cLabel;
	
	private String name;
	private String open;
	private String oPar;
	private String cPar;
	private String close;
	private String label;

	public FunctionELM(String name, String label, String open, String oPar, String cPar, String close, RootELM rootELM) {
		super(rootELM);
		cursorBox = new CursorBox(this);
		cursorBox.getChildren().add(new Cursor(this));
		this.name = name; 
		this.label = label;
		this.open = open;
		this.oPar = oPar;
		this.cPar = cPar;
		this.close = close;
				
		oLabel = new Label(label + open);
		oLabel.setStyle(unactiveStyle);
		cLabel  = new Label(close);
		cLabel.setStyle(unactiveStyle);		
		bpane.setLeft(oLabel);
		bpane.setRight(cLabel);
		bpane.setCenter(cursorBox);
		bpane.setStyle("-fx-alignment:CENTER; -fx-background-color: white; -fx-padding: 0 0 0 0;" );
		BorderPane.setAlignment(oLabel, Pos.CENTER);
		BorderPane.setAlignment(cLabel, Pos.CENTER);

		cursorBox.setStyle(cusrorHboxStyle);
		oLabel.setOnMouseClicked(e->{
			cursorBox.requestFocus();
			cursorBox.showFirstCursor();
		});
		bpane.setOnMouseClicked(e ->{
			e.consume();
		});
		
		this.getElements().addListener((ListChangeListener<? super ELM>) a -> rootELM.refreshSideLableText());
	}
	
	public void styleFocused() {
		oLabel.setStyle(activeStyle);
		cLabel.setStyle(activeStyle);
	}

	public void styleUnfocused() {
		oLabel.setStyle(unactiveStyle);
		cLabel.setStyle(unactiveStyle);
	}

	public FunctionELM(OpenContext context, org.w3c.dom.Node fx, RootELM rootELM) {		
		this(XML.atr(fx, "name"), XML.atr(fx, "label"), XML.atr(fx, "open"), XML.atr(fx, "oPar"), XML.atr(fx, "cPar"), XML.atr(fx, "close"), rootELM);
		super.createXMLChildren(context, fx,rootELM);
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public String getText() {				
		StringBuilder ret = new StringBuilder();
		ret.append(" ");
		ret.append(oLabel.getText());
		this.getElements().forEach(elm -> ret.append(elm.getText() + ""));
		ret.append(cLabel.getText());
		return ret.toString();
	}
	
	public String getSideLabelText() {				
		StringBuilder ret = new StringBuilder();
		ret.append(" ");
		ret.append(oLabel.getText());
		this.getElements().forEach(elm -> ret.append(elm.getSideLabelText() + ""));
		ret.append(cLabel.getText());
		return ret.toString();
	}
	
	public String getFullSqlName() {
		StringBuilder ret = new StringBuilder();
		ret.append(" "+ name);
		ret.append(open);
		ret.append(oPar);
		this.getElements().forEach(elm -> ret.append(elm.getFullSqlName() + ""));
		ret.append(cPar);
		ret.append(close);
		return ret.toString();
	}
	
	public String getSqlPivotizedColumn(Field pvtFld, String val) {
		StringBuilder ret = new StringBuilder();
		ret.append(" " + name);
		ret.append(open);
		ret.append(oPar);
		this.getElements().forEach(elm -> ret.append(elm.getSqlPivotizedColumn(pvtFld, val) + ""));
		ret.append(cPar);
		ret.append(close);
		return ret.toString();
	}

	public Node getNode() {
		return bpane;
	}
	
	public void saveXml(Document doc, Element parentE) {
		Element funcE = doc.createElement("FunctionELM");
		parentE.appendChild(funcE);
		funcE.setAttribute("name", name);
		funcE.setAttribute("label", label);
		funcE.setAttribute("open", open);
		funcE.setAttribute("oPar", oPar);
		funcE.setAttribute("cPar", cPar);
		funcE.setAttribute("close", close);
		funcE.setAttribute("name", name);
		this.getElements().forEach(ch -> ch.saveXml(doc, funcE));
	}
	
	public List<ELM> isUsedInElm(Field field) {
		List<ELM> used = new  ArrayList<ELM>();
		 this.getElements().forEach(el -> used.addAll(el.isUsedInElm(field)));
		return used;
	}

}

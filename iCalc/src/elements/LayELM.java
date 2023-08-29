package elements;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.XML;
import file.OpenContext;
import generic.LAY;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import logic.Field;
import logic.SQL;
import status.ColorMode;

public class LayELM extends ELM{
	private String activeStyle = "-fx-padding: 0 2 0 2; -fx-spacing: 5;  -fx-alignment:CENTER; -fx-min-height: 21; -fx-max-height: 21; -fx-effect: dropshadow(three-pass-box, #99ddff, 2, 0.5, 0, 0.5); -fx-background-color: white; -fx-text-fill: #9DA1A1;  -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;" ;
	private String unactiveStyle = "-fx-padding: 0 2 0 2; -fx-spacing: 5;  -fx-alignment:CENTER; -fx-min-height: 21; -fx-max-height: 21; -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 2, 0.5, 0, 0.5); -fx-background-color: white; -fx-text-fill: #9DA1A1;  -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;" ;	
	private Label label;
	private LAY lay;
	private BorderPane bpane = new BorderPane();
	private Tooltip tp = new Tooltip();

	public LayELM(LAY lay, RootELM rootELM) {
		super(rootELM);
		cursorBox = new CursorBox(this);
		cursorBox.getChildren().add(new Cursor(this));
		
		this.lay = lay;
		label = new Label();
		label.setTooltip(tp);
		tp.setText(lay.getAliase());
		
		label.getStyleClass().add("layInFunction");
		BorderPane.setAlignment(label, Pos.CENTER);
		BorderPane.setMargin(label, new Insets(0, 2,0,2));		
		
		bpane.setLeft(label);
		bpane.setCenter(cursorBox);
		cursorBox.setStyle("-fx-padding: 0 0 0 0; -fx-spacing: 2;  -fx-alignment:CENTER; -fx-background-color: transparent; -fx-background-radius: 10 10 10 10;  -fx-border-radius: 10 10 10 10;" );
		bpane.setStyle(unactiveStyle);

		
		label.setOnMouseClicked(e->{
			cursorBox.requestFocus();
			cursorBox.showFirstCursor();
			lay.getStyler().pulse(ColorMode.EDIT);
		});
		
		bpane.setOnMouseClicked(e ->{
			e.consume();
		});
		
		//Rebuild hbox
		this.getElements().addListener((ListChangeListener<? super ELM>) a -> rootELM.refreshSideLableText());
//		elmHBox.setOnKeyPressed(e -> this.handleKeyEvent(e));
	}
	
	public void styleFocused() {
		bpane.setStyle(activeStyle);
	}

	public void styleUnfocused() {
		bpane.setStyle(unactiveStyle);
	}
	
	public LayELM(OpenContext context, org.w3c.dom.Node fx, RootELM rootELM) {
		this(context.getAliaseLAYs().get(XML.atr(fx, "layAliase")), rootELM);//get lay from context
		super.createXMLChildren(context, fx,rootELM);
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public String getLabelText() {				
		StringBuilder ret = new StringBuilder();
		ret.append(" query(");
		this.getElements().forEach(elm -> ret.append(elm.getLabelText() + ""));		
		ret.append(")");
		return ret.toString();
	}
	
	public void buildSQL(SQL sql) {
		
		SQL fieldSQL = new SQL();//Can I get away without usen new sql here??
		this.getElements().forEach(elm -> elm.buildSQL(fieldSQL));// is this is different in LayELM vs other elms?
		sql.SELECT();
		sql.addNText(new NText(" " + fieldSQL.toString() + " ", lay));	
		
		lay.from(sql);
	}
	
	public String getPivotStringSQL(Field pvtFld, String val) {
		return  " ";
	}

	public Node getNode() {
		return bpane;
	}
	
	public void saveXml(Document doc, Element parentE) {
		Element funcE = doc.createElement("LayELM");
		parentE.appendChild(funcE);
		funcE.setAttribute("layAliase", lay.getAliase());
		this.getElements().forEach(ch -> ch.saveXml(doc, funcE));
	}
	
	public List<ELM> isUsedInElm(Field field) {
		List<ELM> used = new  ArrayList<ELM>();
		this.getElements().forEach(el -> used.addAll(el.isUsedInElm(field)));
		return used;
	}
}

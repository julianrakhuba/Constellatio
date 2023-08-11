package elements;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import application.XML;
import file.OpenContext;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import logic.Field;
import status.ColorMode;
import status.FieldPivot;

public class FieldELM extends ELM{
	private String unactiveStyle = "-fx-background-color: #f0f0f0; -fx-text-fill: #708090;  -fx-background-radius: 10 10 10 10;  -fx-border-radius: 10 10 10 10;" ;
	private String activeStylePivotized = "-fx-effect: dropshadow(three-pass-box, #ffcf0f, 2, 0.5, 0, 0.5); -fx-background-color: #f0f0f0; -fx-text-fill: #708090;  -fx-background-radius: 10 10 10 10;  -fx-border-radius: 10 10 10 10;" ;
	private Label label = new Label();
	private Field field;
	private Property<FieldPivot> pivotized = new SimpleObjectProperty<FieldPivot>(FieldPivot.BASIC);
	
	public FieldELM(Field field, RootELM rootELM) {
		super(rootELM);
		label.setStyle(unactiveStyle);
		this.field = field;
		label.setText(" " + field.getLabelText() + " ");
		label.setTooltip(new Tooltip(field.getFieldLay().getAliase()));
		label.setPadding(new Insets(0,0,0,0));
		label.setMaxHeight(10);
		
		label.setOnMouseClicked(e -> {
			if(getRootELM().getFormula() != null && e.isControlDown()) {
				if(pivotized.getValue() == FieldPivot.BASIC) {
					pivotized.setValue(FieldPivot.PIVOTIZED);
				}else {
					pivotized.setValue(FieldPivot.BASIC);
				}
			}else {
				//focus click
				this.parent.cursorBox.focusNextTo(e, this);
				field.getFieldLay().getStyler().pulse(ColorMode.EDIT);
			}
			e.consume();
		});

		pivotized.addListener((a,b,c) -> this.updateStyle());
		if(getRootELM().getFormula() != null) pivotized.setValue(FieldPivot.PIVOTIZED);		
	}
	
	public FieldELM(OpenContext context, org.w3c.dom.Node n, RootELM rootELM) {
		this(context.getFields().get(XML.atr(n, "fieldAliase")), rootELM);
		pivotized.setValue(FieldPivot.valueOf(XML.atr(n, "pivotized")));
	}
	
	private void updateStyle() {	
		if(pivotized.getValue() == FieldPivot.PIVOTIZED) {
			label.setStyle(activeStylePivotized);
		}else {
			label.setStyle(unactiveStyle);
		}
	}

	//OUTPUT •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	public String getLabelText() {
		return  "" + field.getLabelText() + "";
	}
	
	public String getSideLabelText() {
		return  "" + field.getFieldLay().nnode.getTable() + "";
	}
	
	public String getStringSql() {
		return field.getFunction_Column();
	}
	
	public Collection<? extends NText> getTextSql() {
		ArrayList<NText> ret = new ArrayList<NText>();
		ret.add(new NText(field.getFunction_Column()));
		return ret;
	}

	public String getPivotStringSQL(Field pivotField, String val) {
		//makes pivot optional at field level, need to add pivotized option when creating columns 
		if(pivotized.getValue() == FieldPivot.PIVOTIZED) {
			return " CASE WHEN " + pivotField.getFunction_Column() +" = '" + val + "' THEN " + field.getFunction_Column() + " END";
		}else {
			return this.getStringSql();
		}
	}
	
	public Node getNode() {
		return label;
	}

	public Field getField() {
		return field;
	}

	public void saveXml(Document doc, Element parentE) {
		Element fldE = doc.createElement("FieldELM");
		parentE.appendChild(fldE);
		fldE.setAttribute("fieldAliase", field.getAliase());
		fldE.setAttribute("pivotized", pivotized.getValue().toString());				
	}

	public List<ELM> isUsedInElm(Field field) {
		 List<ELM> used = new  ArrayList<ELM>();
		if(this.field == field) used.add(this);		
		return used;
	}

	public void styleFocused() {
		
	}

	public void styleUnfocused() {
		
	}


}

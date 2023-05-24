package pivot;

import logic.Field;
import status.ColorMode;
import status.VersionType;

public class PivotColumn {
	
	private String function_column;
	private String aliase;
	private String label;
	private String tip;
	private VersionType versionType;
	
	private Field field;
	private Field pivotField;
	
	public PivotColumn(VersionType versionType, Field field) {
		this.field = field;
		this.versionType = versionType;		
	}
	
	public PivotColumn(VersionType versionType) {
		this.versionType = versionType;
	}
	
	public String getAliase() {
		return aliase;
	}

	public void setAlias(String aliase) {
		this.aliase = aliase;
	}

	public String getLabel() {		
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTip() {
		return tip;
	}
	
	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getFunction_Column() {
		return function_column;
	}

	public void setFunction_Column(String funcColumn) {
		this.function_column = funcColumn;
	}

	public VersionType getVersionType() {
		return versionType;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
	
	public Field getPivotField() {
		return pivotField;
	}
	
	public void setPivotField(Field pivotField) {
		this.pivotField = pivotField;
	}

	public void pulseLay() {
		if(pivotField == null) {
			field.getFieldLay().getStyler().pulse(ColorMode.VIEW);
		}else {
			field.getFieldLay().getStyler().pulse(ColorMode.VIEW);
			pivotField.getFieldLay().getStyler().pulse(ColorMode.FORMULA);
		}
	}
}

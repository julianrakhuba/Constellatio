package generic;

import java.util.ArrayList;

import application.JoinLine;
import application.Nnode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Field;
import logic.FormulaField;
import logic.Join;
//import rakhuba.generic.DLayer;
//import rakhuba.generic.LAY;
import status.SqlType;
import status.VersionType;

public class DLayer extends LAY {
	private LAY parentLay;
	private JoinLine joinLine;

	public DLayer(Nnode nnode, LAY parentLay) {
		super(nnode, SqlType.SQLD);
		this.parentLay = parentLay;
		this.parentLay.setChildDLayer(this);
	}
	
	public void rebuildDFieldsAndJoins() {
		if(parentLay instanceof DLayer) ((DLayer) parentLay).rebuildDFieldsAndJoins();
		parentLay.refreshPivotCache();
		parentLay.recreateVersions();
		ObservableList<Field> newFields = FXCollections.observableArrayList();
		parentLay.getVersions().forEach(v ->{
			String fldAls = this.getAliase() + "_" + v.getAliase();
			if (!this.containsField(fldAls)) {
				Field field = new Field(this);
				field.setAliase(fldAls);
				field.setText(v.getLabel());
				field.setSQL_Column_name(v.getAliase());
				field.setSchema(v.getField().getSchema());
				field.setTable(v.getField().getTable());
				field.setColumn(v.getField().getColumn());
				field.setRowset_type(v.getField().getRowset_type());
				field.setColumn_key(v.getField().getColumn_key());
				field.setParentVersion(v);
				
				if(v.getVersionType() == VersionType.GROUPBY || v.getVersionType() == VersionType.BLANK) {
					v.getField().getJoins().forEach(parJoin -> {
						Join join = new Join(this, v.getAliase(), parJoin.getSchema(), parJoin.getTable(), parJoin.getColumn(), parJoin.getRemoteSchema(), parJoin.getRemoteTable(), parJoin.getRemoteColumn());
						field.addJoin(join);
					});
				}
				
				newFields.add(field);
			}else {
				newFields.add(this.getFieldOrFunction(fldAls));
				this.getFieldOrFunction(fldAls).setParentVersion(v);// before this change, was it using previous version
			}				
		});

		// CLEANUP
		ArrayList<Field> fieldForRemoval = new ArrayList<Field>();
		this.getFieldsAndFormulas().forEach(f -> {
			if(newFields.filtered(ff -> ff.getAliase().equals(f.getAliase())).size() == 0) {
				fieldForRemoval.add(f);
			}
		});

		fieldForRemoval.forEach(remFld -> {			
			if(remFld instanceof FormulaField) {
				newFields.add(remFld);
				// if CustomField is based on non-exiting version it needs to be removed
			}else {
				if(this.getSelectedFields().contains(remFld)) {
					this.getSelectedFields().remove(remFld);
				}
			}
		});
		this.getFields().clear();
		this.getFormulaFields().clear();
		newFields.forEach(f ->{
			if(f instanceof FormulaField ) {
				this.addFormulaField((FormulaField) f);
			}else {
				this.addField(f);
			}
		});		
		this.recreateVersions();
	}

	public LAY getParentLay() {
		return parentLay;
	}

	public JoinLine getJoinLine() {
		return joinLine;
	}

	public void setJoinLine(JoinLine joinLine) {
		this.joinLine = joinLine;
	}
}

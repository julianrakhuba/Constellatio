package application;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import status.SqlType;

public class SqlTypeButton extends Button {
	private Property<SqlType> sqlType = new SimpleObjectProperty<SqlType>(SqlType.SQLJ);
	
	private void updateStyle() {
		this.getStyleClass().clear();
		this.getStyleClass().add(sqlType.getValue().toString() + "btn");
	}
	
	public SqlType getSqlType() {
		return this.sqlType.getValue();
	}

	public SqlTypeButton(String sting) {
		super(sting);
		this.setFocusTraversable(false);
		this.updateStyle();
		sqlType.addListener((c,f,g) -> this.updateStyle());
		
		
		this.setOnMouseClicked(e -> {
			//Toggle
			if(sqlType.getValue() == SqlType.SQL) {
				sqlType.setValue(SqlType.SQLJ);
			}else if(sqlType.getValue() == SqlType.SQLJ) {
				sqlType.setValue(SqlType.SQL);
			}
			e.consume();
		});
	}


	public void setSqlType(SqlType st) {
		if(st == SqlType.SQLD || st == SqlType.SQLJ) {
			this.sqlType.setValue(SqlType.SQLJ);
		}else {
			this.sqlType.setValue(st);
		}
	}

}

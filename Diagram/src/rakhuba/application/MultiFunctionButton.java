package rakhuba.application;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import rakhuba.status.ActivityMode;
import rakhuba.status.SqlType;

public class MultiFunctionButton extends Button {
	private Property<SqlType> sqlType = new SimpleObjectProperty<SqlType>(SqlType.SQLJ);

	public MultiFunctionButton(String sting, Constallatio napp) {
		super(sting);
		this.setFocusTraversable(false);
		this.updateStyle();
		sqlType.addListener((c,f,g) -> this.updateStyle());
		
		this.setOnMouseClicked(e -> {		
			if(e.isShiftDown() || (napp.filemanager.getActiveNFile() != null && napp.filemanager.getActiveNFile().getActivityMode() == ActivityMode.SELECT && napp.search.isSwitchableMode())) {
				if(sqlType.getValue() == SqlType.SQL) {
					sqlType.setValue(SqlType.SQLJ);
				}else if(sqlType.getValue() == SqlType.SQLJ) {
					sqlType.setValue(SqlType.SQL);
				}
			}else {
				napp.funcMenuClick(null);
			}
			e.consume();
		});
	}

	private void updateStyle() {
		this.getStyleClass().clear();
		this.getStyleClass().add(sqlType.getValue().toString() + "btn");
	}
	
	public SqlType getSqlType() {
		return this.sqlType.getValue();
	}

	public void setSqlType(SqlType st) {
		if(st == SqlType.SQLD || st == SqlType.SQLJ) {
			this.sqlType.setValue(SqlType.SQLJ);
		}else {
			this.sqlType.setValue(st);
		}
	}

}

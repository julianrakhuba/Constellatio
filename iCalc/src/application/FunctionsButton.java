package application;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
//import rakhuba.application.Constellatio;
import status.ActivityMode;
import status.SqlType;

public class FunctionsButton extends Button {
	private Property<SqlType> sqlType = new SimpleObjectProperty<SqlType>(SqlType.SQLJ);

	public FunctionsButton(String sting, Constellatio napp) {
		super(sting);
		this.setFocusTraversable(false);
		this.updateStyle();
		sqlType.addListener((c,f,g) -> this.updateStyle());
		
		this.setOnMouseClicked(e -> {		
			if(e.isShiftDown() || (napp.getFilemanager().getActiveNFile() != null && napp.getFilemanager().getActiveNFile().getActivityMode() == ActivityMode.SELECT && napp.getUpperPane().getSearchTextField().isSwitchableMode())) {
				if(sqlType.getValue() == SqlType.SQL) {
					sqlType.setValue(SqlType.SQLJ);
				}else if(sqlType.getValue() == SqlType.SQLJ) {
					sqlType.setValue(SqlType.SQL);
				}
			}else {
				napp.getUpperPane().funcMenuClick(null);
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
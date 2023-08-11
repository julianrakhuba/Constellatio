package logic;

import java.util.ArrayList;
import java.util.List;

import elements.NText;
import generic.DLayer;
import generic.LAY;
import javafx.scene.text.TextFlow;

public class SQL {
	
	public SQL() {
//		System.out.println("[new SQL object]");
	}

	private StringBuilder sqlString = new StringBuilder();
//	private TextFlow textFlow = new TextFlow();
	
	
//	public void append(NText ntext) {
//		sqlString.append(ntext.getText());
//	}
	
	public SQL ORDERBY_ASC(String column) {
		sqlString.append(" ORDER BY " + column + " ASC ");
//		textFlow.getChildren().add(new NText(" ORDER BY " + column + " ASC "));//TODO TEXT SQL
		return this;
	}
	
	public SQL ORDERBY() {
		sqlString.append(" ORDER BY ");
		return this;
	}
	
	public SQL WHERE() {
		sqlString.append(" WHERE ");
		return this;
	}
	
	public SQL EQ(String table, String column, String value) {
		if (value != null)  sqlString.append(table + "." +  column + " = '" + value + "'");
		else sqlString.append(table + "." +  column + " is null ");
		return this;
	}

	public SQL IN(String table, String column, List<String> values) {
		sqlString.append(table + "." + column);
		sqlString.append(" IN ('" + values.get(0) + "'");
		values.subList(1, values.size()).forEach(value -> sqlString.append(", '" + value + "'"));
		sqlString.append(")");
		return this;
	}
	
	public SQL AND() {
		sqlString.append(" AND ");
		return this;
	}
	
	public SQL OR() {
		sqlString.append(" OR ");
		return this;
	}

	public SQL close() {
		sqlString.append(")");
		return this;
	}

	public SQL open() {
		sqlString.append("(");
		return this;
	}
	
	public SQL append(String string) {
		sqlString.append(string);
		return this;
	}
	


	public String toString() {
		return sqlString.toString();
	}

	public SQL OR(ArrayList<String> conditions) {
		if (conditions.size() > 1) {
			sqlString.append("(" + conditions.get(0));
			conditions.subList(1, conditions.size()).forEach(value -> {
				sqlString.append(" OR " + value);
			});
			sqlString.append(")");
		} else {
			sqlString.append(" " + conditions.get(0) + " ");
		}
		return this;
	}
	
	public SQL SELECT (String string) {
		sqlString.append("SELECT " + string);
		return this;
	}
	
	public SQL SELECT () {
		sqlString.append("SELECT ");
		return this;
	}
	
	public SQL FROM(LAY lay) {
		this.line();
		sqlString.append(" FROM " + lay.nnode.getFullNameWithOptionalQuotes() + " " + lay.getAliase());
		return this;
	}
	
	public SQL SUBQRY(DLayer dlay) {
		sqlString.append( " FROM (" + dlay.getParentLay().getSQLJ().toString() + ") " + dlay.getAliase() + " ");
		return this;
	}
	
	public SQL line() {
		sqlString.append(System.getProperty("line.separator"));
		return this;
	}

	public SQL GROUPBY() {
		this.line();
		sqlString.append(" GROUP BY ");
		return this;
	}

	public SQL WITHROLLUP() {
		sqlString.append(" WITH ROLLUP ");
		return this;
	}
	
	public SQL DISTINCT(String string) {
		sqlString.append(" DISTINCT " + string);
		return this;
	}
}


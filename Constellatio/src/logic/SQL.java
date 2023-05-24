package logic;

import java.util.ArrayList;
import java.util.List;

import generic.DLayer;
import generic.LAY;

public class SQL {
	private StringBuilder sql = new StringBuilder();
	
	public SQL ORDERBY_ASC(String column) {
		sql.append(" ORDER BY " + column + " ASC ");
		return this;
	}
	
	public SQL ORDERBY() {
		sql.append(" ORDER BY ");
		return this;
	}
	
	public SQL WHERE() {
		sql.append(" WHERE ");
		return this;
	}
	
	public SQL EQ(String table, String column, String value) {
		if (value != null)  sql.append(table + "." +  column + " = '" + value + "'");
		else sql.append(table + "." +  column + " is null ");
		return this;
	}

	public SQL IN(String table, String column, List<String> values) {
		sql.append(table + "." + column);
		sql.append(" IN ('" + values.get(0) + "'");
		values.subList(1, values.size()).forEach(value -> sql.append(", '" + value + "'"));
		sql.append(")");
		return this;
	}
	
	public SQL AND() {
		sql.append(" AND ");
		return this;
	}
	
	public SQL OR() {
		sql.append(" OR ");
		return this;
	}

	public SQL close() {
		sql.append(")");
		return this;
	}

	public SQL open() {
		sql.append("(");
		return this;
	}
	
	public SQL append(String string) {
		sql.append(string);
		return this;
	}

	public String toString() {
		return sql.toString();
	}

	public SQL OR(ArrayList<String> conditions) {
		if (conditions.size() > 1) {
			sql.append("(" + conditions.get(0));
			conditions.subList(1, conditions.size()).forEach(value -> {
				sql.append(" OR " + value);
			});
			sql.append(")");
		} else {
			sql.append(" " + conditions.get(0) + " ");
		}
		return this;
	}
	
	public SQL SELECT (String string) {
		sql.append("SELECT " + string);
		return this;
	}
	
	public SQL SELECT () {
		sql.append("SELECT ");
		return this;
	}
	
	public SQL FROM(LAY lay) {
		this.line();
		sql.append(" FROM " + lay.nnode.getFullNameWithOptionalQuotes() + " " + lay.getAliase());
		return this;
	}
	
	public SQL IN_SUB(String whatString, LAY lay) {	
			sql.append(lay.getSQL(whatString));
		return this;
	}
	
	public SQL DSUB(DLayer dlay) {
		sql.append( " FROM (" + dlay.getParentLay().getSQLJ().toString() + ") " + dlay.getAliase() + " ");
		return this;
	}
	
	public SQL line() {
		sql.append(System.getProperty("line.separator"));
		return this;
	}
	
	public SQL COUNTDB() {//WHY I HAVE THIS???
		SQL countSQL = new SQL();
		countSQL.append(sql.toString());
		return countSQL;
	}

	public SQL GROUPBY() {
		this.line();
		sql.append(" GROUP BY ");
		return this;
	}

	public SQL WITHROLLUP() {
		sql.append(" WITH ROLLUP ");
		return this;
	}

	public SQL DISTINCT() {
		sql.append(" DISTINCT ");
		return this;
	}
	
	public SQL DISTINCT(String string) {
		sql.append(" DISTINCT " + string);
		return this;
	}

	public void FROM(SQL sqlj) {
		this.line();
		sql.append(" FROM (" + sqlj + ")");
	}
}


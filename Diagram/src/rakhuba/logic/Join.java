package rakhuba.logic;

import rakhuba.generic.LAY;

public class Join {
	
	private LAY fromLay;
	private String sqlColumn;
	private String schema;
	private String table;
	private String column;
	private String remote_schema;
	private String remote_table;
	private String remote_column;
		
	public Join(LAY fromLay, String sqlColumn, String schema, String table, String column,String remote_schema, String remote_table, String remote_column) {				
		this.sqlColumn = sqlColumn;
		this.fromLay = fromLay;
		
		this.schema = schema;
		this.table = table;
		this.column = column;
		this.remote_schema = remote_schema;
		this.remote_table = remote_table;
		this.remote_column = remote_column;
	}
	
	public LAY getLay() {
		return fromLay;
	}

	public String getSqlColumn() {
		return this.sqlColumn;
	}
	
	public String getColumn() {
		return column;
	}
		
	public String getTable() {
		return table;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public String getRemoteColumn() {
		return remote_column;
	}
	
	public String getRemoteTable() {
		return remote_table;
	}
	
	public String getRemoteSchema() {
		return remote_schema;
	}
	
	public boolean isLocal_by_Derived () {
		return fromLay.nnode.getSchema().equals(this.getRemoteSchema());
	}
	
	public String toString() {

		return super.toString() + " • " + fromLay.getAliase() + "	"
		+ " sql: " + sqlColumn 
		+ " s: " + schema 
		+ " t: " + table 
		+ " c: " + column 
		+ " rs: " + remote_schema 
		+ " rt: " + remote_table 
		+ " rc: " + remote_column 
		
		
		;
	}

}
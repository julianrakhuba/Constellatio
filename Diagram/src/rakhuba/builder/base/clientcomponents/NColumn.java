package rakhuba.builder.base.clientcomponents; //

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rakhuba.application.XML;
import rakhuba.generic.BO;

public class  NColumn  extends BO{
	private String schema_name; 
	private String table_name; 
	private String column_name; 
	private Number order_position; 
	private String data_type; //make separate class for db column with data type, I think data type is not needed in NColumn and Field
	private String rowset_type; 
	private String column_key; 
		
	public  NColumn(){

	}

	public NColumn(String position, String table_schema, String table_name, String column_name, String data_type, String column_key) {
		this.order_position = Integer.valueOf(position);
		this.schema_name = table_schema;
		this.table_name = table_name;
		this.column_name = column_name;
		this.data_type = data_type;
		this.column_key = column_key;
	}
	
	public NColumn(ResultSet rs){
		try {
			order_position = rs.getDouble(rs.findColumn("position"));
			schema_name = rs.getString(rs.findColumn("table_schema"));
			table_name = rs.getString(rs.findColumn("table_name"));
			column_name = rs.getString(rs.findColumn("column_name"));
			data_type = rs.getString(rs.findColumn("data_type"));
			column_key = rs.getString(rs.findColumn("column_key"));			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	public NColumn(Node ch) {
		this.setSchema(XML.atr(ch, "schema_name") == null? null: XML.atr(ch, "schema_name"));
		this.setTable(XML.atr(ch, "table_name") == null? null: XML.atr(ch, "table_name"));
		this.setColumn(XML.atr(ch, "column_name") == null? null: XML.atr(ch, "column_name"));
		this.setColumn_key(XML.atr(ch, "column_key") == null? null: XML.atr(ch, "column_key"));
		this.setPosition(XML.atr(ch, "order_position") == null? null: Double.valueOf(XML.atr(ch, "order_position")));
		this.setData_type(XML.atr(ch, "data_type") == null? null: XML.atr(ch, "data_type"));
		this.setRowset_type(XML.atr(ch, "rowset_type") == null? null: XML.atr(ch, "rowset_type"));
	}

	public void saveXml(Document doc, Element rowE) {
		rowE.setAttribute("schema_name", schema_name);
		rowE.setAttribute("table_name", table_name);
		rowE.setAttribute("column_name", column_name);
		rowE.setAttribute("order_position", order_position.toString());
		rowE.setAttribute("data_type", data_type);
		rowE.setAttribute("rowset_type", rowset_type);
		rowE.setAttribute("column_key", column_key);
	}

	public String getSchema() {
		return schema_name;
	}

	public void setSchema(String schema) {
		this.schema_name = schema;
	}

	public String getTable() {
		return table_name;
	}

	public void setTable(String table) {
		this.table_name = table;
	}

	public String getColumn() {
		return column_name;
	}

	public void setColumn(String column) {
		this.column_name = column;
	}

	public Number getPosition() {
		return order_position;
	}

	public void setPosition(Number position) {
		this.order_position = position;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getRowset_type() {
		return rowset_type;
	}

	public void setRowset_type(String rowset_type) {
		this.rowset_type = rowset_type;
	}

	public String getColumn_key() {
		return column_key;
	}

	public void setColumn_key(String column_key) {
		this.column_key = column_key;
	}
	
	public String toString() {
		return "COLUMN: " + schema_name + "." + table_name + "." + column_name;
	}

	public boolean eq(NColumn col) {
		return col.getSchema().equals(schema_name)  && col.getTable().equals(table_name) && col.getColumn().equals(column_name);
	}
}
package rakhuba.builder.base.clientcomponents; //

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rakhuba.application.XML;
import rakhuba.generic.BO;
import rakhuba.status.Visability;

public class  NTable  extends BO{
	private String table; 
	private String schema; 
	private Number x; 
	private Number y; 
	private String type; 
	private String label;
	
	private Visability visability;
	
	public NTable(ResultSet rs){
		try {
			schema = rs.getString(rs.findColumn("table_schema"));
			table = rs.getString(rs.findColumn("table_name"));
			type = rs.getString(rs.findColumn("table_type"));
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public NTable() {

	}

	public NTable(Node ch) {
		this.setSchema(XML.atr(ch, "schema_name") == null? null: XML.atr(ch, "schema_name"));
		this.setTable(XML.atr(ch, "table_name") == null? null: XML.atr(ch, "table_name"));
		this.setX(XML.atr(ch, "x") == null? null: Double.valueOf(XML.atr(ch, "x")));
		this.setY(XML.atr(ch, "y") == null? null: Double.valueOf(XML.atr(ch, "y")));
		this.setType(XML.atr(ch, "table_type") == null? null: XML.atr(ch, "table_type"));
		this.setLabel(XML.atr(ch, "short_table_name") == null? null: XML.atr(ch, "short_table_name"));
		this.setVisability(XML.atr(ch, "visability") == null? Visability.VISIBLE: Visability.valueOf(XML.atr(ch, "visability")));
	}

	public void saveXml(Document doc, Element rowE) {		
		rowE.setAttribute("table_name", table);
		rowE.setAttribute("schema_name", schema);
		rowE.setAttribute("x", x.toString());
		rowE.setAttribute("y", y.toString());
		rowE.setAttribute("table_type", type);
		rowE.setAttribute("short_table_name", label);
		if(visability == null) {
			rowE.setAttribute("visability", Visability.VISIBLE.toString());	//TODO this is temporary default need to redo	
		}else {
			rowE.setAttribute("visability", visability.toString());		
		}
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Number getX() {
		return x;
	}

	public void setX(Number x) {
		this.x = x;
	}

	public Number getY() {
		return y;
	}

	public void setY(Number y) {
		this.y = y;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString(){return "{" + ", " + "table: " + table + ", " + "schema: " + schema + ", " + "x: " + x + ", " + "y: " + y + ", " + "table_type: " + type + ", " + "label: " + label + "}";}

	public boolean eq(NTable tb) {
		return tb.getSchema().equals(schema) && tb.getTable().equals(table);
	}

	public Visability getVisability() {
		return visability;
	}

	public void setVisability(Visability visability) {
		this.visability = visability;
	}
	
}
package rakhuba.clientcomponents;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rakhuba.application.XML;
import rakhuba.generic.BO;

public class  NType extends BO{
	private String data_type; 
	private String rowset_type; 
		
	public NType(){

	}
	
	public NType(String data_type){
	this.data_type = data_type;
}
	
	public NType(ResultSet rs){
		try {
			data_type = rs.getString(rs.findColumn("data_type"));
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public NType(Node ch) {
		this.setData_type(XML.atr(ch, "data_type") == null? null: XML.atr(ch, "data_type"));
		this.setRowset_type(XML.atr(ch, "rowset_type") == null? null: XML.atr(ch, "rowset_type"));
	}

	public void saveXml(Document doc, Element rowE) {		
		rowE.setAttribute("data_type", data_type);
		rowE.setAttribute("rowset_type", rowset_type);
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
	
	public String toString() {
		return "dt: " + data_type + " rt: " + rowset_type;
	}
	
}
package clientcomponents; //•mysql1

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class  SqliteColumn {
	public String cid; 
	public String name; 
	public String type; 
	public String notnull; 
	public String dflt_value; 
	public String pk; 
		
	public SqliteColumn(ResultSet rs){
		try {
			cid = rs.getString(rs.findColumn("cid"));
			name = rs.getString(rs.findColumn("name"));
			type = rs.getString(rs.findColumn("type"));
			notnull = rs.getString(rs.findColumn("notnull"));
			dflt_value = rs.getString(rs.findColumn("dflt_value"));
			pk = rs.getString(rs.findColumn("pk"));
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void settype(String type) {
		this.type = type;
	}

	public String getNotnull() {
		return notnull;
	}

	public void setNotnull(String notnull) {
		this.notnull = notnull;
	}

	public String getDflt_value() {
		return dflt_value;
	}

	public void setDflt_value(String dflt_value) {
		this.dflt_value = dflt_value;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
	
	public String getTypeCleaned() {
		return Arrays.asList(type.split(Pattern.quote("("))).get(0);
	}
	
	public String toString(){return "{" + "cid: " + cid + ", " + "name: " + name + ", " + "type: " + type + ", " + "notnull: " + notnull + ", " + "dflt_value: " + dflt_value + ", " + "pk: " + pk + "}";}
	
}
package rakhuba.generic;

import java.sql.Connection;
import java.sql.SQLException;

import rakhuba.builder.base.Meta;
import rakhuba.builder.base.XMLBase;
import rakhuba.login.Login;

public abstract class BaseConnection {	
	protected abstract void connectToDB();
	public abstract String end();
	public abstract Meta getClientMetaData();
	protected Connection con = null;
	protected Login login;
	protected XMLBase xmlBase;

	public XMLBase getXMLBase() {
		if(xmlBase == null) xmlBase = new XMLBase(this);
		return xmlBase;
	}
	
	public Connection getJDBC() {
		if(con == null) connectToDB();
		return con;
	}
	
	public Login getLogin() {
		return login;
	}
	
	public void closeIfOpen() {
		try {
			if(con != null && !con.isClosed()) {
				con.close();
				con = null;
			}
			
		} catch (SQLException e) {e.printStackTrace();}
	}

}

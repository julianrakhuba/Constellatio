package generic;

import java.sql.Connection;
import java.sql.SQLException;

import application.Constellatio;
import clients.Meta;
import clients.XMLBase;
import login.Login;

public abstract class BaseConnection {	
	protected abstract void connectToDB();
	public abstract String end();
	public abstract Meta getClientMetaData();
	protected Connection con = null;
	protected Login login;
	protected XMLBase xmlBase;
	private Constellatio napp;

	public BaseConnection(Login login, Constellatio napp) {
		this.login = login;
		this.napp = napp;
	}
	
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
	public Constellatio getNapp() {
		return napp;
	}

}

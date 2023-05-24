package rakhuba.connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import rakhuba.clients.Meta;
import rakhuba.clients.MicrosoftMeta;
import rakhuba.generic.BaseConnection;
import rakhuba.login.Login;

public class MicroSoftConn extends BaseConnection {
	private Meta meta;

	public MicroSoftConn(Login lgin) {
		this.login = lgin;
	}

	public void connectToDB() {
		try {
			con = DriverManager.getConnection(login.getUrl(), login.getUsername(), login.getPassword());
			Statement stmt  = con.createStatement();
	        login.getStatements().forEach(st ->{ try { stmt.execute(st); } catch (SQLException e) { e.printStackTrace(); }});
	        stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String end() {
		return "";
	}

	public Meta getClientMetaData() {
		if(meta == null) meta = new MicrosoftMeta(getJDBC());
		return  meta;		
	}
	
}

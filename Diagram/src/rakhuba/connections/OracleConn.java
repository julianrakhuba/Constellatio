package rakhuba.connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import rakhuba.builder.base.Meta;
import rakhuba.builder.base.OracleMeta;
import rakhuba.generic.BaseConnection;
import rakhuba.login.Login;

public class OracleConn extends BaseConnection {
	private Meta meta;

	public OracleConn(Login lgin) {
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
		if(meta == null) meta = new OracleMeta(getJDBC());
		return  meta;
	}

}

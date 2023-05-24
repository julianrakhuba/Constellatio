package rakhuba.connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import rakhuba.clients.Meta;
import rakhuba.clients.MySqlMeta;
import rakhuba.generic.BaseConnection;
import rakhuba.login.Login;

public class MysqlConn extends BaseConnection {
	private Meta meta;

	public MysqlConn(Login lgin) {
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
			if(e.getErrorCode() == 1045) {
				System.out.println("WRONG DB LOGIN • " + e.getErrorCode() + " " +  e.getLocalizedMessage() + "    con:  " + con);
			}
		}
	}

	public String end() {
		return ";";
	}

	public Meta getClientMetaData() {
		if(meta == null) meta = new MySqlMeta(getJDBC());
		return  meta;
	}
}

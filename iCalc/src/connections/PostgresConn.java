package connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import application.Constellatio;
import clients.Meta;
import clients.PostgresMeta;
import generic.BaseConnection;
import login.Login;

public class PostgresConn extends BaseConnection {
	private Meta meta;

	public PostgresConn(Login lgin, Constellatio napp) {
		super (lgin, napp);
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
		if(meta == null) meta = new PostgresMeta(getJDBC());
		return  meta;
	}

}

package connections;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import application.Constellatio;
import clients.Meta;
import clients.SqliteMeta;
import generic.BaseConnection;
import login.Login;

public class SqliteConn extends BaseConnection {
	private Meta meta;

	public SqliteConn(Login lgin, Constellatio napp) {
		super (lgin, napp);
	}
	
	public void connectToDB() {
        try {
        	con = DriverManager.getConnection(login.getUrl());
        	Statement stmt  = con.createStatement();
	        login.getStatements().forEach(st ->{ try { stmt.execute(st); } catch (SQLException e) { e.printStackTrace(); }});
	        stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public String end() {
		return ";";
	}
	
	public Meta getClientMetaData() {
		if(meta == null) meta = new SqliteMeta(getJDBC(), login.getShcemas());
		return  meta;
	}
}

package connections;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
		this.copySampleDb();
				
        try {
        	con = DriverManager.getConnection(login.getUrl());
        	Statement stmt  = con.createStatement();
        	login.getStatements().forEach(st ->{ try { 
	        	stmt.execute(st);
	        } catch (SQLException e) { e.printStackTrace(); }});

	        stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	private void copySampleDb() {
		this.getLogin().getShcemas().forEach(sk ->{
			try {
	            InputStream inputStream =  getClass().getResourceAsStream("/"+sk+".db");
	            Path targetPath = Path.of(this.getNapp().getConfigurationPath() + sk + ".db");
	            if(!targetPath.toFile().exists()) {
	            	Files.copy(inputStream, targetPath);
	 	            System.out.println("Database copied to: " + targetPath);
//	 	           <statement command="Attach '/Users/julianrakhuba/dbs/Chinook.db' as 'Chinook';"/>
//	 	            <statement command="Attach '/Users/julianrakhuba/dbs/sakila.db' as 'sakila';"/>
//	 	            <statement command="Attach '/Users/julianrakhuba/dbs/North.db' as 'North';"/>
	 	            
	            }
	        } 
		  catch (IOException e) { 
//			  e.printStackTrace(); 
		  }
		});		
	}

	public String end() {
		return ";";
	}
	
	public Meta getClientMetaData() {
		if(meta == null) meta = new SqliteMeta(getJDBC(), login.getShcemas());
		return  meta;
	}
}

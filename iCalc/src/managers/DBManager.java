package managers;

import java.util.HashMap;

import application.Constellatio;
import builder.Build;
import connections.MicroSoftConn;
import connections.MysqlConn;
import connections.OracleConn;
import connections.PostgresConn;
import connections.SqliteConn;
import generic.BaseConnection;
import login.Login;
import login.Logins;
import status.ConnectionStatus;

public class DBManager {
	private Constellatio napp;
	private Logins logins = new Logins();
	private HashMap<Login,BaseConnection> connections = new HashMap<Login,BaseConnection>();	
	private BaseConnection activeConnection;
	
	public DBManager(Constellatio napp) {
		this.napp = napp;
		logins.getLoginList().forEach(lgin ->{
			if(lgin.getDb().equals("mysql")) {
				connections.put(lgin, new MysqlConn(lgin));
			}else if(lgin.getDb().equals("sqlite")){
				connections.put(lgin, new SqliteConn(lgin));
			}else if(lgin.getDb().equals("oracle")){
				connections.put(lgin, new OracleConn(lgin));	
			}else if(lgin.getDb().equals("postgres")){
				connections.put(lgin, new PostgresConn(lgin));
			}else if(lgin.getDb().equals("sqlserver")){
				connections.put(lgin, new MicroSoftConn(lgin));
			}else {
				//why I am here??? No dtatbase driver?");
			}			
		});
	}

	public Logins getLogins() {
		return logins;
	}
		
	public BaseConnection getActiveConnection() {
		return activeConnection;
	}

	public void activateConnectionForConnection(Login login) {
		this.closeUserConnectionIfOpen();//close previous user connection
//		if(PasswordUtils.verifyUserPassword(login.getPassword(), login.getSecuredPassword(), login.getSalt())) {//IF VALID SECURED PASSWORD
		activeConnection = connections.get(login);	
		//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
			Build b2 = new Build();
			b2.loops(activeConnection);		
		//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
	
		//app config
		napp.getMenu().getFileMenu().getNewMenu().getItems().clear();
		activeConnection.getXMLBase().getSchemas().forEach(sk -> napp.getMenu().getFileMenu().addNewSchemaToMenu(sk));
		if(!napp.getMenu().getFileMenu().getSavePasswordMenuItem().isSelected()) login.setPassword("");//do not save visual password
		logins.saveConnectionsToXML();//TODO TEMPORARY DISABLED SAVE !!!!!!!!!!!!!!!!!!!!!
		napp.light.setStatus(ConnectionStatus.CONNECTED);
		napp.getMenu().getFileMenu().activateConnectionMenus();
	}
	

	public void closeUserConnectionIfOpen() {
		if(activeConnection != null) activeConnection.closeIfOpen();
		napp.light.setStatus(ConnectionStatus.DISCONNECTED);
		napp.getMenu().getFileMenu().deactivateConnectionMenus();
		activeConnection = null;
	}
}

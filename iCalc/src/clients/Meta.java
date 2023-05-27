package clients;

import java.sql.Connection;
import java.sql.SQLException;

import clientcomponents.ClientDAO;
import clientcomponents.NColumn;
import clientcomponents.NKey;
import clientcomponents.NTable;
import clientcomponents.NType;
import javafx.collections.ObservableList;

public abstract class Meta {
	public Connection connection;
	
	public String typesSql;
	public String tablesSql;
	public String columnsSql;
	public String keysSql;
	
	public ClientDAO clientDAO;
	
	public ObservableList<NType> getDataTypes() {
		return clientDAO.getTypes(typesSql);
	}
	
	public ObservableList<NTable> getTables() {
		return clientDAO.getTables(tablesSql);
	}
	
	public ObservableList<NColumn> getColumns() {
		return clientDAO.getColumns(columnsSql);
	}
	
	public ObservableList<NKey> getKeys() {
		return clientDAO.getKeys(keysSql);
	}

	public void closeConnection() {
		try { if(connection != null && !connection.isClosed()) { connection.close(); }
		} catch (SQLException e) {e.printStackTrace();}
	}
}

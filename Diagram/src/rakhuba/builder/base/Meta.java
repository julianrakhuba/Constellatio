package rakhuba.builder.base;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import rakhuba.builder.base.clientcomponents.ClientDAO;
import rakhuba.builder.base.clientcomponents.NColumn;
import rakhuba.builder.base.clientcomponents.NKey;
import rakhuba.builder.base.clientcomponents.NTable;
import rakhuba.builder.base.clientcomponents.NType;

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

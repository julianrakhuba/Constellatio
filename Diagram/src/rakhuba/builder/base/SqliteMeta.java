package rakhuba.builder.base;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rakhuba.builder.base.clientcomponents.ClientDAO;
import rakhuba.builder.base.clientcomponents.NColumn;
import rakhuba.builder.base.clientcomponents.NKey;
import rakhuba.builder.base.clientcomponents.NTable;
import rakhuba.builder.base.clientcomponents.NType;

public class SqliteMeta extends Meta {	
	private ObservableList<NType> dataTypesList;
	private ObservableList<NColumn> columnsList;
	private ObservableList<NTable> tablesList;
	private ObservableList<NKey> keysList;
	
	//temporary here need to delete ??
	 List<String> schemas;
	 
	public SqliteMeta(Connection connection, List<String> schemas) {
		this.connection = connection;
		this.schemas = schemas;
		clientDAO = new ClientDAO(this.connection);
	}

	//CLIENT OVERWRITE
	public ObservableList<NType> getDataTypes() {
		if(dataTypesList != null) return dataTypesList;
		dataTypesList = FXCollections.observableArrayList();
		HashSet<String> types = new HashSet<String>();
		this.getColumns().forEach(col-> types.add(col.getData_type()));
		types.forEach(tp -> dataTypesList.add(new NType(tp)));
		return dataTypesList;
	}
	
	public ObservableList<NColumn> getColumns() {
		if(columnsList != null) return columnsList;
		columnsList = FXCollections.observableArrayList();
		this.getTables().forEach(tbl -> {
			String sqlt = " PRAGMA " + tbl.getSchema() + ".table_info('" + tbl.getTable() + "');";
			clientDAO.getSqliteColumns(sqlt).forEach(scol->{				
				NColumn newcol = new NColumn(scol.getCid(), tbl.getSchema(), tbl.getTable(), scol.getName(), scol.getTypeCleaned(), scol.getPk());
				columnsList.add(newcol);
			});
		});	
		return columnsList;
	}
	
	public ObservableList<NTable> getTables() {
		if(tablesList != null) return tablesList;
		tablesList = FXCollections.observableArrayList();
		schemas.forEach(sch -> {
			String sql = "SELECT '" + sch + "' as table_schema, tbl_name as table_name, CASE type WHEN 'table' THEN 'BASE TABLE'  WHEN 'view' THEN 'VIEW' END table_type FROM " + sch + ".sqlite_master where  type IN ('table','view') AND name NOT LIKE 'sqlite_%'";
			tablesList.addAll(clientDAO.getTables(sql));
		});		
		return tablesList;
	}

	public ObservableList<NKey> getKeys() {
		if(keysList != null) return keysList;
		keysList = FXCollections.observableArrayList();
		this.getTables().forEach(tbl -> {
			String sql = " PRAGMA "+ tbl.getSchema() + ".foreign_key_list('" + tbl.getTable() + "');";
			clientDAO.getSqliteKey(sql).forEach(skey -> {
				NKey newkeyBO = new NKey("FOREIGN KEY", tbl.getSchema(), tbl.getTable(), skey.getFrom(), tbl.getSchema(), skey.getTable(), skey.getTo());
			 keysList.add(newkeyBO);
		  });
		});			
		return keysList;
	}
}

package rakhuba.clients;

import java.sql.Connection;

import rakhuba.clientcomponents.ClientDAO;


public class MySqlMeta  extends Meta{
	
	public MySqlMeta(Connection connection) {
		this.connection = connection;
		typesSql = "select distinct(data_type) 'data_type' FROM information_schema.COLUMNS";
		tablesSql = "select table_schema  as 'table_schema',  table_name as 'table_name', table_type as 'table_type' from information_schema.tables where table_schema not in ('sys', 'mysql','performance_schema')";
		columnsSql = "select ordinal_position as 'position', table_schema as 'table_schema', table_name as 'table_name', column_name as 'column_name', data_type as 'data_type', COLUMN_KEY as 'column_key' FROM information_schema.COLUMNS where table_schema not in ('sys', 'mysql','performance_schema')";
		keysSql = "select t.CONSTRAINT_TYPE as 'constraint', k.TABLE_SCHEMA as 'table_schema', k.TABLE_NAME as 'table_name', k.COLUMN_NAME as 'column_name', k.REFERENCED_TABLE_SCHEMA as 'ref_table_schema', k.REFERENCED_TABLE_NAME as 'ref_table_name' , k.REFERENCED_COLUMN_NAME as 'ref_column_name' from information_schema.KEY_COLUMN_USAGE k join  information_schema.TABLE_CONSTRAINTS t on k.CONSTRAINT_CATALOG = t.CONSTRAINT_CATALOG and k.CONSTRAINT_SCHEMA = t.CONSTRAINT_SCHEMA and k.CONSTRAINT_NAME = t.CONSTRAINT_NAME and k.TABLE_SCHEMA = t.TABLE_SCHEMA and k.TABLE_NAME = t.TABLE_NAME where t.CONSTRAINT_TYPE in ('PRIMARY KEY', 'FOREIGN KEY') and k.table_schema not in ('sys', 'mysql','performance_schema')";
		clientDAO = new ClientDAO(this.connection);
	}	
}

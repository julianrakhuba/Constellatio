package rakhuba.builder.base;

import java.sql.Connection;

import rakhuba.builder.base.clientcomponents.ClientDAO;

public class MicrosoftMeta extends Meta {
	public MicrosoftMeta(Connection connection) {
		this.connection = connection;
		typesSql = "Select distinct(data_type) as 'data_type' from master.INFORMATION_SCHEMA.COLUMNS";
		tablesSql = "select table_schema, table_name , table_type from master.INFORMATION_SCHEMA.tables";	
		columnsSql = "select ordinal_position as position, table_schema, table_name, column_name, data_type from master.INFORMATION_SCHEMA.columns";
		keysSql = "select tc.constraint_type as 'constraint', kcu.table_schema, kcu.table_name, kcu.column_name, ccu.table_schema as ref_table_schema, ccu.table_name as ref_table_name, ccu.column_name as ref_column_name from mydb.INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu left join mydb.INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc on rc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME left join mydb.information_schema.constraint_column_usage ccu on ccu.constraint_name = rc.UNIQUE_CONSTRAINT_NAME  join mydb.information_schema.table_constraints tc on tc.constraint_name = kcu.constraint_name where tc.constraint_type in ('PRIMARY KEY', 'FOREIGN KEY')";
		clientDAO = new ClientDAO(this.connection);
	}
}
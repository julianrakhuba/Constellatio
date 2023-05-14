package rakhuba.builder.base;

import java.sql.Connection;

import rakhuba.builder.base.clientcomponents.ClientDAO;

public class PostgresMeta extends Meta {
	
	public PostgresMeta(Connection connection) {
		this.connection = connection;
		typesSql = "select distinct(data_type) as data_type from information_schema.columns";
		tablesSql = "select table_schema,  table_name , table_type from information_schema.tables where table_schema not in ('information_schema', 'pg_catalog')";	
		columnsSql = "select ordinal_position as position, table_schema, table_name, column_name, data_type, 'need_to_add_key' as column_key from information_schema.columns  where table_schema not in ('information_schema', 'pg_catalog')";
		keysSql = "select  tc.constraint_type as constraint , kcu.table_schema, kcu.table_name, kcu.column_name, ccu.table_schema as ref_table_schema , ccu.table_name as ref_table_name, ccu.column_name as ref_column_name from information_schema.key_column_usage kcu left join information_schema.referential_constraints rc on kcu.constraint_name  = rc.constraint_name  left join information_schema.constraint_column_usage ccu on ccu.constraint_name  = rc.constraint_name join information_schema.table_constraints tc on tc.constraint_name = kcu.constraint_name where tc.constraint_type in ('PRIMARY KEY', 'FOREIGN KEY')";
		clientDAO = new ClientDAO(this.connection);
	}
}
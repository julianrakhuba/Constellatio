package rakhuba.builder.base;

import java.sql.Connection;

import rakhuba.builder.base.clientcomponents.ClientDAO;

public class OracleMeta extends Meta{
	
	public OracleMeta(Connection connection) {
		this.connection = connection;
		typesSql = "select distinct(cols.data_type) \"data_type\" from all_tab_columns cols WHERE OWNER in ('COMPUTERSTORE','SAKILA')";
		tablesSql = "select owner as \"table_schema\", VIEW_NAME as \"table_name\", 'VIEW' as \"table_type\" from all_views WHERE OWNER in ('COMPUTERSTORE','SAKILA') union select owner as \"table_schema\", TABLE_NAME as \"table_name\", 'BASE_TABLE' as \"table_type\"  from all_tables  WHERE OWNER in ('COMPUTERSTORE','SAKILA')";
		columnsSql = "select cols.column_id  as \"position\", cols.owner as \"table_schema\", cols.table_name  as \"table_name\",  cols.column_name  as \"column_name\", cols.data_type as \"data_type\" from all_tab_columns cols WHERE OWNER in ('COMPUTERSTORE','SAKILA')";
		keysSql = "SELECT 'PRIMARY KEY' \"constraint\", cons.owner \"table_schema\", cols.table_name \"table_name\", cols.column_name \"column_name\", '' as \"ref_table_schema\",   '' as \"ref_table_name\",  '' as \"ref_column_name\" FROM all_constraints cons, all_cons_columns cols WHERE cons.constraint_type = 'P' AND cols.OWNER in ('COMPUTERSTORE','SAKILA') AND cons.constraint_name = cols.constraint_name AND cons.owner = cols.owner  union SELECT 'FOREIGN KEY' \"constraint\", a.owner \"table_schema\", a.table_name \"table_name\", a.column_name \"column_name\",  b.owner as \"ref_table_schema\",   b.table_name \"ref_table_name\",  b.column_name \"ref_column_name\"  FROM all_cons_columns a  JOIN all_constraints c ON a.owner = c.owner  AND a.constraint_name = c.constraint_name  JOIN all_constraints c_pk ON c.r_owner = c_pk.owner  AND c.r_constraint_name = c_pk.constraint_name  JOIN all_cons_columns b ON C_PK.owner = b.owner  AND  C_PK.CONSTRAINT_NAME = b.constraint_name AND b.POSITION = a.POSITION  WHERE c.constraint_type = 'R'  and c.OWNER in ('COMPUTERSTORE','SAKILA')";
		clientDAO = new ClientDAO(this.connection);
	}
}
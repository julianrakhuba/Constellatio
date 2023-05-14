package rakhuba.generic;

import rakhuba.application.Nnode;
import rakhuba.logic.Field;
import rakhuba.logic.Join;
import rakhuba.status.SqlType;

public class SLayer extends LAY {
	public SLayer(Nnode nnode, SqlType type) {
		super(nnode, type);	
//		sqlType.setValue(type); //= new SimpleObjectProperty<SqlType>(type);
		nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getXColumns().filtered(c ->  (c.getSchema().equals(nnode.getSchema()) && c.getTable().equals(nnode.getTable()))).forEach(xcol -> {
			//CREATE FIELD
			Field field = new Field(this);
			field.setSchema(xcol.getSchema());
			field.setTable(xcol.getTable());
			field.setColumn(xcol.getColumn());
			field.setRowset_type(xcol.getRowset_type());
			field.setColumn_key(xcol.getColumn_key());
			
			field.setSQL_Column_name(xcol.getColumn());
			field.setAliase(this.getAliase() + "_" + xcol.getColumn());
			field.setText(xcol.getColumn());
			this.addField(field);
			
			//CREATE JOINS
			nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getKeys().filtered(k -> 
			k.getConst().equals("FOREIGN KEY") &&
			k.getSchema().equalsIgnoreCase(field.getSchema()) &&
			k.getTable().equalsIgnoreCase(field.getTable()) &&
			k.getColumn().equalsIgnoreCase(field.getColumn())
			).forEach(key -> {
				Join join = new Join(this, key.getColumn(), key.getSchema(), key.getTable(), key.getColumn(), key.getRSchema(), key.getRTable(), key.getRColumn());
				field.addJoin(join);
			});
			
			//r keys
			nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getKeys().filtered(k -> 
			k.getConst().equals("FOREIGN KEY") &&		
			k.getRSchema().equalsIgnoreCase(field.getSchema()) &&
			k.getRTable().equalsIgnoreCase(field.getTable()) && 
			k.getRColumn().equalsIgnoreCase(field.getColumn())		
					).forEach(key -> {
				Join join = new Join(this, key.getRColumn(), key.getRSchema() , key.getRTable() ,  key.getRColumn() , key.getSchema() , key.getTable() , key.getColumn());
				field.addJoin(join);
			});
		});		
	}
}

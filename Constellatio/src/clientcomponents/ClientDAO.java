package clientcomponents;//•

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import generic.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import rakhuba.clientcomponents.NColumn;
//import rakhuba.clientcomponents.NKey;
//import rakhuba.clientcomponents.NTable;
//import rakhuba.clientcomponents.NType;
//import rakhuba.clientcomponents.SqliteColumn;
//import rakhuba.clientcomponents.SqliteKey;

public class ClientDAO  extends DAO{
	
	public ClientDAO(Connection connection){
		this.connection = connection;
	}
	
	public ObservableList<NTable> getTables(String sql){		
		ObservableList<NTable> list = FXCollections.observableArrayList();
		super.openStatement();
		ResultSet resultSet = super.executeQuery(sql);		
		try {while (resultSet.next()) {
			NTable bo = new  NTable(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	public ObservableList<NColumn> getColumns(String sql){		
		ObservableList<NColumn> list = FXCollections.observableArrayList();
		super.openStatement();
		ResultSet resultSet = super.executeQuery(sql);		
		try {while (resultSet.next()) {
			NColumn bo = new  NColumn(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	public ObservableList<NKey> getKeys(String sql){		
		ObservableList<NKey> list = FXCollections.observableArrayList();
		super.openStatement();
		ResultSet resultSet = super.executeQuery(sql);			
		try {while (resultSet.next()) {
			NKey bo = new  NKey(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	public ObservableList<NType> getTypes(String sql){		
		ObservableList<NType> list = FXCollections.observableArrayList();
		super.openStatement();
		ResultSet resultSet = super.executeQuery(sql);		
		try {while (resultSet.next()) {
			NType bo = new  NType(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	
	//SQLITE ONLY
	public ObservableList<SqliteColumn> getSqliteColumns(String sql){		
		ObservableList<SqliteColumn> list = FXCollections.observableArrayList();
		String query = sql + ";";
		
		super.openStatement();
		ResultSet resultSet = super.executeQuery(query);
		try {while (resultSet.next()) {
				SqliteColumn bo = new SqliteColumn(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	public ObservableList<SqliteKey> getSqliteKey(String sql){
		ObservableList<SqliteKey> list = FXCollections.observableArrayList();
		String query = sql + ";";
		super.openStatement();
		ResultSet resultSet = super.executeQuery(query);
		try {while (resultSet.next()) {
				SqliteKey bo = new SqliteKey(resultSet);
				list.add(bo);
		}} 
		catch (SQLException e) {e.printStackTrace();}	
		super.closeStatement();
		return list;
	}
	
	
	
	
}
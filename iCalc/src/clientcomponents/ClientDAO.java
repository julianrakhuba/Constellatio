/*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package clientcomponents;//•

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import generic.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
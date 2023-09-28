/*******************************************************************************
 *   Copyright (c) 2023 Constellatio
 *  
 *   This software is released under the [Educational/Non-Commercial License or Commercial License, choose one]
 *  
 *   Educational/Non-Commercial License (GPL):
 *  
 *   Permission is hereby granted, free of charge, to any person or organization
 *   obtaining a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including without
 *   limitation the rights to use, copy, modify, merge, publish, distribute,
 *   sublicense, and/or sell copies of the Software, and to permit persons to
 *   whom the Software is furnished to do so, subject to the following conditions:
 *  
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *  
 *   THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 *  
 *   Commercial License:
 *  
 *   You must obtain a separate commercial license if you
 *   wish to use this software for commercial purposes. Please contact
 *   rakhuba@gmail.com for licensing information.
 *  
 *  
 *******************************************************************************/
package generic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.Nnode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import logic.SQL;

public class OpenDAO {
	private Nnode nnode;	

	private  Statement statement = null; 
	private  ResultSet resultSet = null;
	
	public OpenDAO(Nnode nnode) {
		this.nnode = nnode;
	}
	
	public void openStatement(){
		try {
			statement = nnode.getNmap().getNapp().getDBManager().getActiveConnection().getJDBC().createStatement();
			statement.setQueryTimeout(30);
		} 
		catch (SQLException e) { e.printStackTrace(); } 
	}
	
	public void closeStatement(){
		try { if(!(resultSet == null)){resultSet.close();} statement.close(); } 
		catch (SQLException e) {e.printStackTrace();}
	}
	
	public ResultSet executeQuery(String  query){
		nnode.getNmap().getNapp().getConsole().addTextToQue(query + "\n");
		try {
			resultSet = statement.executeQuery(query);
//			statement.cancel();
		}catch (SQLException ex) { 
			ex.printStackTrace(); 
			} //1142 denied access to table ex.getErrorCode() 
		return resultSet;
	}

	
	public ObservableList<OpenBO> readDB(SQL sql, LAY lay) {
		ClipboardContent content = new ClipboardContent();
		ObservableList<OpenBO> openBOs2 = FXCollections.observableArrayList();
//		String statement = sql.append(getDB().end()).toString();
		String statement = sql.endStatement(getDB()).toString();
		content.putString(statement);
		Clipboard.getSystemClipboard().setContent(content);	
		this.openStatement();
		ResultSet resultSet = this.executeQuery(statement);
//		try {
//			resultSet.last();
//			//this will not work on postgres
//			resultSet.beforeFirst();
//		} catch (SQLException e1) { e1.printStackTrace();}
		
		try {while (resultSet.next()) {
			openBOs2.add(new OpenBO(resultSet, lay));
		}} catch (SQLException e) {e.printStackTrace();}
		this.closeStatement();
		return openBOs2;
	}
	
	
	//NEW
	public ArrayList<String> readDistinctValues(SQL sql){
		ArrayList<String> distinctValues = new ArrayList<String>();
//		String query = sql.append(getDB().end()).toString();
		this.openStatement();
		ResultSet resultSet = this.executeQuery(sql.endStatement(getDB()).toString());
		try {while (resultSet.next()) {			
			String value = resultSet.getString(1);
			distinctValues.add(value == null ? "null" : value);
		}}
		catch (SQLException e) {e.printStackTrace();}	
		this.closeStatement();
		return distinctValues;
	}

//	public ArrayList<String> readPivotChache(SQL sql){
//		ArrayList<String> pivotColumns = new ArrayList<String>();
//		this.openStatement(); 
//		sql.append(getDB().end());
//		ResultSet resultSet = this.executeQuery(sql.toString());
//		try {
//			while (resultSet.next()) {
//				String value = resultSet.getString(1);
//				pivotColumns.add(value);
//			}
//		}
//		catch (SQLException e) {e.printStackTrace();}
//		this.closeStatement();
//		return pivotColumns;
//	}
	
//	public void print(ResultSet resultSet) {
//		DBTablePrinter.printResultSet(resultSet);
//	}
	
	private BaseConnection getDB() {
		return nnode.getNmap().getNapp().getDBManager().getActiveConnection();
	}
	
}

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
	public Nnode nnode;	

	private  Statement statement = null; 
	private  ResultSet resultSet = null;
	
	public OpenDAO(Nnode nnode) {
		this.nnode = nnode;
	}
	
	public void openStatement(){
		try {
			statement = nnode.nmap.napp.getDBManager().getActiveConnection().getJDBC().createStatement();
			statement.setQueryTimeout(30);
		} 
		catch (SQLException e) { e.printStackTrace(); } 
	}
	
	public void closeStatement(){
		try { if(!(resultSet == null)){resultSet.close();} statement.close(); } 
		catch (SQLException e) {e.printStackTrace();}
	}
	
	public ResultSet executeQuery(String  query){
//		System.out.println(query);
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
		String statement = sql.append(getDB().end()).toString();
		content.putString(statement);
		Clipboard.getSystemClipboard().setContent(content);	
		this.openStatement();
//		System.out.println("OpenDAO.executeQuery()");
		ResultSet resultSet = this.executeQuery(statement);
//		System.out.println("OpenDAO create BOs");
//		try {
//			resultSet.last();
//			//this will not work on postgres
//			System.out.println("Table contains "+resultSet.getRow()+" rows");
//			resultSet.beforeFirst();
//		} catch (SQLException e1) { e1.printStackTrace();}
		
		try {while (resultSet.next()) {
			openBOs2.add(new OpenBO(resultSet, lay));
		}} catch (SQLException e) {e.printStackTrace();}
		this.closeStatement();
//		System.out.println("OpenDAO close statement");
		return openBOs2;
	}
	
	
	//NEW
	public ArrayList<String> readDistinctValues(SQL sql){
		ArrayList<String> distinctValues = new ArrayList<String>();
		String query = sql.append(getDB().end()).toString();
		this.openStatement();
		ResultSet resultSet = this.executeQuery(query);
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
		return nnode.nmap.napp.getDBManager().getActiveConnection();
	}
	
}

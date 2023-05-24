package generic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import constellatio.Constellatio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import logic.SQL;

public abstract class DAO {
	public Constellatio napp;	
	public  Connection connection = null; 
	private  Statement statement = null; 
	private  ResultSet resultSet = null;
	
	public void openStatement(){
		try {
			if(napp != null) {
				statement = napp.getDBManager().getActiveConnection().getJDBC().createStatement();				
			}else {
				statement = connection.createStatement();
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} //1045 wrong password e.getErrorCode()
	}
	
	
	public void closeStatement(){
		try {
		if(!(resultSet == null)){resultSet.close();}
			statement.close(); 
		} 
		catch (SQLException e) {e.printStackTrace();}
	}
	
	
	public ResultSet executeQuery(String  query){
		try {
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException ex) { 
			ex.printStackTrace(); 
			} //1142 denied access to table ex.getErrorCode() 
		return resultSet;
	}
	
	public void executeUpdate(String query){		
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {e.printStackTrace();}
	}
	

	
	public int getSqlSize(SQL sql){
		int rowsCountInDB = 0;
		this.openStatement();
		ResultSet resultSet = this.executeQuery(sql.COUNTDB().toString());
		try {resultSet.next(); rowsCountInDB = resultSet.getInt("countSql");} catch (SQLException e) {e.printStackTrace();}
		this.closeStatement();
		return rowsCountInDB;	
	}
//	public ArrayList<String> readPivotColumns(SQL sql, Field field){
//		ArrayList<String> pivotColumns = new ArrayList<String>();
//		this.openStatement();		
//		ResultSet resultSet = this.executeQuery(sql.toString());
//		
//		try {
////			resultSet.last();
////			int row = resultSet.getRow();
////			
////			if(row < 25) {//REMOVED BECOUSE MYSQL IS FORWARD ONLY
////				resultSet.beforeFirst();			
////				while (resultSet.next()) pivotColumns.add(resultSet.getString(resultSet.findColumn(field.getSQL_Column_name())));
//				while (resultSet.next()) pivotColumns.add(resultSet.getString(resultSet.findColumn(field.getFunction_Column())));
//
////			}else {
////				//Create Message to to user to let them know to reduce pivot selection
////				
////			}
//		}
//		catch (SQLException e) {e.printStackTrace();}
//		this.closeStatement();
//		return pivotColumns;
//	}
	
	public ObservableList<OpenBO> readDB(SQL sql, LAY lay) {
		ClipboardContent content = new ClipboardContent();
		ObservableList<OpenBO> openBOs2 = FXCollections.observableArrayList();
		String statement = sql.toString();
		content.putString(statement);
		Clipboard.getSystemClipboard().setContent(content);	
		this.openStatement();
		ResultSet resultSet = this.executeQuery(statement);
		try {while (resultSet.next()) {
			openBOs2.add(new OpenBO(resultSet, lay));
		}} catch (SQLException e) {e.printStackTrace();}
		this.closeStatement();
		return openBOs2;
	}
	
	
	//NEW
	public ArrayList<String> readDistinctValues(SQL sql, String column){
		ArrayList<String> distinctValues = new ArrayList<String>();
		String query = sql.toString();
		this.openStatement();
		ResultSet resultSet = this.executeQuery(query);
		try {while (resultSet.next()) {
			String value = resultSet.getString(resultSet.findColumn(column));
			distinctValues.add(value == null ? "null" : value);
		}}
		catch (SQLException e) {e.printStackTrace();}	
		this.closeStatement();
		return distinctValues;
	}

}

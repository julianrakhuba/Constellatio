package rakhuba.builder;

import javafx.collections.ObservableList;
import rakhuba.clientcomponents.NColumn;

public class Options {
	private ObservableList<NColumn> columns;
	
	public Options(ObservableList<NColumn> columns) {
		this.columns = columns;
	}

	public String getImportDate() {
		for (NColumn col : columns) {if(col.getRowset_type().equals("Date")) return  true +"";}
		return false + "";
	}
	
	public String getTimestamp() {
		for (NColumn col : columns) {if(col.getRowset_type().equals("Timestamp")) return  true +"";}
		return false + "";
	}
	
	public String getTime() {
		for (NColumn col : columns) {if(col.getRowset_type().equals("Time")) return  true +"";}
		return false + "";
	}
}

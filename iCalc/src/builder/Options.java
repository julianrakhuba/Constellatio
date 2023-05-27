package builder;

import clientcomponents.NColumn;
import javafx.collections.ObservableList;

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

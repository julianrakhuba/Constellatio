package generic;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import pivot.FieldVersion;
//import rakhuba.generic.OpenBO;

public class TableCellNumber extends TableCell<OpenBO, Number> {
	private FieldVersion version;
	
    public TableCellNumber(FieldVersion version) {
    	this.version = version;
    	this.setPadding(new Insets(6,4,6,4));
	}

    protected void updateItem(Number value, boolean empty) {
        super.updateItem(value, empty);
        if (value == null || empty) {
            setText(null);
        } else {
        	String id = version.getField().getFormat().getId();
        	if(id.equals("plain")) {
        		setText(value.toString());
        	}else if(id.equals("currency")) {// currency
            	setText(NumberFormat.getCurrencyInstance().format(value));            	
        	}else if(id.matches("double|longdouble")) {// 0.00
            	setText(String.format(version.getField().getFormat().getFormatString(), value.doubleValue()));            	
        	}else if(id.equals("month") && value.intValue() > 0 && value.intValue() <= 12) {//Month
            	setText(new DateFormatSymbols().getShortMonths()[value.intValue() -1]);            	
        	}else if(id.equals("weekday") && value.intValue() >=0 && value.intValue() <= 6) {//Weekday
            	setText(new DateFormatSymbols().getShortWeekdays()[value.intValue() +1]);            	
        	}else if(id.equals("year") && value.intValue() > 0) {//Year
        		setText(value.toString());
        	}else {
        		setText("-");        		
        	}
        }
    }
}
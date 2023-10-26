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

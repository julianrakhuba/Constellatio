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
package application;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.scene.control.Label;

public class InfoLabel extends Label {
	private Double totalSum = Double.valueOf(0);
	private String type;

	public InfoLabel(String type) {
		this.type = type;
		this.setText("");
	}

	public void add(Number number) {
		NumberFormat df = new DecimalFormat("#.##");
		totalSum = totalSum + number.doubleValue();
		String ft = df.format(totalSum);
		this.setText( type +" "+ ft);		
	}
	
	public void setCountValue(int count) {
		this.setText(type + " " + count);
	}
	
	public void clear() {
		totalSum = 0.0;
		this.setText("");
	}
}

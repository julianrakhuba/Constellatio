/*******************************************************************************
 * /*******************************************************************************
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
 *  *******************************************************************************/
 *******************************************************************************/
package file;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

@SuppressWarnings({ "rawtypes", "unused", "unchecked" })

public class NLineChart  extends NChart  {
	private CategoryAxis x = new CategoryAxis();
	private NumberAxis y = new NumberAxis();
	private LineChart chart;
	private NSheet nSheet;
	
	
	public NLineChart(NSheet nSheet) {
		this.nSheet = nSheet;
		chart = new LineChart(x, y);
		y.setMinorTickVisible(false);
		chart.setMinWidth(0);
		chart.setAnimated(false);
		chart.setVerticalGridLinesVisible(false);
		chart.setAlternativeRowFillVisible(true);		
	}

	public Region getRegion() {
		return chart;
	}
	
	public void refresh(ObservableList<String> categories, ObservableList<Series<String, Number>> data) {
		x.getCategories().clear();
		x.setCategories(categories);
		chart.setData(data);
	}

}

/*******************************************************************************
 *  MIT License
 *
 * Copyright (c) 2023 Julian Rakhuba
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package layer;

import java.util.HashMap;

import javafx.scene.paint.Color;
import status.ColorMode;

public class LayColors {
	private HashMap<ColorMode, String> hm = new HashMap<ColorMode, String>();

	private String sql = "#7cd0f9";
	private String sqlsel = "#99ddff";
	private String sqlj = "#7cbbf9";
	private String sqljsel = "#99ccff";
	private String sqld = "#7cbbf9";
	private String sqldsel = "#99ccff";
	private String edit = "#ff99cc";
	private String view = "#7fff7f";
	private String formula = "#ffcf0f";
	
	public LayColors() {
		hm.put(ColorMode.EDIT, edit);
		hm.put(ColorMode.VIEW, view);
		hm.put(ColorMode.FORMULA, formula);
		hm.put(ColorMode.SQL, sql);
		hm.put(ColorMode.SQLSELECTED, sqlsel);
		hm.put(ColorMode.SQLJ, sqlj);
		hm.put(ColorMode.SQLJSELECTED, sqljsel);
		hm.put(ColorMode.SQLD, sqld);
		hm.put(ColorMode.SQLDSELECTED, sqldsel);
	}
	
	public Color getColor(ColorMode mode){
		return Color.valueOf(hm.get(mode));
	}
}

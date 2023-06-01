package generic;

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
	
	public Color getColors(ColorMode mode){
		return Color.valueOf(hm.get(mode));
	}
}

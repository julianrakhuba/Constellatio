package rakhuba.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;
import rakhuba.status.ColorMode;

public class LayColors {
	private HashMap<ColorMode, List<String>> hm = new HashMap<ColorMode, List<String>>();
	//baby color
//	private List<String> sql = Arrays.asList("#fafdff","#42b7ff","#d1edff","#8fd4ff");
//	private List<String> sqlsel = Arrays.asList("#fff","#a3dcff","#ebf7ff","#bde6ff");
//	private List<String> sqlj = Arrays.asList("#e0edf9","#63aef9","#c7e0f9","#94c7fa");
//	private List<String> sqljsel = Arrays.asList("#ffffff","#7fbfff","#e5f2ff","#c2e0ff");
//	private List<String> sqld = Arrays.asList("#ffcc85","#ee9017","#c7e0f9","#94c7fa");
//	private List<String> sqldsel = Arrays.asList("#ffddad","#ffa42e","#e5f2ff","#c2e0ff");
//	private List<String> edit = Arrays.asList("#ffffff","#ffa3d1","#ffe5f2","#ffc7e3");
//	private List<String> view = Arrays.asList("#ffffff","#8ccf8c","#d1ffd1","#8fff8f");
//	private List<String> formula = Arrays.asList("#fff5eb","#ffa061","#ffefad","#ffdc52");
//	private List<String> white = Arrays.asList("#f5f5f5","#f5f5f5","#f5f5f5","#f5f5f5");
	
	private List<String> sql = Arrays.asList("#e0f1f9","#63c7f9","#c7e9f9","#7cd0f9");
	private List<String> sqlsel = Arrays.asList("#ffffff","#7fd4ff","#e5f6ff","#99ddff");
	private List<String> sqlj = Arrays.asList("#e0edf9","#63aef9","#c7e0f9","#7cbbf9");
	private List<String> sqljsel = Arrays.asList("#ffffff","#7fbfff","#e5f2ff","#99ccff");
	private List<String> sqld = Arrays.asList("#ffcc85","#ee9017","#c7e0f9","#7cbbf9");
	private List<String> sqldsel = Arrays.asList("#ffddad","#ffa42e","#e5f2ff","#99ccff");
	private List<String> edit = Arrays.asList("#ffffff","#ff8ac4","#ffe5f2","#ff99cc");
	private List<String> view = Arrays.asList("#ffffff","#8ccf8c","#d1ffd1","#7fff7f");
	private List<String> formula = Arrays.asList("#fff5eb","#ffa061","#ffefad","#ffcf0f");
	private List<String> white = Arrays.asList("#f5f5f5","#f5f5f5","#f5f5f5","#f5f5f5");
	
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
		hm.put(ColorMode.WHITE, white);
	}
	
	public ArrayList<Color> getColors(ColorMode mode){
		ArrayList<Color> ret = new ArrayList<Color>();
		hm.get(mode).forEach(st -> {
			ret.add(Color.valueOf(st));
		});
		return ret;
	}
}

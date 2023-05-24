package rakhuba.pivot;

import javafx.scene.control.CustomMenuItem;
import rakhuba.clients.DataType;
import rakhuba.logic.Field;
import rakhuba.logic.NFormat;

public class FormatMenuItem extends CustomMenuItem {
	private NSelector nseletcor;
	private Field field;
	private NFormat nf;
	private  DataType tp;
	public FormatMenuItem(Field field, NFormat nf, DataType tp) {
		nseletcor = new NSelector(nf.getLabel());
		this.setContent(nseletcor.getLabel());
		this.setHideOnClick(false);
		this.field = field;
		this.nf = nf;
		this.tp = tp;
//		this.updateStyle();
	}

	public void updateStyle() {
		boolean bol = (field.getFormat().getId().equals(nf.getId())) && (field.getRowset_type().equals(tp.getName()));
		nseletcor.setValue(bol);
	}

	public NSelector getNseletcor() {
		return nseletcor;
	}

}

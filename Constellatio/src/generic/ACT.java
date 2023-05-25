package generic;


import java.util.ArrayList;

import activity.Edit;
import application.Nnode;
import file.NFile;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
//import rakhuba.generic.LAY;
import search.PAIR;

public abstract class ACT {
	public NFile nFile;
	protected LAY rootLay;
	public abstract void passLAY(LAY lay);
	public abstract void passNnode(Nnode nnode, MouseEvent e);	
	public abstract void newSearchFUNCTION(Nnode nnod, String col, PAIR funcVAL);
	public abstract void newSearchBETWEEN(Nnode nnod, String col, String from, String to);
	public abstract void newSearchIN(Nnode nnod, String col, String in, ArrayList<String> values);
	public abstract void rebuildFieldMenu();
	public abstract void closeActivity();
	
	public LAY getActiveLayer() {
		return rootLay;
	}
	public void passKeyEvent(KeyEvent e) {		
		if(this instanceof Edit) {
			((Edit)this).startLayDelete();
		}
	}
	
}
package rakhuba.managers;

import org.w3c.dom.Document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rakhuba.file.NFile;

public class UndoManager {
	private NFile nFile;
	private Document activeUndo;
	private ObservableList<Document> undos = FXCollections.observableArrayList();

	public UndoManager(NFile nFile) {
		this.nFile = nFile;
	}
	
	public void undo() {
		//select previous save from active undo //set active undo to variable
		if(activeUndo != null && undos.indexOf(activeUndo) > 0) {
			Document doc = undos.get(undos.indexOf(activeUndo) -1);
			activeUndo = doc;
			nFile.openUndoDocument(doc);
		}
	}
	
	public void redo() {
		if(activeUndo != null && undos.size() > 1 && undos.indexOf(activeUndo) < (undos.size() - 1)) {
			Document doc = undos.get(undos.indexOf(activeUndo) + 1);
			activeUndo = doc;
			nFile.openUndoDocument(doc);
		}
	}
	
	//SAVE UNDO
	public void saveUndoAction() {
		//NEED MORE WORK, something is wired here
		if(activeUndo != null && (undos.indexOf(activeUndo) + 1) < undos.size()) undos.remove((undos.indexOf(activeUndo) + 1), undos.size());
		//if active not last, remove extra undoes' //save new undo //set new active undo
		Document doc = nFile.createDocument();
		activeUndo = doc;
		undos.add(doc);
		if(undos.size() > 100) undos.remove(0);
	}
}

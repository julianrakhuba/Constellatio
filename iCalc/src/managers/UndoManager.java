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
package managers;

import org.w3c.dom.Document;

import file.NFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

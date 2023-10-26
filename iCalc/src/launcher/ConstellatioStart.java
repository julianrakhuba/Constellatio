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
package launcher;

import java.awt.Desktop;
import java.awt.Desktop.Action;

import application.Constellatio;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ConstellatioStart extends Application {
	private Constellatio constapp = new Constellatio(this);
	
    
	public ConstellatioStart() {
		super();
		if (Desktop.getDesktop().isSupported(Action.APP_OPEN_FILE)) {
			Desktop.getDesktop().setOpenFileHandler(e -> {
				e.getFiles().forEach(autoOpenFile -> {
					Platform.runLater(() -> {
						if (constapp.getDBManager() != null && constapp.getDBManager().getActiveConnection() != null) {
							constapp.getFilemanager().openFile(autoOpenFile);
						} else {
							constapp.getFilemanager().setAutoOpenFile(autoOpenFile);
						}
					});
				});
			});
		}
	}

	@Override
	public void start(@SuppressWarnings("exports") Stage stage)  {
		constapp.start(stage);		
	}

}

package launcher;

import java.awt.Desktop;
import java.awt.Desktop.Action;

import application.Constellatio;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Fx extends Application {
	private Constellatio constapp = new Constellatio(this);
	public Fx() {
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

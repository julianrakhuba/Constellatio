package managers;

import java.io.File;

import application.Constellatio;
import file.NFile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileManager {
	public Constellatio napp;
	private ObjectProperty<NFile> activeNFile = new SimpleObjectProperty<NFile>();
	private ObservableList<NFile> openFiles = FXCollections.observableArrayList();	
	private File autoOpenFile;
	public FileManager(Constellatio napp) {
		this.napp = napp;
		activeNFile.addListener((c,f,h) -> {
			if(activeNFile.getValue() != null) {
				activeNFile.getValue().setAppTitle();
				napp.getMenu().getSchemaMenu().editSchema.setDisable(false);			
			}else {
				napp.setTitle("");// when all files are closed
				napp.getMenu().getSchemaMenu().editSchema.setDisable(true);
			}
		});		
		openFiles.addListener((ListChangeListener<NFile>) c -> {
			napp.getMenu().disableMenus(c.getList().size() == 0);
		});//if more than one file 
	}
	
	public void createNewFile(String schemaName) {
		String db = napp.getDBManager().getActiveConnection().getLogin().getDb();
		File file = new File(System.getProperty("user.home") + "/documents/"+ db + "_"+ (openFiles.size() + 1) + ".con");
		NFile nfile = new NFile(file, this);
		nfile.setNewFile(true);
		nfile.createNewMap(schemaName);
		this.selectNFile(nfile);	
		openFiles.add(nfile);
		activeNFile.set(nfile);
		nfile.getUndoManager().saveUndoAction();
	}

	
	public void openFileChooser() {
		final FileChooser fileChooser = new FileChooser();		
		fileChooser.setTitle("Open");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Constelattio Files", "*.xml", "*.con"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/documents"));
		File file = fileChooser.showOpenDialog(napp.getStage());
		this.openFile(file);	
	}
	
	public void setAutoOpenFile (File file) {
		this.autoOpenFile = file;
	}
	
	public void openFile(File file) {
		if (file != null) {
			if(file.isFile()) {
				NFile nfile = new NFile(file, this);
				openFiles.add(nfile);	
				activeNFile.set(nfile);
				nfile.openFile();				
			}
		}	
	}

	public NFile getActiveNFile() {
		return activeNFile.get();
	}

	public void save() {
		if(activeNFile.get().isNewFile()) {
			this.saveAs();
		}else {			
			activeNFile.get().saveFile();
		}
	}

	public void saveAs() {
		final FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Save As");
    	
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Constelattio","*.con"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		
    	fileChooser.setInitialDirectory(activeNFile.get().getXMLFile().getParentFile());
    	fileChooser.setInitialFileName(activeNFile.get().getXMLFile().getName());   	
    	File zip = fileChooser.showSaveDialog(napp.getStage());
    	if(zip != null) {
    		activeNFile.get().setNewFile(false);
    		activeNFile.get().setZIP(zip);//SETS NEW FILE     		
        	activeNFile.get().saveFile();
        	activeNFile.getValue().setAppTitle();
    	}
	}
	
	public void closeActiveFile() {
		activeNFile.get().getActivity().closeActivity();
		activeNFile.get().infoPaneManager.close();
		napp.appBorderPane.setCenter(null);
		openFiles.remove(activeNFile.get());
		activeNFile.set(null);
		
		
		napp.getBottomBar().getBottomHideShowButtons().getChildren().clear();
		napp.getBottomBar().getSumLabel().clear();
		napp.getBottomBar().getCountLabel().clear();
		napp.getBottomBar().getRowsCount().clear();
		if(openFiles.size() > 0) this.selectNFile(openFiles.get(0));
	}

	public void closeAllFiles() {
		openFiles.clear();
		activeNFile.get().infoPaneManager.close();
		activeNFile.set(null);
		napp.appBorderPane.setCenter(null);
		napp.getBottomBar().getBottomHideShowButtons().getChildren().clear();
	}
	
	public ObservableList<NFile> getOpenFiles() {
		return openFiles;
	}
	
	public int size() {
		return openFiles.size();
	}
	
	public void selectNFile(NFile nfile) {
		
		napp.getBottomBar().getBottomHideShowButtons().getChildren().clear();
		activeNFile.set(nfile);
		nfile.ActivateFile();
		napp.getBottomBar().getBottomHideShowButtons().getChildren().add(nfile.tabManager.getButton());
		napp.getBottomBar().getBottomHideShowButtons().getChildren().add(nfile.getSidePaneManager().getButton());
	}

	public void setCompactView(boolean b) {
		openFiles.forEach(nf -> {
			nf.setCompactView(b);
		});
		if(activeNFile.get() != null) activeNFile.get().refreshTempFixForOffsetIssue();
	}

	public void openAutoFile() {
		if(this.autoOpenFile != null) {
			this.openFile(autoOpenFile);
			autoOpenFile = null;
		}
	}

}

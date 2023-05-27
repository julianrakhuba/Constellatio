package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class NMenu extends MenuBar {
	private ObservableList<MenuItem> disableMenus = FXCollections.observableArrayList();
	private SchemaMenu schemaMenu;
	private ViewMenu viewMenu;	
	private EditMenu editMenu;	
	private FileMenu fileMenu;
	
	public NMenu(Constellatio constellatio) {
		fileMenu = new FileMenu("File", constellatio);
		editMenu = new EditMenu("Edit", constellatio);	
		viewMenu = new ViewMenu("View", constellatio);
		schemaMenu = new SchemaMenu("Schema", constellatio);
		
		this.getMenus().addAll(fileMenu, editMenu, viewMenu, schemaMenu);
		this.setUseSystemMenuBar(true);

		disableMenus.addAll(schemaMenu.addschema, schemaMenu.removeschema, fileMenu.getExportMenuItem(), fileMenu.getCloseMenuItem(), fileMenu.getCloseAllMenuItem(), fileMenu.getSaveMenuItem(), fileMenu.getSaveAsMenuItem(), editMenu.getUndoMenuItem(), editMenu.getRedoMenuItem(), editMenu.getCopyMenuItem(), editMenu.getClearMenuItem(),
				viewMenu.getDynamicSearchMenuItem(), viewMenu.getAutoFoldMenuItem(), viewMenu.getInMenuItem(), viewMenu.getCenterMenuItem(), viewMenu.getOutMenuItem(), schemaMenu.editSchema);
		this.disableMenus(true);

	}
	
	public void disableMenus(boolean b) {
		disableMenus.forEach(m -> m.setDisable(b)); // HOW TO REFRESH APPLE MENU, JAVA ISSUE???
	}
	
	public SchemaMenu getSchemaMenu() {
		return schemaMenu;
	}

	public ViewMenu getViewMenu() {
		return viewMenu;
	}


	public FileMenu getFileMenu() {
		return fileMenu;
	}

}

package application;

import activity.Select;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import status.ConnectionStatus;

public class FileMenu extends Menu {
	private Constellatio constellatio;
	private Menu newMenu = new Menu("New");
	private MenuItem openMenuItem = new MenuItem("Open");
	private MenuItem closeMenuItem = new MenuItem("Close");
	private MenuItem closeAllMenuItem = new MenuItem("Close All");
	private MenuItem saveMenuItem = new MenuItem("Save");
	private MenuItem saveAsMenuItem = new MenuItem("Save As");
	private MenuItem exportMenuItem = new MenuItem("Export");
	private CheckMenuItem savePasswordMenuItem = new CheckMenuItem("Save Password");
	private MenuItem logoutMenuItem = new MenuItem("Logout");
	private MenuItem exitMenuItem = new MenuItem("Exit");
	
	
	public FileMenu(String string, Constellatio constellatio) {
		super (string);
		this.constellatio = constellatio;
		exitMenuItem.setOnAction(e -> {
			try {
				constellatio.getStartFX().stop();
				Platform.exit();
				System.exit(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		exportMenuItem.setOnAction(e -> {
			if (constellatio.getFilemanager().getActiveNFile().getActivity() instanceof Select
					&& constellatio.getFilemanager().getActiveNFile().getActivity().getActiveLayer() != null)
				constellatio.getFilemanager().getActiveNFile().getActivity().getActiveLayer().exportToCsv();
		});
		

		logoutMenuItem.setOnAction(e -> {
			constellatio.getDBManager().closeUserConnectionIfOpen();
			constellatio.getConnectionStage().show();// login screen
		});
		
		this.getItems().addAll(newMenu, openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem(), saveMenuItem, saveAsMenuItem, exportMenuItem,new SeparatorMenuItem(), savePasswordMenuItem, logoutMenuItem, new SeparatorMenuItem(), exitMenuItem);
		this.setOnShowing(e -> this.reconfigureFileMenu());// FILE MENU IS RECONFIGURED EVERY TIME IT OPENS
		openMenuItem.setDisable(true);
		newMenu.setDisable(true);
		logoutMenuItem.setDisable(true);
		
		savePasswordMenuItem.setSelected(true);
		openMenuItem.setOnAction((e) -> constellatio.getFilemanager().openFileChooser());
		openMenuItem.setAccelerator(constellatio.KeyCodeCombination(KeyCode.O));

		saveMenuItem.setOnAction((e) -> constellatio.getFilemanager().save());
		saveMenuItem.setAccelerator(constellatio.KeyCodeCombination(KeyCode.S));

		saveAsMenuItem.setOnAction((e) -> constellatio.getFilemanager().saveAs());
		closeMenuItem.setOnAction(e -> constellatio.getFilemanager().closeActiveFile());
		closeMenuItem.setAccelerator(constellatio.KeyCodeCombination(KeyCode.W));
		closeAllMenuItem.setOnAction(e -> constellatio.getFilemanager().closeAllFiles());
	}
	
	void reconfigureFileMenu() {
		this.getItems().clear();
		this.getItems().addAll(newMenu, openMenuItem, closeMenuItem, closeAllMenuItem, new SeparatorMenuItem());
		if (constellatio.light.getStatus() == ConnectionStatus.CONNECTED) {
			constellatio.getFilemanager().getOpenFiles().forEach(nfile -> {
				CheckMenuItem menuItem = new CheckMenuItem(nfile.getXMLFile().getName());
				menuItem.setSelected(nfile == constellatio.getFilemanager().getActiveNFile());
				menuItem.setOnAction(mie -> constellatio.getFilemanager().selectNFile(nfile));
				this.getItems().add(menuItem);
			});
		}
		this.getItems().addAll(new SeparatorMenuItem(), saveMenuItem, saveAsMenuItem, exportMenuItem, new SeparatorMenuItem(), savePasswordMenuItem,
				logoutMenuItem, new SeparatorMenuItem(), exitMenuItem);
	}
	
	public void addNewSchemaToMenu(String schemaName) {
		MenuItem schemaMenuItem = new MenuItem(schemaName);
		schemaMenuItem.setOnAction((e) -> {
			constellatio.getFilemanager().createNewFile(schemaName);
		});
		if (schemaName.equalsIgnoreCase("sakila")) {
			schemaMenuItem.setAccelerator(constellatio.KeyCodeCombination(KeyCode.N));
		}
		newMenu.getItems().add(schemaMenuItem);
	}

	public MenuItem getCloseMenuItem() {
		return closeMenuItem;
	}

	public MenuItem getCloseAllMenuItem() {
		return closeAllMenuItem;
	}

	public MenuItem getSaveMenuItem() {
		return saveMenuItem;
	}

	public MenuItem getSaveAsMenuItem() {
		return saveAsMenuItem;
	}

	public MenuItem getExportMenuItem() {
		return exportMenuItem;
	}

	public CheckMenuItem getSavePasswordMenuItem() {
		return savePasswordMenuItem;
	}

	public Menu getNewMenu() {
		return newMenu;
	}

	public void activateConnectionMenus() {
		openMenuItem.setDisable(false);
		newMenu.setDisable(false);
		logoutMenuItem.setDisable(false);
		savePasswordMenuItem.setDisable(true);
		if(constellatio.getFilemanager().size()>0) constellatio.getMenu().disableMenus(false);
		
	}

	public void deactivateConnectionMenus() {
		openMenuItem.setDisable(true);
		newMenu.setDisable(true);
		logoutMenuItem.setDisable(true);
		savePasswordMenuItem.setDisable(false);
		constellatio.getMenu().disableMenus(true);	
	}
}

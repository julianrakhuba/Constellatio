package application;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import status.ActivityMode;

public class SchemaMenu extends Menu {
	public Menu addschema = new Menu("Add");
	public Menu removeschema = new Menu("Delete");
	public CheckMenuItem editSchema = new CheckMenuItem("Edit Schema");
	public MenuItem saveSchema = new MenuItem("Save Schema");
	
	public SchemaMenu(String string, Constellatio constellatio) {
		super (string);
		this.getItems().addAll(addschema, removeschema, new SeparatorMenuItem(), editSchema, saveSchema);
		this.setOnAction(e -> {
			if (editSchema.isSelected()) {
				constellatio.getFilemanager().getActiveNFile().setActivityMode(ActivityMode.CONFIGURE);
			} else {
				constellatio.getFilemanager().getActiveNFile().getActivity().closeActivity();
				constellatio.getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
			}
		});
		saveSchema.setOnAction(e -> {
			constellatio.getFilemanager().getActiveNFile().getActiveNmap().saveNnodeCoordinates();
			constellatio.getDBManager().getActiveConnection().getXMLBase().save_existing_or_crate_new();
			constellatio.getFilemanager().getActiveNFile().getActivity().closeActivity();
			constellatio.getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
			editSchema.setSelected(false);
		});
		
		
		this.setOnShowing(meneu_event -> {
			//••••••••••••••••••••
			this.getItems().clear();
			this.getItems().addAll(new SeparatorMenuItem());
			if (constellatio.getFilemanager().getActiveNFile() != null) {
				constellatio.getFilemanager().getActiveNFile().getMaps().forEach((name, map) -> {
					CheckMenuItem mapMenuItem = new CheckMenuItem(name);
					if (map == constellatio.getFilemanager().getActiveNFile().getActiveNmap())
						mapMenuItem.setSelected(true);
					mapMenuItem.setSelected(map == constellatio.getFilemanager().getActiveNFile().getActiveNmap());
					mapMenuItem.setOnAction(e -> {
						constellatio.getFilemanager().getActiveNFile().activateNmap(name);
					});
					this.getItems().add(mapMenuItem);
				});
			}
			addschema.getItems().clear();

			constellatio.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema -> {
				if (constellatio.getFilemanager().getActiveNFile() != null && !constellatio.getFilemanager().getActiveNFile().getMaps().containsKey(schema)) {
					MenuItem menuItem = new MenuItem(schema);
					menuItem.setOnAction(e -> {
						constellatio.getFilemanager().getActiveNFile().createNewMap(schema);
					});
					addschema.getItems().add(menuItem);
				}
			});
			removeschema.getItems().clear();
			constellatio.getDBManager().getActiveConnection().getXMLBase().getSchemas().forEach(schema -> {
				if (constellatio.getFilemanager().getActiveNFile() != null && constellatio.getFilemanager().getActiveNFile().getMaps().size() > 1
						&& constellatio.getFilemanager().getActiveNFile().getMaps().containsKey(schema)) {
					MenuItem menuItem = new MenuItem(schema);
					menuItem.setOnAction(e -> {
						constellatio.getFilemanager().getActiveNFile().removeSchema(schema);
					});
					removeschema.getItems().add(menuItem);
				}
			});
			this.getItems().addAll(new SeparatorMenuItem(), addschema, removeschema, new SeparatorMenuItem(), editSchema, saveSchema);			
		});

	}
}
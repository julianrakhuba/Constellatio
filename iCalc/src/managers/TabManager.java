package managers;
import java.util.ArrayList;

import application.NMap;
import file.NFile;
import file.NSheet;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import status.VisualStatus;

public class TabManager extends TabPane {
	private NFile nfile;
	private Property<VisualStatus> status = new SimpleObjectProperty<VisualStatus>(VisualStatus.UNAVALIBLE);
	private StackPane testPane;

	public TabManager(NFile nfile) {
		this.nfile = nfile;
		this.setSide(Side.TOP);
		this.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
		this.setPrefHeight(250);			
		
		this.getSelectionModel().selectedItemProperty().addListener((a,b,c)->{
			if(c instanceof NSheet) {
				NSheet nsheet = (NSheet)c;
				nfile.getFileManager().napp.getBottomBar().getRowsCount().setCountValue(nsheet.getLay().getItems().size());
			}else {
				nfile.getFileManager().napp.getBottomBar().getRowsCount().clear();
			}
		});	
		testPane = new StackPane(this);

		if(nfile.getFileManager().napp.getStage().getStyle() == StageStyle.TRANSPARENT) {
			testPane.setStyle(" -fx-padding: 0 5 0 0; -fx-background-color: transparent;");		
			this.setStyle("-fx-border-width: 0; -fx-border-color: transparent; -fx-padding: 0 0 0 5; -fx-background-color: transparent; -fx-background-radius: 3; -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 10, 0.0 , 0, 0);");

		}else {
			testPane.setStyle(" -fx-padding: 0; -fx-background-color: transparent;");		
			this.setStyle("-fx-border-width: 0; -fx-border-color: transparent; -fx-padding: 5; -fx-background-color: transparent; -fx-background-radius: 3; -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 10, 0.0 , 0, 0);");

		}

			
		
	}
	
	public void buttonClick() {
		if(status.getValue() == VisualStatus.SHOW) {
			status.setValue(VisualStatus.HIDE);
			this.hideGrid();
		}else if(status.getValue() == VisualStatus.HIDE){
			this.showGrid();
		}
	}

	
	public void selectTab(Tab tab) {
		if(!this.getTabs().contains(tab)) {
			this.getTabs().add(tab);
		}
		Tab currentTab = this.getSelectionModel().getSelectedItem();
		if(currentTab == null || currentTab instanceof NSheet) {
			this.getSelectionModel().select(tab);
			
//			if(currentTab instanceof NSheet) {
//				((NSheet) currentTab).makeAvaliableIfValid();
//			}
			
		}
	}
	
	

	public void removeTab(Tab tab) {
		this.getTabs().remove(tab);
		if(this.getTabs().size() == 0)  {
			status.setValue(VisualStatus.UNAVALIBLE);
			this.hideGrid();		
		}
	}
	
	public void removeNSheetFor(NMap nmap) {
		ArrayList<Tab> panesToRemove = new ArrayList<Tab>();
		this.getTabs().forEach(tab -> {
			if(((NSheet)tab).getLay().nnode.nmap == nmap) panesToRemove.add(tab);
		});
		panesToRemove.forEach(sheet -> this.removeTab(sheet));
	}
	
	//MOVE TAB SPLIT TO FILE
	public void showGrid() {
		status.setValue(VisualStatus.SHOW);	
//		nfile.getQuadSplit().setBottomRight(testPane);
		nfile.getQuadSplit().setBottomLeft(testPane);

	}
	
	private void hideGrid() {
//		nfile.getQuadSplit().setBottomRight(null);
		nfile.getQuadSplit().setBottomLeft(null);

	}

	public void clear() {
		this.getTabs().clear();
		status.setValue(VisualStatus.UNAVALIBLE);
		this.hideGrid();
	}
	
	//used to create document for file or undo
	public VisualStatus getStatus() {
		return status.getValue();
	}
	
	//used in open file and undo
	public void setStatus(VisualStatus vs) {
		this.status.setValue(vs);
	}
}

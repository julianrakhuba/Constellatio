package managers;
import java.util.ArrayList;

import application.NMap;
import file.NFile;
import generic.NSheet;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import status.VisualStatus;

public class TabManager {
	private TabPane tabPane = new TabPane();
	private NFile nfile;
	private Property<VisualStatus> status = new SimpleObjectProperty<VisualStatus>(VisualStatus.UNAVALIBLE);
	private Pane button = new Pane();

	public TabManager(NFile nfile) {
		this.nfile = nfile;
		tabPane.setSide(Side.TOP);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
		tabPane.setPrefHeight(250);
		
//		tabPane.getStylesheets().add(getClass().getResource("/tab-pane.css").toExternalForm());

		button.getStyleClass().add("gridLightGray");
		
		status.addListener((a,b,c) -> {//listener for style only
			button.getStyleClass().clear();
			if(c == VisualStatus.SHOW) { button.getStyleClass().add("gridLightBlue");
			}else if(c == VisualStatus.HIDE)  { button.getStyleClass().add("gridLightOrange");
			}else if(c == VisualStatus.UNAVALIBLE)  { button.getStyleClass().add("gridLightGray"); }
		});
		
	
		
		
		button.setOnMouseClicked(e -> {
			if(status.getValue() == VisualStatus.SHOW) {
				status.setValue(VisualStatus.HIDE);
				this.hideGrid();
			}else if(status.getValue() == VisualStatus.HIDE){
				this.showGrid();
			}
		});
		
//		tabPane.setStyle("-fx-open-tab-animation: NONE; -fx-close-tab-animation: NONE;");
		
		
		tabPane.getSelectionModel().selectedItemProperty().addListener((a,b,c)->{
			if(c instanceof NSheet) {
				NSheet nsheet = (NSheet)c;
				nfile.getFileManager().napp.getBottomBar().getRowsCount().setCountValue(nsheet.getLay().getItems().size());
			}else {
				nfile.getFileManager().napp.getBottomBar().getRowsCount().clear();
			}
		});
		
//		tabPane.setOnZoom( zz ->{
//			tabPane.setPrefHeight(tabPane.getHeight() * zz.getZoomFactor());
////			tabPane.setScaleX(tabPane.getScaleX() * zz.getZoomFactor());
////			tabPane.setScaleY(tabPane.getScaleY() * zz.getZoomFactor());
//		 });		
	}
	
	public void selectTab(Tab tab) {
		if(!tabPane.getTabs().contains(tab)) {
			tabPane.getTabs().add(tab);
		}
		Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
		if(currentTab == null || currentTab instanceof NSheet) {
			tabPane.getSelectionModel().select(tab);
		}
	}

	public void removeTab(Tab tab) {
		tabPane.getTabs().remove(tab);
		if(tabPane.getTabs().size() == 0)  {
			status.setValue(VisualStatus.UNAVALIBLE);
			this.hideGrid();		
		}
	}
	
	public void removeNSheetFor(NMap nmap) {
		ArrayList<Tab> panesToRemove = new ArrayList<Tab>();
		tabPane.getTabs().forEach(tab -> {
			if(((NSheet)tab).getLay().nnode.nmap == nmap) panesToRemove.add(tab);
		});
		panesToRemove.forEach(sheet -> this.removeTab(sheet));
	}
	
	//MOVE TAB SPLIT TO FILE
	public void showGrid() {
		status.setValue(VisualStatus.SHOW);	
		nfile.showLowerPane(tabPane);
	}
	
	private void hideGrid() {
		nfile.hideTabPane(tabPane);
	}

	public void clear() {
		tabPane.getTabs().clear();
		status.setValue(VisualStatus.UNAVALIBLE);
		this.hideGrid();
	}
		
	public Pane getButton() {
		return button;
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

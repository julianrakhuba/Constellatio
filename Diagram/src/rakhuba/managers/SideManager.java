package rakhuba.managers;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rakhuba.file.NFile;
import rakhuba.generic.LAY;
import rakhuba.sidePanel.InfoStage;
import rakhuba.status.VisualStatus;

public class SideManager {
	private InfoStage infoStage;
	private ScrollPane scrollPane = new ScrollPane();
	
	private NFile nfile;
	private Pane showHideButton = new Pane();
	private Property<VisualStatus> status = new SimpleObjectProperty<VisualStatus>(VisualStatus.SHOW);
	private boolean useStage = false;
	private VBox searchSideVBox = new VBox();

	public SideManager(NFile nfile) {
		this.nfile = nfile;
		searchSideVBox.getChildren().addAll(nfile.getMessagesRegion());
		scrollPane.setContent(searchSideVBox);
		scrollPane.setMaxWidth(250);
		scrollPane.setMinWidth(250);
		
//		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
//		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		searchSideVBox.setStyle("-fx-background-color: #f5f5f5;  -fx-padding: 10,5,10,10; -fx-spacing: 12;");	
		scrollPane.setContent(searchSideVBox);
		
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

		infoStage = nfile.getFileManager().napp.infoStage;
		infoStage.setOnCloseRequest(e ->{
    		this.setStatus(VisualStatus.HIDE);
    		this.hideSidePane();
    	});
		
		//•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		showHideButton.getStyleClass().add("seachLightBlue");
		status.addListener((a,b,c) -> {
			showHideButton.getStyleClass().clear();
			if(c == VisualStatus.SHOW) {
				showHideButton.getStyleClass().add("seachLightBlue");
			}else if(c == VisualStatus.HIDE) {
				showHideButton.getStyleClass().add("seachLightOrange");
			}
		});
		
		showHideButton.setOnMouseClicked(e -> {
			if(status.getValue() == VisualStatus.SHOW) {
				status.setValue(VisualStatus.HIDE);
				this.hideSidePane();
			}else if(status.getValue() == VisualStatus.HIDE){
				status.setValue(VisualStatus.SHOW);
				this.showSidePane();
			}
		});	
	}
	
	public void close() {
		infoStage.close(); 
	}

	public void activateSearch(LAY lay) {
		this.showSidePaneIfNotHidden();
		lay.updateRowCount();
		searchSideVBox.getChildren().clear();
//		searchSideVBox.getChildren().add(new HeaderLabel(lay.nnode.getTable(),"#ade0ff"));
		searchSideVBox.getChildren().addAll(lay.getFormulaRegion());
		searchSideVBox.getChildren().addAll(lay.getSearchRegion());
		
		if(lay.doShowGroupOptions()) {
			searchSideVBox.getChildren().addAll(lay.getOptionsRegion());
		}	
	}
	
	public void activateFormula(LAY lay) {
		this.showSidePaneIfNotHidden();
		lay.updateRowCount();
		searchSideVBox.getChildren().clear();
//		searchSideVBox.getChildren().add(new HeaderLabel(lay.nnode.getTable(),"orange"));
		searchSideVBox.getChildren().addAll(lay.getFormulaRegion());
		searchSideVBox.getChildren().addAll(lay.getSearchRegion());
		if(lay.doShowGroupOptions()) {
			searchSideVBox.getChildren().addAll(lay.getOptionsRegion());
		}
	}

	
	public void deactivate() {
		searchSideVBox.getChildren().clear();
		searchSideVBox.getChildren().addAll(nfile.getMessagesRegion());
	}
		
	public void activateErrorTab() {
//		scrollPane.setContent(nfile.getMessagesSideVBox());
	}
	
	//MOVE SEARHSPLIT TO FILE ••••••••••••••••••••••••••••••••••••••••••••••••••••
	public void showSidePaneIfNotHidden() {
		if(status.getValue() != VisualStatus.HIDE) {
			status.setValue(VisualStatus.SHOW);
			this.showSidePane();
		}
	}

	public void showSidePane() {
		if(useStage) {
			if(!infoStage.getRootPane().getChildren().contains(scrollPane)) {
				infoStage.getRootPane().getChildren().clear();
				infoStage.getRootPane().getChildren().add(scrollPane);
			}
			infoStage.show();
		}else {
			nfile.getFileBorderPane().setRight(scrollPane);
		}
	}
	
	public void hideSidePane() {
		if(useStage) {
			infoStage.hide();
			infoStage.getRootPane().getChildren().clear();
		}else {
			nfile.getFileBorderPane().setRight(null);
		}
	}
	
	public Pane getButton() {
		return showHideButton;
	}
	
	public VisualStatus getStatus() {
		return status.getValue();
	}
	
	public void setStatus(VisualStatus vs) {
		this.status.setValue(vs);
	}
}

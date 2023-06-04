package managers;

import file.NFile;
import generic.LAY;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sidePanel.InfoStage;
import status.VisualStatus;

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
		searchSideVBox.setStyle("-fx-background-color: rgba(255,255,255, 1);  -fx-padding: 10,5,10,10; -fx-spacing: 12;  -fx-effect: dropshadow(two-pass-box , rgba(0, 0, 0, 0.3), 10, 0.0 , 0, 0);-fx-background-radius: 7;");	
		
		StackPane.setMargin(searchSideVBox, new Insets(5));

		scrollPane.setContent(new StackPane (searchSideVBox));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        
//        scrollPane.setPadding(new Insets(10));
		scrollPane.setStyle("-fx-background-color: transparent;");


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
		searchSideVBox.getChildren().addAll(lay.getSearchRegion());
		
		if(lay.doShowGroupOptions()) {
			searchSideVBox.getChildren().addAll(lay.getOptionsRegion());
		}	
	}
	
	public void activateFormula(LAY lay) {
		this.showSidePaneIfNotHidden();
		lay.updateRowCount();
		searchSideVBox.getChildren().clear();
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
//			nfile.getFileBorderPane().setRight(scrollPane);
			
			nfile.showSideManager(scrollPane);
		}
	}
	
	public void hideSidePane() {
		if(useStage) {
			infoStage.hide();
			infoStage.getRootPane().getChildren().clear();
		}else {
//			nfile.getFileBorderPane().setRight(null);
			nfile.hideSideManager();
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

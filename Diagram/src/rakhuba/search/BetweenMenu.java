package rakhuba.search;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rakhuba.application.Constellatio;
import rakhuba.application.NScene;

public class BetweenMenu extends Stage {
	private HBox hbox = new HBox();
	private Constellatio napp;
	private NScene nscene;
	
	public BetweenMenu(Constellatio napp, String table, String column, ArrayList<String> distinctValues) {
		initOwner(napp.getStage());
		initStyle(StageStyle.TRANSPARENT);
		initModality(Modality.WINDOW_MODAL);
		this.napp = napp;
		nscene = new NScene(hbox, napp);
		nscene.setFill(Color.TRANSPARENT);
		this.setScene(nscene);
		this.setTitle("" + column);
		hbox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.85); -fx-background-radius: 2 2 2 2;");
		hbox.setPadding(new Insets(10, 10 , 10 ,10));
		hbox.setSpacing(15);
		

		this.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {if (!isNowFocused) {this.hide();} });
		this.setAlwaysOnTop(true);
		
		ListView<String> listViewA = new ListView<String>();
		ListView<String> listViewB = new ListView<String>();
		
		HBox.setHgrow(listViewA, Priority.ALWAYS);
		HBox.setHgrow(listViewB, Priority.ALWAYS);

		distinctValues.forEach(str -> {
			listViewA.getItems().add(str);
			listViewB.getItems().add(str);
			
		});

		listViewA.setOnKeyPressed(e -> {
			if (e.getCode().toString().equals("ENTER")) {
				if(listViewB.getSelectionModel().getSelectedItems().size() == 1) {
					this.prepareSearch(table, column, listViewA.getSelectionModel().getSelectedItem(), listViewB.getSelectionModel().getSelectedItem());
				}
			}
		});
		
		listViewB.setOnKeyPressed(e -> {
			if (e.getCode().toString().equals("ENTER")) {
				if(listViewA.getSelectionModel().getSelectedItems().size() == 1) {
					this.prepareSearch(table, column, listViewA.getSelectionModel().getSelectedItem(), listViewB.getSelectionModel().getSelectedItem());
				}
			}
		});

		this.hbox.getChildren().addAll(listViewA, listViewB);
		Point2D pt = napp.search.localToScreen(0, napp.search.getHeight() + 1);
		this.setX(pt.getX() + 15);
		this.setY(pt.getY());
		this.setMaxHeight(200);
		this.setWidth(napp.search.getWidth() - 30);
		this.show();
	}
	
	private void prepareSearch(String table, String column, String fromValue, String toValue) {
		String from;
		String to;
		try {
			if(Double.valueOf(toValue).compareTo(Double.valueOf(fromValue)) >= 0) {
					from = fromValue;
					to = toValue;
				}else {
					from = toValue;
					to = fromValue;
				}
			
		} catch (NumberFormatException e){
			from = fromValue;
			to = toValue;
		}

		napp.filemanager.getActiveNFile().getActivity().newSearchBETWEEN(napp.filemanager.getActiveNFile().getActiveNmap().getNnode(table), column, from, to);		
		this.hide();
		napp.search.clear();
		napp.search.requestFocus();
	}
	
}
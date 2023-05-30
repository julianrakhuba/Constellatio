package application;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import search.SearchTextField;
import status.ActivityMode;

public class UpperPane extends StackPane {
	private Constellatio constellatio;
	
	private HBox overlapBox = new HBox(-15);
	private FunctionsButton functionsButton;
	private ContextMenu searchContext;
	private SearchTextField searchTextField;
	private Pane placeHolder;
	
	public UpperPane(Constellatio constellatio) {
		this.getStyleClass().add("newSearchBar");
		this.constellatio = constellatio;
		functionsButton = new FunctionsButton("", constellatio);
		searchContext = new ContextMenu();
		searchTextField = new SearchTextField(constellatio, this);
		placeHolder = new Pane(searchTextField);
		
		overlapBox.getChildren().addAll(functionsButton, placeHolder);

		// •••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
		searchContext.setOnHiding(e -> {
			searchContext.getItems().clear();
			if (constellatio.getFilemanager().getActiveNFile().getActivityMode() == ActivityMode.VIEW) {
				constellatio.getFilemanager().getActiveNFile().getActivity().closeActivity();
				constellatio.getFilemanager().getActiveNFile().setActivityMode(ActivityMode.SELECT);
				constellatio.getFilemanager().getActiveNFile().infoPaneManager.deactivate();
			}
		});

		functionsButton.setMinHeight(30);
		functionsButton.setMaxHeight(30);
		functionsButton.setMinWidth(40);
		functionsButton.setMaxWidth(40);
		
		overlapBox.setAlignment(Pos.CENTER);
		this.getChildren().add(overlapBox);
	}
	
	public HBox getOverlapBox() {
		return overlapBox;
	}

	public FunctionsButton getFunctionsButton() {
		return functionsButton;
	}

	public ContextMenu getSearchContext() {
		return searchContext;
	}

	public Pane getPlaceHolder() {
		return placeHolder;
	}
	
	public void setFormulaSearch(Node formulaHBox) {
		placeHolder.getChildren().clear();
		placeHolder.getChildren().add(formulaHBox);
	}

	// test field
	public void setRegularSearch() {
		placeHolder.getChildren().clear();
		placeHolder.getChildren().add(searchTextField);
	}
	
	public void funcMenuClick(Node anchor) {
		if (!searchContext.isShowing()) {
			if (constellatio.getFilemanager().getActiveNFile() != null) {
				constellatio.getFilemanager().getActiveNFile().getActivity().rebuildFieldMenu();
			}
			if (!searchContext.isShowing() && searchContext.getItems().size() > 0) {
				if (anchor == null) {
					searchContext.show(placeHolder, Side.BOTTOM, 15, 2);
				} else {
					searchContext.show(anchor, Side.BOTTOM, 15, 2);
				}
			}
		} else {
			searchContext.hide();
		}
	}

	public SearchTextField getSearchTextField() {
		return searchTextField;
	}


}

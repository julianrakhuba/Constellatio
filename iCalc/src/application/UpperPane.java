package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import search.Search;
import status.ActivityMode;


public class UpperPane extends StackPane {
	private Constellatio constellatio;
	
	private HBox overlapBox = new HBox(-15);
	private FunctionsButton functionsButton;
	private ContextMenu searchContext;
	private Search searchTextField;
	private Pane placeHolder;
	
	public UpperPane(Constellatio constellatio) {
		this.getStyleClass().add("newSearchBar");
		this.constellatio = constellatio;
		functionsButton = new FunctionsButton("", constellatio);
		searchContext = new ContextMenu();
		searchTextField = new Search(constellatio, this);
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
		
		searchContext.setOnShowing(e ->{
			searchContext.setOpacity(0);
			KeyFrame kf1 = new KeyFrame(Duration.millis(200), new KeyValue(searchContext.opacityProperty(), 1));
		    Timeline timeline = new Timeline(kf1);
		    timeline.setCycleCount(1);
		    timeline.play();
		});
		

		functionsButton.setMinHeight(30);
		functionsButton.setMaxHeight(30);
		functionsButton.setMinWidth(40);
		functionsButton.setMaxWidth(40);
		
		overlapBox.setAlignment(Pos.CENTER);
		this.getChildren().add(overlapBox);
		
//		this.setEffect(new Reflection(5,0.1,0.2, 0.0));
//		this.setEffect(new Lighting());

		
//		double topOffset, double fraction,
//        double topOpacity, double bottomOpacity
        
//		this.setOpacity(0.0);
		
//		this.setOnMouseEntered(e -> {
////			this.setOpacity(1);
//			FadeTransition ft = new FadeTransition(Duration.millis(300), this);
//			ft.fromValueProperty().bind(this.opacityProperty());
//			ft.setToValue(1);
//			ft.play();
//		});
//		this.setOnMouseExited(e -> {
//			FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
//			ft.fromValueProperty().bind(this.opacityProperty());
//			ft.setToValue(0.0);
//			ft.play();
////			this.setOpacity(0.1);
//		});

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

	public Search getSearchTextField() {
		return searchTextField;
	}


}

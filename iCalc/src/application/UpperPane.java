package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	private ScrollPane scrollpane = new ScrollPane();
	private boolean usescroll = true;
	
	private Pane holderPaen = new Pane();

	
	public UpperPane(Constellatio constellatio) {
		this.getStyleClass().add("newSearchBar");
//		this.setStyle("-fx-padding: 10;");
		
		this.constellatio = constellatio;
		functionsButton = new FunctionsButton("", constellatio);
		searchContext = new ContextMenu();
		
		if (constellatio.getMenu().getViewMenu().getGlassModeMenuItem().isSelected()) {
			searchContext.setSkin(new CustomContextMenuSkin(searchContext));
		}
		searchTextField = new Search(constellatio, this);
		searchTextField.minWidthProperty().bind(this.widthProperty().divide(1.7225));
		
//		bind text search to scroll width and little shorter		
		scrollpane.minWidthProperty().bind(this.widthProperty().divide(1.7));
		scrollpane.maxWidthProperty().bind(this.widthProperty().divide(1.7));
		
		scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollpane.setVbarPolicy(ScrollBarPolicy.NEVER);
		String focusedCursorBox = "-fx-padding: 0 5 0 5; -fx-effect: innershadow(three-pass-box, #99ddff, 4, 0.5, 0, 0); -fx-background-color: white; -fx-text-fill: #9DA1A1; -fx-border-width: 0 ; -fx-background-radius: 15 15 15 15;  -fx-border-radius: 15 15 15 15;" ;
		scrollpane.setStyle(focusedCursorBox);
		scrollpane.setMaxHeight(30);
		scrollpane.minViewportHeightProperty().bind(searchTextField.heightProperty().add(0));
		scrollpane.setOnMouseClicked(e ->{
			System.out.println("scroll height: " + scrollpane.getHeight()+" padding: " + scrollpane.getPadding() +" insets: "+ scrollpane.getInsets());
		});
//		overlapBox.setStyle("-fx-background-color: transparent");
		
		overlapBox.getChildren().addAll(functionsButton, holderPaen);
		overlapBox.maxWidthProperty().bind(this.widthProperty().divide(1.8));
		this.setRegularSearch();

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
	}

	public FunctionsButton getFunctionsButton() {
		return functionsButton;
	}

	public ContextMenu getSearchContext() {
		return searchContext;
	}

	/**
	 * @return Pane for PopUp Stage anchor
	 */
	public Pane getPlaceHolder() {
		return overlapBox;
	}
	
	public void setFormulaSearch(Pane formulaHBox) {
		overlapBox.getChildren().clear();
//		overlapBox.getChildren().removeIf(j -> !(j instanceof FunctionsButton));
//		overlapBox.getChildren().add(formulaHBox);
		
		if(usescroll) {
			scrollpane.setContent(formulaHBox);
			overlapBox.getChildren().addAll(functionsButton, scrollpane);
			formulaHBox.minWidthProperty().bind(this.widthProperty().divide(1.7225));
		}else {
			overlapBox.getChildren().addAll(functionsButton, formulaHBox);
			formulaHBox.minWidthProperty().bind(this.widthProperty().divide(1.7225));
		}
	}

	public void setRegularSearch() {
		overlapBox.getChildren().clear();
//		overlapBox.getChildren().removeIf(j -> !(j instanceof FunctionsButton));
//		boolean usescroll = true;
		if(usescroll) {
			scrollpane.setContent(searchTextField);
			overlapBox.getChildren().addAll(functionsButton, scrollpane);
			
			searchTextField.setEffect(null);
		}else {
			overlapBox.getChildren().addAll(functionsButton, searchTextField);
		}
		
		//use scroll pane
		

	}
	
	public void funcMenuClick(Node anchor) {
		if (!searchContext.isShowing()) {
			if (constellatio.getFilemanager().getActiveNFile() != null) {
				constellatio.getFilemanager().getActiveNFile().getActivity().rebuildFieldMenu();
			}
			if (!searchContext.isShowing() && searchContext.getItems().size() > 0) {
				if (anchor == null) {
					searchContext.show(overlapBox, Side.BOTTOM, 40, 2);
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

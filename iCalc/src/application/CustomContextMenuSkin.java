package application;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.skin.ContextMenuSkin;

public  class CustomContextMenuSkin extends ContextMenuSkin {
    public CustomContextMenuSkin(ContextMenu contextMenu) {
        super(contextMenu);
        // Set the background color of the context menu
//        getSkinnable().setStyle("-fx-effect: dropshadow(gaussian, derive(#1E90FF, 20%) , 8, 0.1, 0.0, 0.0); -fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 3; -fx-border-radius: 3;");	        
     // Set the background color of the context menu -fx-effect: dropshadow(gaussian, derive(#1E90FF, -30%) , 8, 0.2, 0.0, 0.0);
        getSkinnable().setStyle(" -fx-background-color: rgba(0, 0, 0, 0.7); "
        		+ "-fx-border-width: 0.5;"
        		+ "-fx-border-color: derive(#1E90FF, 50%);"
        		+ "-fx-effect: dropshadow(gaussian, derive(#1E90FF, 40%) , 8, 0.2, 0.0, 0.0);"
        		+ "-fx-background-radius: 5;"
        		+ "-fx-border-radius: 5;");
    }
}
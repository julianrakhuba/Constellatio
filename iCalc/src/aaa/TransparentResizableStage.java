/*******************************************************************************
 *   Copyright (c) 2023 Constellatio
 *  
 *   This software is released under the [Educational/Non-Commercial License or Commercial License, choose one]
 *  
 *   Educational/Non-Commercial License (GPL):
 *  
 *   Permission is hereby granted, free of charge, to any person or organization
 *   obtaining a copy of this software and associated documentation files (the
 *   "Software"), to deal in the Software without restriction, including without
 *   limitation the rights to use, copy, modify, merge, publish, distribute,
 *   sublicense, and/or sell copies of the Software, and to permit persons to
 *   whom the Software is furnished to do so, subject to the following conditions:
 *  
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *  
 *   THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 *  
 *   Commercial License:
 *  
 *   You must obtain a separate commercial license if you
 *   wish to use this software for commercial purposes. Please contact
 *   rakhuba@gmail.com for licensing information.
 *  
 *  
 *******************************************************************************/
package aaa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("unused")
public class TransparentResizableStage extends Application {

    private static final int RESIZE_MARGIN = 5;

    private double xOffset = 0;
    private double yOffset = 0;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage primaryStage) {
        // Create a transparent stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        // Create a resizable region as the root node
        Region root = new Region();
        root.setStyle("-fx-background-color: rgba(0,0,0,0.5);");

        // Create the scene with a transparent fill
        Scene scene = new Scene(root, Color.TRANSPARENT);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Enable resizing by dragging the edges
        enableResize(primaryStage, root);

        // Show the stage
        primaryStage.show();
    }

    private void enableResize(Stage stage, Region region) {
        region.setOnMousePressed(event -> {
            xOffset = event.getScreenX() - stage.getX();
            yOffset = event.getScreenY() - stage.getY();
        });

        region.setOnMouseDragged(event -> {
//            if (event.getX() >= region.getWidth() - RESIZE_MARGIN && event.getY() >= region.getHeight() - RESIZE_MARGIN) {
                stage.setWidth(event.getScreenX() - stage.getX());
                stage.setHeight(event.getScreenY() - stage.getY());
//            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

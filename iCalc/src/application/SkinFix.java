/*******************************************************************************
 *  *  MIT License
 *  *
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING
 *  * FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package application;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.skin.ContextMenuSkin;

public  class SkinFix extends ContextMenuSkin {
    public SkinFix(ContextMenu contextMenu) {
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
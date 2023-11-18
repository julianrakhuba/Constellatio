/*******************************************************************************
 * Copyright (c) 2023 Constellatio
 *  
 * This software is released under the [Educational/Non-Commercial License or Commercial License, choose one]
 *  
 * Educational/Non-Commercial License (GPL):
 *  
 * Permission is hereby granted, free of charge, to any person or organization
 * obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute,
 * sub-license, and/or sell copies of the Software, and to permit persons to
 * whom the Software is furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS," WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *  
 * Commercial License:
 *  
 * You must obtain a separate commercial license if you
 * wish to use this software for commercial purposes. Please contact Julian Rakhuba at 916-390-9979 or email
 * rakhuba@gmail.com for licensing information.
 *******************************************************************************/
package elements;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Cursor extends Pane {
	private FadeTransition pulse = new FadeTransition(Duration.millis(500), this);
	
	public Cursor(ELM elm) {
		this.getStyleClass().add("cusrsorElmUnfocused");
		pulse.setFromValue(1);
		pulse.setToValue(0.3);
		pulse.setAutoReverse(true);
		pulse.setCycleCount(Animation.INDEFINITE);
		pulse.setInterpolator(Interpolator.EASE_BOTH);
	}

	public void styleFocused() {
		this.getStyleClass().clear();
		this.getStyleClass().add("cusrsorElmFocused");	
		pulse.play();
	}
	
	public void styleUnfocused() {
		this.getStyleClass().clear();
		this.getStyleClass().add("cusrsorElmUnfocused");
		pulse.stop();		
	}	
}
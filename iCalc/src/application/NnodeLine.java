/*******************************************************************************
 *
 * Copyright (c) 2023 Constellatio BI
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
 * You must obtain a separate commercial license if you wish to use this software for commercial purposes. 
 * Please contact me at 916-390-9979 or rakhuba@gmail.com for licensing information.
 *******************************************************************************/
package application;


import javafx.scene.shape.CubicCurve;
import map.Nnode;

public class NnodeLine extends CubicCurve {
	private Nnode startNnode;
	private Nnode endNnode;
	
	public NnodeLine(Nnode startNnode, Nnode endNnode) {
		this.startNnode = startNnode; 
		this.endNnode = endNnode;
		updateLayout();
		this.getStyleClass().add("dotedLine");
	}

	// ••••••••••••••••••••••••••
	public void updateLayout() {
		double startX = startNnode.getCenterX();
		double startY = startNnode.getCenterY();
		double endX =  endNnode.getCenterX();
		double endY =  endNnode.getCenterY();
		setStartX(startX);
		setStartY(startY);
		setEndX(endX);
		setEndY(endY);
		setControlY1(startY);
		setControlY2(endY);
		
		//Curved lines
		setControlX1((startX + endX)/2); 
		setControlX2((startX + endX)/2);
		
		//Flat lines
//		setControlX1(startX); 
//		setControlX2(endX);
	}
	
//	public void createGredient(String fromColor, String toColor) {
//		setStroke(new LinearGradient(this.getStartX(), this.getStartY(), this.getEndX(), this.getEndY(),  false, CycleMethod.NO_CYCLE,new Stop(1,Color.valueOf(fromColor)), new Stop(0.1,Color.valueOf(toColor))));// Light to Dark
//	}
}

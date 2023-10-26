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

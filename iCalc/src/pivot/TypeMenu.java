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
 *   sub-license, and/or sell copies of the Software, and to permit persons to
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
 *   (documentation will be provided for commercial license)
 *  
 *  
 *******************************************************************************/
package pivot;

import clients.DataType;
import javafx.collections.ObservableMap;
import javafx.scene.control.Menu;
import logic.Field;
import logic.NFormat;

public class TypeMenu extends Menu {
	private NSelector nselector;
	
	public TypeMenu(Menu parentMenu, Field field, DataType tp) {
		nselector = new NSelector(tp.getName());;
		this.setGraphic(nselector.getLabel());
		ObservableMap<String, NFormat> fomats = field.getFieldLay().getNnode().getNmap().getNapp().getDBManager().getActiveConnection().getXMLBase().getNFormats();
		nselector.getLabel().setOnMouseClicked(e ->{
			nselector.setValue(true);
			field.setRowset_type(tp.getName()); //set fornat to plain on every chage
			
			if(!tp.getFormats().contains(field.getFormat().getId())) {
				field.setFormat(fomats.get(tp.getFormats().get(0)));
			}			
			
			this.getItems().forEach(j -> ((FormatMenuItem)j).updateStyle());
			parentMenu.getItems().forEach(i2 ->{
				if(i2 != this )  ((TypeMenu)i2).getNselector().setValue(false);
			});
		});
		
		//FORMAT CREATE
		fomats.forEach((i,nf) ->{
			if(tp.getFormats().contains(nf.getId())) {
				FormatMenuItem fmenu =  new FormatMenuItem(field, nf, tp);
				fmenu.setOnAction(e ->{
					nselector.setValue(true);
					field.setRowset_type(tp.getName()); //set fornat to plain on every chage 	
					field.setFormat(nf);
					this.getItems().forEach(j -> ((FormatMenuItem)j).updateStyle());
					parentMenu.getItems().forEach(i2 ->{
						if(i2 != this )  ((TypeMenu)i2).getNselector().setValue(false);
					});
				});
				this.getItems().add(fmenu);
			}
		});
		
		//FORMAT UPDATE
		this.setOnShowing(sh ->{
			this.getItems().forEach(i -> ((FormatMenuItem)i).updateStyle());
		});
				
	}
	
	public NSelector getNselector() {
		return nselector;
	}
	
}
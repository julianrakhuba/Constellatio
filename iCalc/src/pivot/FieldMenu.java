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
package pivot;


import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import logic.Field;

public class FieldMenu extends Menu {
	private CustomMenuItem pivot;	
	private CustomMenuItem group;	
	private CustomMenuItem agrigate;	
	
	public FieldMenu(Field field) {
		super(null,field.getSelectLabel());
		pivot =  new CustomMenuItem( field.getPivotLabel(), false);		
		group =  new CustomMenuItem(field.getGroupLabel(), false);		
		agrigate =  new CustomMenuItem(field.getAgrigateLabel(), false);
		
		Menu typeM = new Menu("data");
		
		
//		Arrays.asList(DataTypes.values()).forEach(dt ->{
////			typeM.getItems().add(new TypeMenu(typeM,field, dt.toString()));
//		});
		
		field.getFieldLay().getNnode().getNmap().getNapp().getDBManager().getActiveConnection().getXMLBase().getFtype().forEach(tp ->{
			typeM.getItems().add(new TypeMenu(typeM,field, tp));
		});
		
		typeM.setOnShowing(sh ->{
			typeM.getItems().forEach(i ->{				
				NSelector ns = ((TypeMenu)i).getNselector();
				if (ns.getLabel().getText().equals(field.getRowset_type())) { 
					ns.setValue(true);
				}else {
					ns.setValue(false);
				}
			});
		});
		
//		//FORMAT CREATE
//		Menu formatM = new Menu("format");
//		field.getFieldLay().nnode.nmap.napp.getDBManager().getActiveConnection().getXmlBase().getNFormats().forEach((i,nf) ->{
//			FormatMenuItem fmenu =  new FormatMenuItem(field, nf);		
//			fmenu.setOnAction(e ->{
//				field.setFormat(nf);
//				formatM.getItems().forEach(j -> ((FormatMenuItem)j).updateStyle());
//			});
//			formatM.getItems().add(fmenu);
//		});
//		
//		//FORMAT UPDATE
//		formatM.setOnShowing(sh ->{
//			formatM.getItems().forEach(i -> ((FormatMenuItem)i).updateStyle());
//		});
		
		this.getItems().addAll(pivot, group, agrigate, new SeparatorMenuItem(),  typeM);
	}
}
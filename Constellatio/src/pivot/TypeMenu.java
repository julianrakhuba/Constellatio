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
		ObservableMap<String, NFormat> fomats = field.getFieldLay().nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getNFormats();
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
//				fmenu.setOnMenuValidation(e ->{
//////					fmenu.getNseletcor().setValue(true);
////					System.out.println("ON setOnMenuValidation item: "+ tp.getName());
////					fmenu.updateStyle();
//				});
								
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
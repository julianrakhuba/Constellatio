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
		
		field.getFieldLay().nnode.nmap.napp.getDBManager().getActiveConnection().getXMLBase().getFtype().forEach(tp ->{
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
package rakhuba.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import rakhuba.logic.Field;
import rakhuba.pivot.PivotColumn;

public class NSheet extends Tab {
	private TableView<OpenBO> tableView = new TableView<OpenBO>();
	private LAY lay;
	private boolean calculateCells = false;
	
	public void setCalculateCells(boolean calculateCells) {
		this.calculateCells = calculateCells;
		if(!calculateCells) {
			lay.nnode.nmap.napp.sumLabel.clear();
			lay.nnode.nmap.napp.countLabel.clear();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public NSheet(LAY lay) {
		this.lay = lay;	
	    this.setText(lay.nnode.getTable() + " ");
		this.setOnClosed(e ->{
			lay.clearPopulation();
		});
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.setTableMenuButtonVisible(true);
		this.setContent(tableView);		
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {			
			
			if(calculateCells) {
				FilteredList<TablePosition> list = tableView.getSelectionModel().getSelectedCells().filtered(p ->  p.getTableColumn().getCellObservableValue(p.getRow()).getValue() instanceof Number);
				if(list.size() > 1 ) {
					lay.nnode.nmap.napp.sumLabel.clear();
					list.forEach(e -> lay.nnode.nmap.napp.sumLabel.add((Number) e.getTableColumn().getCellObservableValue(e.getRow()).getValue()));
				}else {
					lay.nnode.nmap.napp.sumLabel.clear();
				}
				int countSize = tableView.getSelectionModel().getSelectedCells().size();
				if(countSize > 1) {
					lay.nnode.nmap.napp.countLabel.setCountValue(countSize);
				}else {
					lay.nnode.nmap.napp.countLabel.clear();
				}
			} else {

			}
			//does this belong here?? create method in column for usedData replacement
			if(tableView.getSelectionModel().getSelectedCells().size() == 1) {
				PivotColumn version =  (PivotColumn) tableView.getSelectionModel().getSelectedCells().get(0).getTableColumn().getUserData();
				if(version != null) {
					lay.nnode.nmap.napp.sumLabel.setText(version.getTip());
					version.pulseLay();
					tableView.getSelectionModel().getSelectedItem();
				}
			}
		});	
		
		tableView.getColumns().addListener(new ListChangeListener<TableColumn<OpenBO,?>>() {
		     public void onChanged(Change<? extends TableColumn<OpenBO,?>> change) {
		    	 change.next();
		    	 ArrayList<Field> fldz = new  ArrayList<Field>();
		    	 change.getAddedSubList().forEach((tc) ->{		    		 
		    		 PivotColumn version =  (PivotColumn) tc.getUserData();
		    		 if(version != null) {
						if(!fldz.contains(version.getField())) {
							fldz.add(version.getField());
						}
		    		 }
		    	 });
		    	 Collections.sort(lay.getSelectedFields(), Comparator.comparing(item -> fldz.indexOf(item)));
		     }
		});
	}

	public TableView<OpenBO> getTableView() {
        return tableView;
	}
	
	public LAY getLay() {
		return lay;
	}
}

package rakhuba.application;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.scene.control.Label;

public class InfoLabel extends Label {
	private Double totalSum = Double.valueOf(0);
	private String type;

	public InfoLabel(String type) {
		this.type = type;
		this.setText("");
	}

	public void add(Number number) {
		NumberFormat df = new DecimalFormat("#.##");
		totalSum = totalSum + number.doubleValue();
		String ft = df.format(totalSum);
		this.setText( type +" "+ ft);		
	}
	
	public void setCountValue(int count) {
		this.setText(type + " " + count);
	}
	
	public void clear() {
		totalSum = 0.0;
		this.setText("");
	}
}

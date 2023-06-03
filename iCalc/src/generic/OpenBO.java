package generic;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;

import application.XML;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import pivot.PivotColumn;

public class OpenBO {
	private HashMap<String, SimpleObjectProperty<String>> strings = new HashMap<String, SimpleObjectProperty<String>>();
	private HashMap<String, SimpleObjectProperty<Number>> numbers = new HashMap<String, SimpleObjectProperty<Number>>();
	private HashMap<String, SimpleObjectProperty<Time>> times = new HashMap<String, SimpleObjectProperty<Time>>();
	private HashMap<String, SimpleObjectProperty<Date>> dates = new HashMap<String, SimpleObjectProperty<Date>>();
	private HashMap<String, SimpleObjectProperty<Timestamp>> timestamps = new HashMap<String, SimpleObjectProperty<Timestamp>>();
	
	public OpenBO(ResultSet rs, LAY lay) {
//		rs.get
		
		lay.getVersions().forEach(version -> {
			String als = version.getAliase();
			try {
				if(version.getField().isExcludedType()) {
					strings.put(als, new SimpleObjectProperty<String>(this , als, "-------"));
				}else {
					boolean sqlite = lay.nnode.nmap.napp.getDBManager().getActiveConnection().getLogin().getDb().equals("sqlite");
					
					if(version.getField().isString()) {//STRING
						strings.put(als, new SimpleObjectProperty<String>(this , als, rs.getString(als) == null? null : rs.getString(als)));				
					}else if(version.getField().isDouble()) {//DOUBLE
						numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getDouble(als)));
					}
					else if(version.getField().isFloat()) {//FLOAT
						numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getFloat(als)));
					}
					else if(version.getField().isInt()) {//INT
						numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getInt(als)));
					}
					else if(version.getField().isBigDecimal()) {//BIGDCIMAL
						numbers.put(als, new SimpleObjectProperty<Number>(this , als,rs.getBigDecimal(als) == null? 0 : rs.getBigDecimal(als)));
					}else if(version.getField().isTime()) {//TIME
						times.put(als, new SimpleObjectProperty<Time>(this , als, rs.getTime(als) == null? null : rs.getTime(als, java.util.Calendar.getInstance())));					
					}else if(version.getField().isTimestamp()) {//TIMESTAMP
						timestamps.put(als, this.newTimestamp(version, rs, sqlite, als));
					}else if(version.getField().isDate()) {//DATE	
						dates.put(als, this.newDate(version, rs, sqlite, als));
					}
				}				
			} catch (SQLException e) {e.printStackTrace();}
		});
	}
	
	private SimpleObjectProperty<Timestamp> newTimestamp(PivotColumn version, ResultSet rs, boolean sqlite, String als) throws SQLException {	
		if(sqlite) {
			return new SimpleObjectProperty<Timestamp>(this , als, rs.getString(als) == null? null : Timestamp.valueOf(rs.getString(als)));
		}else {
			return new SimpleObjectProperty<Timestamp>(this , als, rs.getTimestamp(als) == null? null : rs.getTimestamp(als, java.util.Calendar.getInstance()));	
		}
	}

	private SimpleObjectProperty<Date> newDate(PivotColumn version, ResultSet rs, boolean sqlite, String als) throws SQLException {
		if(sqlite) {
			return new SimpleObjectProperty<Date>(this , als, rs.getString(als) == null? null : Date.valueOf(rs.getString(als)));
		}else {
			return new SimpleObjectProperty<Date>(this , als, rs.getDate(als) == null? null : rs.getDate(als, java.util.Calendar.getInstance()));
		}
	}

	public OpenBO(Node boX) {
		List<Node> nodes = XML.children(boX);
		nodes.forEach(n ->{
			if (n.getNodeName().equals("col")) {
				String alias = XML.atr(n, "aliase");
				String rtype = XML.atr(n, "rowset_type");
				String isNull = XML.atr(n, "isNull");
				String text = n.getTextContent();				
				if(rtype.equals("String")) strings.put(alias, new SimpleObjectProperty<String>(this , alias, isNull.equals("true") ? null : new String(text)));				
				else if(rtype.equals("Double")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Double.valueOf(text)));
				else if(rtype.equals("Float")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Float.valueOf(text)));
				else if(rtype.equals("Int")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Integer.valueOf(text)));
				else if(rtype.equals("BigDecimal")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : new BigDecimal(text)));
				else if(rtype.equals("Time")) times.put(alias, new SimpleObjectProperty<Time>(this , alias, isNull.equals("true") ? null : Time.valueOf(text)));
				else if(rtype.equals("Date")) dates.put(alias, new SimpleObjectProperty<Date>(this , alias, isNull.equals("true") ? null : Date.valueOf(text)));
				else if(rtype.equals("Timestamp")) timestamps.put(alias, new SimpleObjectProperty<Timestamp>(this , alias, isNull.equals("true") ? null : Timestamp.valueOf(text)));
			}
		});
	}

	public SimpleObjectProperty<String> getString(String alias) {
		return strings.get(alias);
	}
	
	public SimpleObjectProperty<Number> getNumber(String alias) {
		return numbers.get(alias);
	}
	
	public SimpleObjectProperty<Date> getDate(String alias) {
		return dates.get(alias);
	}
	
	public SimpleObjectProperty<Time> getTime(String alias) {
		return times.get(alias);
	}
	
	public SimpleObjectProperty<Timestamp> getTimestamp(String alias) {
		return timestamps.get(alias);
	}
	
	public SimpleObjectProperty<?> getProperty(PivotColumn version) {
		if(version.getField().isString()) return strings.get(version.getAliase());
		else if(version.getField().isNumber()) return numbers.get(version.getAliase());
		else if(version.getField().isTime()) return times.get(version.getAliase());
		else if(version.getField().isDate()) return dates.get(version.getAliase());
		else if(version.getField().isTimestamp()) return timestamps.get(version.getAliase());
		else if(version.getField().isExcludedType()) return strings.get(version.getAliase());
		else {
			return null;
		}
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Series<String, Number> getSeries(LAY lay) {
		Series<String, Number> oranges = new Series<>();		
//		oranges.setName(lay.getVersions().get(0).get);//group by
		String valuez =  (String) this.getProperty(lay.getVersions().get(0)).get();
		oranges.setName(valuez);
				
		lay.getVersions().subList(1, lay.getVersions().size()).forEach(version -> {
			if(version.getPivotField() != null) {
				String dataname = version.getLabel();
				Number value = (Number) this.getProperty(version).get();
				oranges.getData().add(new Data<String, Number>(dataname, value));
			}
//			System.out.println("version: "+ version.getField().getFunction_Column() + " is group: "+version.getField().isGroupBy());
		});
		
		
		//•••••••••••••••••••••••
//		oranges.getData().add(new Data("a", 167));
//		oranges.getData().add(new Data("b", 1542));
//		oranges.getData().add(new Data("c", 1400));
		return oranges;
	}	
}















//
//
////
//package rakhuba.generic;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Time;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//
//import org.w3c.dom.Node;
//
//import javafx.beans.property.SimpleObjectProperty;
//import rakhuba.application.XML;
//import rakhuba.pivot.PivotColumn;
//
//public class OpenBO {
//	private HashMap<String, SimpleObjectProperty<String>> strings = new HashMap<String, SimpleObjectProperty<String>>();
//	private HashMap<String, SimpleObjectProperty<Number>> numbers = new HashMap<String, SimpleObjectProperty<Number>>();
//	private HashMap<String, SimpleObjectProperty<Time>> times = new HashMap<String, SimpleObjectProperty<Time>>();
//	private HashMap<String, SimpleObjectProperty<Date>> dates = new HashMap<String, SimpleObjectProperty<Date>>();
//	private HashMap<String, SimpleObjectProperty<Timestamp>> timestamps = new HashMap<String, SimpleObjectProperty<Timestamp>>();
//	
//	public OpenBO(ResultSet rs, LAY lay) {
//		lay.getVersions().forEach(version -> {
//			String als = version.getAliase();
//			try {
//				if(version.getField().isExcludedType()) {
//					strings.put(als, new SimpleObjectProperty<String>(this , als, "-------"));
//				}else {
//					boolean sqlite = lay.nnode.nmap.napp.getDBManager().getActiveConnection().getLogin().getDb().equals("sqlite");
//					if(version.getField().isString()) strings.put(als, new SimpleObjectProperty<String>(this , als, rs.getString(als)));				
//					else if(version.getField().isDouble()) numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getDouble(als)));
//					else if(version.getField().isFloat()) numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getFloat(als)));
//					else if(version.getField().isInt()) numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getInt(als)));
//					else if(version.getField().isBigDecimal()) numbers.put(als, new SimpleObjectProperty<Number>(this , als, rs.getBigDecimal(als)));
//					else if(version.getField().isTime()) times.put(als, new SimpleObjectProperty<Time>(this , als, rs.getTime(als, java.util.Calendar.getInstance())));					
//					else if(version.getField().isTimestamp()) {//TIMESTAMP
//						timestamps.put(als, this.createTimestamp(version, rs, sqlite, als));
////						if(sqlite) {
////							timestamps.put(als, new SimpleObjectProperty<Timestamp>(this , als, rs.getString(als) == null? null : Timestamp.valueOf(rs.getString(als))));
////						}else {
////							timestamps.put(als, new SimpleObjectProperty<Timestamp>(this , als, rs.getTimestamp(als) == null? null : rs.getTimestamp(als, java.util.Calendar.getInstance())));	
////						}
//					}else if(version.getField().isDate()) {//DATE	
//						dates.put(als, this.createDate(version, rs, sqlite, als));
////						if(sqlite) {
////							dates.put(als, new SimpleObjectProperty<Date>(this , als, rs.getString(als) == null? null : Date.valueOf(rs.getString(als))));
////						}else {
////							dates.put(als, new SimpleObjectProperty<Date>(this , als, rs.getDate(als) == null? null : rs.getDate(als, java.util.Calendar.getInstance())));
////						}
//					}
//				}				
//			} catch (SQLException e) {e.printStackTrace();}
//		});
//	}
//	
//	private SimpleObjectProperty<Timestamp> createTimestamp(PivotColumn version, ResultSet rs, boolean sqlite, String als) throws SQLException {
//		if(sqlite) {
//			return new SimpleObjectProperty<Timestamp>(this , als, rs.getString(als) == null? null : Timestamp.valueOf(rs.getString(als)));
//		}else {
//			return new SimpleObjectProperty<Timestamp>(this , als, rs.getTimestamp(als) == null? null : rs.getTimestamp(als, java.util.Calendar.getInstance()));	
//		}
//	}
//
//	private SimpleObjectProperty<Date> createDate(PivotColumn version, ResultSet rs, boolean sqlite, String als) throws SQLException {
//		if(sqlite) {
//			return new SimpleObjectProperty<Date>(this , als, rs.getString(als) == null? null : Date.valueOf(rs.getString(als)));
//		}else {
//			return new SimpleObjectProperty<Date>(this , als, rs.getDate(als) == null? null : rs.getDate(als, java.util.Calendar.getInstance()));
//		}
//	}
//
//	public OpenBO(Node boX) {
//		List<Node> nodes = XML.children(boX);
//		nodes.forEach(n ->{
//			if (n.getNodeName().equals("col")) {
//				String alias = XML.atr(n, "aliase");
//				String rtype = XML.atr(n, "rowset_type");
//				String isNull = XML.atr(n, "isNull");
//				String text = n.getTextContent();				
//				if(rtype.equals("String")) strings.put(alias, new SimpleObjectProperty<String>(this , alias, isNull.equals("true") ? null : new String(text)));				
//				else if(rtype.equals("Double")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Double.valueOf(text)));
//				else if(rtype.equals("Float")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Float.valueOf(text)));
//				else if(rtype.equals("Int")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : Integer.valueOf(text)));
//				else if(rtype.equals("BigDecimal")) numbers.put(alias, new SimpleObjectProperty<Number>(this , alias, isNull.equals("true") ? null : new BigDecimal(text)));
//				else if(rtype.equals("Time")) times.put(alias, new SimpleObjectProperty<Time>(this , alias, isNull.equals("true") ? null : Time.valueOf(text)));
//				else if(rtype.equals("Date")) dates.put(alias, new SimpleObjectProperty<Date>(this , alias, isNull.equals("true") ? null : Date.valueOf(text)));
//				else if(rtype.equals("Timestamp")) timestamps.put(alias, new SimpleObjectProperty<Timestamp>(this , alias, isNull.equals("true") ? null : Timestamp.valueOf(text)));
//			}
//		});
//	}
//
//	public SimpleObjectProperty<String> getString(String alias) {
//		return strings.get(alias);
//	}
//	
//	public SimpleObjectProperty<Number> getNumber(String alias) {
//		return numbers.get(alias);
//	}
//	
//	public SimpleObjectProperty<Date> getDate(String alias) {
//		return dates.get(alias);
//	}
//	
//	public SimpleObjectProperty<Time> getTime(String alias) {
//		return times.get(alias);
//	}
//	
//	public SimpleObjectProperty<Timestamp> getTimestamp(String alias) {
//		return timestamps.get(alias);
//	}
//	
//	public SimpleObjectProperty<?> getProperty(PivotColumn version) {
//		if(version.getField().isString()) return strings.get(version.getAliase());
//		else if(version.getField().isNumber()) return numbers.get(version.getAliase());
//		else if(version.getField().isTime()) return times.get(version.getAliase());
//		else if(version.getField().isDate()) return dates.get(version.getAliase());
//		else if(version.getField().isTimestamp()) return timestamps.get(version.getAliase());
//		else if(version.getField().isExcludedType()) return strings.get(version.getAliase());
//		else {
//			return null;
//		}
//	}	
//}


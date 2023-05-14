package rakhuba.file;

import java.util.HashMap;

import rakhuba.generic.LAY;
import rakhuba.logic.Field;
import rakhuba.logic.SearchCON;
import rakhuba.pivot.PivotColumn;

public class OpenContext {
	private HashMap<String, HashMap<LAY, HashMap<String, SearchCON>>> mapAliaseCONs = new HashMap<String, HashMap<LAY, HashMap<String, SearchCON>>>();
	private HashMap<String, LAY> allLAYs = new HashMap<String, LAY>();
	private HashMap<String, PivotColumn> versions = new HashMap<String, PivotColumn>();
	private HashMap<String, Field> fields = new HashMap<String, Field>();


	

	public HashMap<String, LAY> getAliaseLAYs(){
		return allLAYs;
	}

	public HashMap<String, SearchCON> getAliaseCONS(String schema, LAY lay) {
		if(!mapAliaseCONs.containsKey(schema)) mapAliaseCONs.put(schema,  new  HashMap<LAY, HashMap<String, SearchCON>>());
		if(!mapAliaseCONs.get(schema).containsKey(lay)) mapAliaseCONs.get(schema).put(lay, new HashMap<String, SearchCON>());
		return mapAliaseCONs.get(schema).get(lay);
	}

	public HashMap<String, PivotColumn> getVersions() {
		return versions;
	}

	public HashMap<String, Field> getFields() {
		return fields;
	}

}

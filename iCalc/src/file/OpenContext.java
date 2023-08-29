package file;

import java.util.HashMap;

import generic.LAY;
import logic.Field;
import logic.SearchCON;
import pivot.FieldVersion;

public class OpenContext {
	private HashMap<String, HashMap<LAY, HashMap<String, SearchCON>>> mapAliaseCONs = new HashMap<String, HashMap<LAY, HashMap<String, SearchCON>>>();
	private HashMap<String, LAY> allLAYs = new HashMap<String, LAY>();
	private HashMap<String, FieldVersion> versions = new HashMap<String, FieldVersion>();
	private HashMap<String, Field> fields = new HashMap<String, Field>();


	

	public HashMap<String, LAY> getAliaseLAYs(){
		return allLAYs;
	}

	public HashMap<String, SearchCON> getAliaseCONS(String schema, LAY lay) {
		if(!mapAliaseCONs.containsKey(schema)) mapAliaseCONs.put(schema,  new  HashMap<LAY, HashMap<String, SearchCON>>());
		if(!mapAliaseCONs.get(schema).containsKey(lay)) mapAliaseCONs.get(schema).put(lay, new HashMap<String, SearchCON>());
		return mapAliaseCONs.get(schema).get(lay);
	}

	public HashMap<String, FieldVersion> getVersions() {
		return versions;
	}

	public HashMap<String, Field> getFields() {
		return fields;
	}

}

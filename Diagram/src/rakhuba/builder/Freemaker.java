package rakhuba.builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import rakhuba.builder.base.clientcomponents.NTable;
import rakhuba.generic.BaseConnection;

public class Freemaker {
	private Configuration config = new Configuration(Configuration.VERSION_2_3_25);
	public HashMap<String, java.lang.Object> data = new HashMap<String, java.lang.Object>();

	public Freemaker()  {
		try {
			config.clearTemplateCache();
			config.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir") + "/src/rakhuba/builder/"));
			config.setDefaultEncoding("UTF-8");
			config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			config.setLogTemplateExceptions(false);
		}
		catch (IOException e) {e.printStackTrace();}
	}
//create method with if null for configuration
	
	public void build(String type, BaseConnection baseConnection) {
		try {
			NTable tbo = ((NTable) data.get("table"));
			String directory = System.getProperty("user.dir") + "/src/rakhuba/dbs/" + baseConnection.getLogin().getDbInstance() +"/" + tbo.getSchema();
			File dir = new File(directory); 
			if (!dir.exists()) dir.mkdirs();
			String filename =  ((NTable) data.get("table")).getTable() + type + ".java";
			FileWriter writer = new FileWriter(new File(directory + "/" + filename.replaceAll(" ", "_")));
			Template template = config.getTemplate(type + ".ftl");
			template.process(data, writer);
			writer.close();
		} 
		catch (TemplateException | IOException e) { e.printStackTrace(); }
	}

	public void prebuild(String type) {
		try {
			String table = (String) data.get("table");
			String database = (String) data.get("database");
			String directory = System.getProperty("user.dir") + "/src/rakhuba/builder/" + database;
			File dir = new File(directory); 
			if (!dir.exists()) dir.mkdirs();
			String filename =  table + "_" + type + ".java";
			FileWriter writer = new FileWriter(new File(directory + "/" + filename.replaceAll(" ", "_")));
			Template template = config.getTemplate(type + ".ftl");
			template.process(data, writer);
			writer.close();
			System.out.println("Created: " + filename);
		} 
		catch (TemplateException | IOException e) { e.printStackTrace(); }
	}

}
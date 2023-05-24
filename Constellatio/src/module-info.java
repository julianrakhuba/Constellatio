/**
 * 
 */
/**
 * @author julianrakhuba
 *
 */
module ConstModule {
	
//	javafx.graphics may not be accessible to clients due to missing 'requires transitive'
	
	
	requires transitive java.xml;
	requires transitive java.sql;
//	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires org.apache.commons.io;
	requires freemarker;
	requires java.desktop;
	requires javafx.graphics;	
//	exports application;
	exports constellatio;
//	
//	exports generic;
	
//	application
	
//	because module HelloFX does not export application to module javafx.graphics
}
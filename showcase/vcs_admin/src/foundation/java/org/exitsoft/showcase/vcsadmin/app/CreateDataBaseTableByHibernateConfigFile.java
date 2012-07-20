package org.exitsoft.showcase.vcsadmin.app;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 
 * @author vincent
 *
 */
public class CreateDataBaseTableByHibernateConfigFile {
	
	
	public static void main(String[] args) {
		
		SchemaExport export = new SchemaExport(new Configuration().configure());
		
		export.setFormat(false);
		
		export.setOutputFile("src/test/resources/h2schma.sql");
		
		export.create(true, false);
	}
	
}

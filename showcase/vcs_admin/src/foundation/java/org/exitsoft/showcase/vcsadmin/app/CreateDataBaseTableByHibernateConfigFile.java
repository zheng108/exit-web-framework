package org.exitsoft.showcase.vcsadmin.app;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 
 * 通过Hibernate配置文件创建生成表的sql语句到src/test/resources/h2schma.sql文件中，
 * 该类是对单元测试的模拟创建表起辅助作用
 * 
 * @author vincent
 *
 */
public class CreateDataBaseTableByHibernateConfigFile {
	
	
	public static void main(String[] args) {
		
		SchemaExport export = new SchemaExport(new Configuration().configure().setNamingStrategy(new ImprovedNamingStrategy()));
		
		export.setFormat(false);
		
		export.setOutputFile("src/test/resources/h2schma.sql");
		
		export.create(true, false);
	}
	
}

package org.exitsoft.showcase.vcsadmin.unit;

import java.util.HashMap;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 业务单元测试基类
 * 
 * @author vincent
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class ManagerTestCaseSuper {
	
	protected DataSource dataSource;
	
	protected NamedParameterJdbcTemplate jdbcTemplate;
	
	private static DataSource dataSourceHandler;
	
	protected SessionFactory sessionFactory;
	
	@Autowired
	public void setDataSource(DataSource dataSource) throws Exception {
		this.dataSource = dataSource;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForInt("SELECT COUNT(0) FROM " + tableName,new HashMap<String, Object>());
	}
	
	@Before
	public void reloadSampleData() throws Exception {
		if (dataSourceHandler == null) {
			Fixtures.loadData(dataSource, "/sample-data.xml");
			dataSourceHandler = dataSource;
		}

	}

	@AfterClass
	public static void cleanData() throws Exception {
		Fixtures.deleteData(dataSourceHandler, "/sample-data.xml");
		dataSourceHandler = null;
	}

	
	
}

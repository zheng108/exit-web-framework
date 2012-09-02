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

	/**
	 * 通过表名计算出表中的总记录数
	 * 
	 * @param tableName 表名
	 * 
	 * @return int
	 */
	protected int countRowsInTable(String tableName) {
		return jdbcTemplate.queryForInt("SELECT COUNT(0) FROM " + tableName,new HashMap<String, Object>());
	}
	
	/**
	 * 
	 * 每个单元测试用例开始先把模拟数据加载到dataSource中，如果整个单元测试类（不是单个方法用例，是整个类）已经存在数据，就不重复加载
	 * 
	 * @throws Exception
	 */
	@Before
	public void reloadSampleData() throws Exception {
		if (dataSourceHandler == null) {
			Fixtures.loadData(dataSource, "/sample-data.xml");
			dataSourceHandler = dataSource;
		}

	}
	
	/**
	 * 整个类的单元测试方法用例测试完成后，将dataSource的模拟数据清楚，让reloadSampleData方法在重新加载模拟数据，供第二个单元测试类使用
	 * @throws Exception
	 */
	@AfterClass
	public static void cleanData() throws Exception {
		Fixtures.deleteData(dataSourceHandler, "/sample-data.xml");
		dataSourceHandler = null;
	}

	
	
}

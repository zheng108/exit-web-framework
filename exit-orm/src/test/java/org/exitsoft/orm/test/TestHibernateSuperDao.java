package org.exitsoft.orm.test;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.exitsoft.common.unit.Fixtures;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.HibernateSuperDao;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LikeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.NeRestriction;
import org.exitsoft.orm.test.entity.Role;
import org.exitsoft.orm.test.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * 测试HibernateSuperDao的查询方法
 * 
 * @author vincent
 *
 */
@ContextConfiguration(locations = { "/applicationContext-core-test.xml" })
public class TestHibernateSuperDao extends AbstractTransactionalJUnit4SpringContextTests{

	private HibernateSuperDao<User, String> dao;
	
	private DataSource dataSource;
	
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.dataSource = dataSource;
		
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new HibernateSuperDao<User, String>(User.class);
		dao.setSessionFactory(sessionFactory);
	}
	
	@Before
	public void reloadSampleData() throws Exception {
		simpleJdbcTemplate.update("drop all objects");

		executeSqlScript("classpath:/h2schma.sql", false);

		Fixtures.loadData(this.dataSource, "/sample-data.xml");
	}
	
	@Test
	public void testFind() {
		List<User> userList = new ArrayList<User>();
		List<Role> roleList = new ArrayList<Role>();
		
		//---------------------------------------------Criterion test--------------------------------------------------//
		
		userList = dao.findByCriterion(Restrictions.eq("loginName", "admin"));
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByCriterion("loginName",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_ASC,realName_desc",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_asc,realName_DESC",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName,realName_DESC",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByCriterion("loginName_asc,realName",Restrictions.eq("state", 1));
		Assert.assertEquals(userList.size(), 8);
		
		roleList = dao.findByCriterion(Role.class, Restrictions.eq("name", "系统管理员"));
		Assert.assertEquals(roleList.size(), 1);
		
		roleList = dao.findByCriterion(Role.class, "name_asc");
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByCriterion(Role.class, "name_desc");
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------Expression test--------------------------------------------------//
		
		userList = dao.findByExpression("EQ_S_loginName", "admin");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("EQ_I_state", "1","loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQ_I_state", "1","loginName_asc,realName_DESC");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQ_I_state", "1","loginName,realName_DESC");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQ_I_state", "1","loginName_asc,realName");
		Assert.assertEquals(userList.size(), 8);
		
		roleList = dao.findByExpression("EQ_S_name", "系统管理员", Role.class);
		Assert.assertEquals(roleList.size(), 1);
		
		roleList = dao.findByExpression("NE_S_name", null, "name_asc",Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------Expressions test--------------------------------------------------//
		
		userList = dao.findByExpressions(new String[]{"EQ_S_loginName","EQ_S_realName"}, new String[]{"admin","admin"});
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpressions(new String[]{"LIKE_S_loginName","EQ_I_state"}, new String[]{"m","1"});
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKE_S_loginName","EQ_I_state"}, new String[]{"m","1"},"loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKE_S_loginName","EQ_I_state"}, new String[]{"m","1"},"loginName");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByExpressions(new String[]{"LIKE_S_loginName","EQ_I_state"}, new String[]{"m","1"},"realName_asc");
		Assert.assertEquals(userList.size(), 4);
		
		roleList = dao.findByExpressions(new String[]{"LIKE_S_name"}, new String[]{"系统"},Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByExpressions(new String[]{"LIKE_S_name"}, new String[]{"系统"},"name",Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		//---------------------------------------------PropertyFiter test--------------------------------------------------//
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"LIKE_S_loginName","EQ_I_state"}, new String[]{"m","1"});
		
		userList = dao.findByPropertyFilters(filters);
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"loginName_ASC,realName_desc");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"loginName");
		Assert.assertEquals(userList.size(), 4);
		
		userList = dao.findByPropertyFilters(filters,"realName_asc");
		Assert.assertEquals(userList.size(), 4);
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"LIKE_S_name"}, new String[]{"系统"});
		
		roleList = dao.findByPropertyFilters(filters, Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		roleList = dao.findByPropertyFilters(filters,"name", Role.class);
		Assert.assertEquals(roleList.size(), 3);
		
		userList = dao.findByQueryNamed("QueryUserResource", "系统管理");
	}
	
	@Test
	public void testFindByProperty() {
		List<User> userList = new ArrayList<User>();
		List<Role> roleList = new ArrayList<Role>();
		
		userList = dao.findByProperty("loginName", "admin");
		Assert.assertEquals(userList.size(),1);
		
		userList = dao.findByProperty("state", 1,NeRestriction.RestrictionName);
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByPropertyWithOrderBy("state", 1, "loginName_asc");
		Assert.assertEquals(userList.size(),8);
		
		userList = dao.findByPropertyWithOrderBy("state", 1, "loginName_asc",NeRestriction.RestrictionName);
		Assert.assertEquals(userList.size(),0);
		
		roleList = dao.findByProperty("name","系统",Role.class);
		Assert.assertEquals(roleList.size(),1);
		
		roleList = dao.findByProperty("name","系统",LikeRestriction.RestrictionName,Role.class);
		Assert.assertEquals(roleList.size(),3);
		
		roleList = dao.findByProperty("name","系统",LikeRestriction.RestrictionName,Role.class,"name");
		Assert.assertEquals(roleList.size(),3);
		
	}
	
	@Test
	public void testFindUnique() {
		User user = new User();
		Role role = new Role();
		
		user = dao.findUniqueByCriterions(new Criterion[]{Restrictions.eq("loginName", "admin")});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByCriterions(new Criterion[]{Restrictions.eq("name", "系统")}, Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		user = dao.findUniqueByExpression("EQ_S_loginName", "admin");
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByExpression("EQ_S_name", "系统",Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		user = dao.findUniqueByExpressions(new String[]{"EQ_S_loginName"}, new String[]{"admin"});
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByExpressions(new String[]{"EQ_S_name"}, new String[]{"系统"},Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQ_S_loginName"}, new String[]{"admin"});
		
		user = dao.findUniqueByPropertyFilters(filters);
		Assert.assertEquals(user.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQ_S_name"}, new String[]{"系统"});
		
		role = dao.findUniqueByPropertyFilters(filters, Role.class);
		Assert.assertEquals(role.getId(), "SJDK3849CKMS3849DJCK2039ZMSK0010");
		
	}
	
	@Test
	public void testFindUniqueByProperty() {
		User user = new User();
		Role role = new Role();
		
		user = dao.findUniqueByProperty("loginName", "admin");
		Assert.assertEquals(user.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0002");
		
		role = dao.findUniqueByProperty("name", "系统",Role.class);
		Assert.assertEquals(role.getId(),"SJDK3849CKMS3849DJCK2039ZMSK0010");
		
	}
	
	@Test
	public void testFindPage() {
		PageRequest request = new PageRequest(1,2);
		Page<User> user = new Page<User>();
		Page<Role> role = new Page<Role>();
		
		user = dao.findPage(request, Restrictions.eq("state", 1));
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPage(request, "EQ_I_state","1");
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQ_I_state"}, new String[]{"1"});
		
		user = dao.findPage(request, filters);
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		user = dao.findPage(request, new String[]{"EQ_I_state"}, new String[]{"1"});
		Assert.assertEquals(user.getResult().size(), 2);
		Assert.assertEquals(user.getTotalPages(), 4);
		Assert.assertEquals(user.getTotalItems(), 8);
		
		role = dao.findPage(request,Role.class,Restrictions.eq("name","系统管理员"));
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		role = dao.findPage(request,"EQ_S_name","系统管理员",Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		filters = PropertyFilterRestrictionHolder.createPropertyFilter(new String[]{"EQ_S_name"}, new String[]{"系统管理员"});
		
		role = dao.findPage(request,filters,Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
		
		role = dao.findPage(request,new String[]{"EQ_S_name"}, new String[]{"系统管理员"},Role.class);
		Assert.assertEquals(role.getResult().size(), 1);
		Assert.assertEquals(role.getTotalPages(), 1);
		Assert.assertEquals(role.getTotalItems(), 1);
	}
	
	@Test
	public void testAllRestriction() {
		List<User> userList = new ArrayList<User>();
		
		userList = dao.findByExpression("EQ_D_createTime", "2012-08-12");
		
		userList = dao.findByExpression("EQ_S_wubiCode", null);
		Assert.assertEquals(userList.size(), 1);
		userList = dao.findByExpression("EQ_S_wubiCode", "");
		Assert.assertEquals(userList.size(),6);
		userList = dao.findByExpression("EQ_S_wubiCode", "123");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("NE_S_wubiCode", null);
		Assert.assertEquals(userList.size(), 7);
		userList = dao.findByExpression("NE_S_wubiCode", "");
		Assert.assertEquals(userList.size(), 1);
		userList = dao.findByExpression("NE_S_wubiCode", "123");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findByExpression("LIKE_S_loginName", "m");
		Assert.assertEquals(userList.size(), 4);
		userList = dao.findByExpression("RLIKE_S_loginName", "m");
		Assert.assertEquals(userList.size(), 3);
		userList = dao.findByExpression("LLIKE_S_loginName", "n");
		Assert.assertEquals(userList.size(), 1);
		
		userList = dao.findByExpression("LE_I_state", "1");
		Assert.assertEquals(userList.size(), 8);
		userList = dao.findByExpression("LT_I_state", "2");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("GE_I_state", "1");
		Assert.assertEquals(userList.size(), 8);
		userList = dao.findByExpression("GT_I_state", "0");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("IN_S_loginName", "admin,vincent");
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findByExpression("NIN_S_loginName", "admin,vincent");
		Assert.assertEquals(userList.size(), 6);
		
		userList = dao.findByExpression("BT_I_state","1,2");
		Assert.assertEquals(userList.size(), 8);
		
		userList = dao.findByExpression("EQ_S_loginName","admin|vincent");
		Assert.assertEquals(userList.size(), 2);
		
		userList = dao.findByExpression("EQ_S_loginName","admin,vincent");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByExpression("EQ_S_loginName","admin,null");
		Assert.assertEquals(userList.size(),0);
		
		userList = dao.findByExpression("EQ_S_loginName_OR_realName","null|admin");
		Assert.assertEquals(userList.size(), 1);
		
	}

}

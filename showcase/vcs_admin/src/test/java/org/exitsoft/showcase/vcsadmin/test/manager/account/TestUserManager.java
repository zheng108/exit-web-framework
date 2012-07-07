package org.exitsoft.showcase.vcsadmin.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.State;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.exitsoft.showcase.vcsadmin.service.ServiceException;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.exitsoft.showcase.vcsadmin.unit.ManagerTestCaseSuper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试用户管理所有方法
 * 
 * @author vincent
 *
 */
public class TestUserManager extends ManagerTestCaseSuper{

	@Autowired
	private AccountManager accountManager;
	
	@Test(expected = ServiceException.class)
	public void testInsertUser() {
		User entity = new User();
		entity.setEmail("27637461@qq.com");
		entity.setPassword("123456");
		entity.setRealname("vincent");
		entity.setState(State.Enable.getValue());
		entity.setUsername("vincent");
		
		int beforeRow = countRowsInTable("tb_user");
		accountManager.insertUser(entity);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("tb_user");
		
		assertEquals(afterRow, beforeRow + 1);
		
		entity = new User();
		entity.setUsername("admin");
		
		accountManager.insertUser(entity);
	}
	
	@Test
	public void testUpdateUser() {
		User user = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		user.setUsername("other");
		user.setRealname("小");
		
		accountManager.updateUser(user);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		
		user = null;
		user = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		assertEquals(user.getUsername(), "admin");
		assertEquals(user.getRealname(), "小");
		
	}
	
	@Test
	public void testDeleteUsers() {
		List<String> ids = new ArrayList<String>();
		ids.add("SJDK3849CKMS3849DJCK2039ZMSK0001");
		
		int beforeRow = countRowsInTable("tb_user");
		accountManager.deleteUsers(ids);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("tb_user");
		
		assertEquals(afterRow, beforeRow - 1);
	}
	
	@Test
	public void testSearchUserPage() {
		PageRequest request = new PageRequest(1,1);
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(PropertyFilterRestrictionHolder.createPropertyFilter("EQ_I_state", "1"));
		Page<User> page = accountManager.searchUserPage(request, filters);
		assertEquals(page.getResult().size(), 1);
		assertEquals(page.getTotalItems(), 2);
		assertEquals(page.getTotalPages(), 2);
	}
	
	@Test
	public void updateUserPassword() {
		accountManager.updateUserPassword("SJDK3849CKMS3849DJCK2039ZMSK0001", "123456");
		String password = accountManager.getUser("SJDK3849CKMS3849DJCK2039ZMSK0001").getPassword();
		assertEquals(password, new SimpleHash("MD5", "123456").toHex());
	}
}

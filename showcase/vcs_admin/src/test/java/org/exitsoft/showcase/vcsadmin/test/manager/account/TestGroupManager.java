package org.exitsoft.showcase.vcsadmin.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.State;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.exitsoft.showcase.vcsadmin.unit.ManagerTestCaseSuper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试组管理所有方法
 * 
 * @author vincent
 *
 */
public class TestGroupManager extends ManagerTestCaseSuper{

	@Autowired
	private AccountManager accountManager;
	
	@Test
	public void testDeleteGroups() {
		List<String> ids = new ArrayList<String>();
		ids.add("SJDK3849CKMS3849DJCK2039ZMSK0003");
		
		int beforeRow = countRowsInTable("TB_GROUP");
		accountManager.deleteGroups(ids);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("TB_GROUP");
		
		assertEquals(afterRow, beforeRow - 4);
	}
	
	@Test
	public void testGetAllParentGroupsByType() {
		List<Group> groups = accountManager.getGroups(GroupType.RoleGorup);
		assertEquals(groups.size(), 1);
	}
	
	@Test
	public void testSaveGroup() {
		Group group = new Group();
		group.setName("test");
		group.setType(GroupType.RoleGorup.getValue());
		group.setState(State.Enable.getValue());
		group.setRemark("*");
		
		int beforeRow = countRowsInTable("TB_GROUP");
		accountManager.saveGroup(group);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("TB_GROUP");
		
		assertEquals(afterRow, beforeRow + 1);
	}
	
}

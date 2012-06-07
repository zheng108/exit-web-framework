package org.vcs.project.test.manager.account;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.exitsoft.project.vcsadmin.common.enumeration.entity.ResourceType;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.exitsoft.project.vcsadmin.service.account.AccountManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.vcs.project.test.manager.ManagerTestCaseSuper;

/**
 * 测试资源管理所有方法
 * 
 * @author vincent
 *
 */
public class TestResourceManager extends ManagerTestCaseSuper{
	
	@Autowired
	private AccountManager accountManager;
	
	@Test
	public void testGetAllResourcesByUserId() {
		List<Resource> list = accountManager.getAllResourcesByUserId("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(list.size(), 8);
		list = accountManager.mergeResourcesToParent(list, ResourceType.Security);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getChildren().size(), 3);
		assertEquals(list.get(1).getChildren().size(), 2);
	}
	
	@Test
	public void testGetAllParentResources() {
		List<Resource> list = accountManager.getAllParentResources();
		assertEquals(list.size(), 2);
	}
	
	@Test
	public void testGetAllResources() {
		List<Resource> list = accountManager.getAllResources();
		assertEquals(list.size(), 8);
	}
	
	@Test
	public void testGetAllParentMenuResources() {
		List<Resource> list = accountManager.getAllParentMenuResources();
		assertEquals(list.size(), 2);
	}
	
	@Test
	public void testSaveResource() {
		Resource resource = new Resource();
		resource.setRemark("*");
		resource.setName("test");
		resource.setType(ResourceType.Security.getValue());
		resource.setValue("**");
		
		int beforeRow = countRowsInTable("TB_RESOURCE");
		accountManager.saveResource(resource);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("TB_RESOURCE");
		
		assertEquals(afterRow, beforeRow + 1);
	}
	
	@Test
	public void testDeleteResource() {
		
		List<String> ids = new ArrayList<String>();
		ids.add("SJDK3849CKMS3849DJCK2039ZMSK0007");
		
		int beforeRow = countRowsInTable("TB_RESOURCE");
		accountManager.deleteResources(ids);
		sessionFactory.getCurrentSession().flush();
		int afterRow = countRowsInTable("TB_RESOURCE");
		
		assertEquals(afterRow, beforeRow - 4);
	}
}

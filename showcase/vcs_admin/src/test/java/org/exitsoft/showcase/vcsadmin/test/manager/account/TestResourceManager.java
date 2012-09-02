package org.exitsoft.showcase.vcsadmin.test.manager.account;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.exitsoft.showcase.vcsadmin.service.account.AccountManager;
import org.exitsoft.showcase.vcsadmin.unit.ManagerTestCaseSuper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(readOnly=true)
	public void testGetAllResourcesByUserId() {
		List<Resource> list = accountManager.getUserResourcesByUserId("SJDK3849CKMS3849DJCK2039ZMSK0001");
		assertEquals(list.size(), 8);
		list = accountManager.mergeResourcesToParent(list, ResourceType.Security);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0).getChildren().size(), 3);
		assertEquals(list.get(1).getChildren().size(), 2);
	}
	
	@Test
	@Transactional(readOnly=true)
	public void testGetAllParentResources() {
		List<Resource> list = accountManager.getAllParentResources();
		assertEquals(list.size(), 2);
	}
	
	@Test
	@Transactional(readOnly=true)
	public void testGetAllResources() {
		List<Resource> list = accountManager.getAllResources();
		assertEquals(list.size(), 8);
	}
	
	@Test
	@Transactional(readOnly=true)
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
		int afterRow = countRowsInTable("TB_RESOURCE");
		
		assertEquals(afterRow, beforeRow + 1);
	}
	
	@Test
	public void testDeleteResource() {
		
		List<String> ids = new ArrayList<String>();
		ids.add("SJDK3849CKMS3849DJCK2039ZMSK0007");
		
		int beforeRow = countRowsInTable("TB_RESOURCE");
		accountManager.deleteResources(ids);
		int afterRow = countRowsInTable("TB_RESOURCE");
		
		assertEquals(afterRow, beforeRow - 4);
	}
}

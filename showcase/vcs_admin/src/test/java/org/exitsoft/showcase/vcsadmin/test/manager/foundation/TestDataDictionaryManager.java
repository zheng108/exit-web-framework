package org.exitsoft.showcase.vcsadmin.test.manager.foundation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.exitsoft.orm.core.PropertyType;
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DictionaryCategory;
import org.exitsoft.showcase.vcsadmin.service.foundation.SystemDictionaryManager;
import org.exitsoft.showcase.vcsadmin.unit.ManagerTestCaseSuper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试数据字典管理所有方法
 * 
 * @author vincent
 *
 */
public class TestDataDictionaryManager extends ManagerTestCaseSuper{
	
	@Autowired
	private SystemDictionaryManager systemDictionaryManager;
	
	@Test
	public void testGetDataDictionariesByCategoryCode() {
		
		assertEquals("启用", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "1"));
		assertEquals("启用", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "1"));
		assertEquals("启用", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "1"));
		
		testSaveDataDictionary();
		
		assertEquals("a1b2c3d4e5f6g7h8i9j0k中文输入法a1b2c3d4e5f6g7h8i9j0k", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "4"));
		assertEquals("a1b2c3d4e5f6g7h8i9j0k中文输入法a1b2c3d4e5f6g7h8i9j0k", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "4"));
		assertEquals("a1b2c3d4e5f6g7h8i9j0k中文输入法a1b2c3d4e5f6g7h8i9j0k", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "4"));
		
		testDeleteDataDictionary();
		
		assertEquals("删除", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "3"));
		assertEquals("删除", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "3"));
		assertEquals("删除", SystemVariableUtils.getDictionaryNameByValue(SystemDictionaryCode.State, "3"));
	}
	
	public void testDeleteDataDictionary() {
		
		List<String> ids = new ArrayList<String>();
		CollectionUtils.addAll(ids, new String[]{"SJDK3849CKMS3849DJCK2039ZMSK0018","SJDK3849CKMS3849DJCK2039ZMSK0019"});
		
		int beforeRow = countRowsInTable("TB_DATA_DICTIONARY");
		systemDictionaryManager.deleteDataDictionary(ids);
		int afterRow = countRowsInTable("TB_DATA_DICTIONARY");
		
		assertEquals(afterRow, beforeRow - 2);
	}
	
	public void testSaveDataDictionary() {
		
		DictionaryCategory category = systemDictionaryManager.getDictionaryCategory("SJDK3849CKMS3849DJCK2039ZMSK0015");
		
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setCategory(category);
		dataDictionary.setName("a1b2c3d4e5f6g7h8i9j0k中文输入法a1b2c3d4e5f6g7h8i9j0k");
		dataDictionary.setValue("4");
		dataDictionary.setType(PropertyType.I.toString());
		dataDictionary.setRemark("*");
		
		int beforeRow = countRowsInTable("TB_DATA_DICTIONARY");
		systemDictionaryManager.saveDataDictionary(dataDictionary);
		int afterRow = countRowsInTable("TB_DATA_DICTIONARY");
		
		assertEquals(afterRow, beforeRow + 1);
		
		assertEquals(dataDictionary.getWubiCode(), "A1B2C3D4E5F6G7H8I9J0KKYLTIA1B2C3D4E5F6G7H8I9J0K");
		assertEquals(dataDictionary.getPinYinCode(), "A1B2C3D4E5F6G7H8I9J0KZWSRFA1B2C3D4E5F6G7H8I9J0K");
		
	}
	
}

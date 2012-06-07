package org.exitsoft.project.vcsadmin.common;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.exitsoft.project.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.project.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.project.vcsadmin.model.SecurityModel;
import org.exitsoft.project.vcsadmin.service.foundation.SystemDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统变量工具类
 * 
 * @author vincent
 *
 */
@Component
public class SystemVariableUtils {
	
	static public String DefaultDictionaryValue = "无";
	
	static private SystemDictionaryManager systemDictionaryManager;
	
	@Autowired
	public void setSystemDictionaryManager(SystemDictionaryManager systemDictionaryManager) {
		SystemVariableUtils.systemDictionaryManager = systemDictionaryManager;
	}
	
	/**
	 * 为了能够借助Spring自动注入systemDictionaryManager这个Bean写一个空方法借助@PostConstruct注解注入
	 */
	@PostConstruct
	public void init() {
		
	}

	/**
	 * 获取数据字典名称
	 * 
	 * @param categoryCode 列别代码
	 * @param value 值
	 * 
	 * @return String
	 */
	public static String getDictionaryNameByValue(SystemDictionaryCode systemDictionaryCode,Object value) {
		
		if (value == null || systemDictionaryCode == null) {
			return DefaultDictionaryValue;
		}
		
		if (value instanceof String && StringUtils.isEmpty(value.toString())) {
			return DefaultDictionaryValue;
		}
		
		List<DataDictionary> dataDictionaries = systemDictionaryManager.getDataDictionariesByCategoryCode(systemDictionaryCode.getCode());
		
		for (Iterator<DataDictionary> iterator = dataDictionaries.iterator(); iterator.hasNext();) {
			DataDictionary dataDictionary = iterator.next();
			
			if (StringUtils.equals(dataDictionary.getValue(), value.toString())) {
				return dataDictionary.getName();
			}
		}
		return DefaultDictionaryValue; 
	}
	
	public static SecurityModel getCurrentUser() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.getPrincipal() != null && subject.getPrincipal() instanceof SecurityModel) {
			return (SecurityModel) subject.getPrincipal();
		}
		
		return null;
	}
	
	public static boolean isLogin() {
		return getCurrentUser() != null;
	}
	
}

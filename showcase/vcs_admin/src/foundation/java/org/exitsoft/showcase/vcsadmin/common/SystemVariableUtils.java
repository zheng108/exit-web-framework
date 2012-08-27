package org.exitsoft.showcase.vcsadmin.common;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.exitsoft.showcase.vcsadmin.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.vcsadmin.common.model.CommonVariableModel;
import org.exitsoft.showcase.vcsadmin.entity.foundation.DataDictionary;
import org.exitsoft.showcase.vcsadmin.service.foundation.SystemDictionaryManager;
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
	 * 为了能够借助Spring自动注入systemDictionaryManager这个Bean.写一个空方法借助@PostConstruct注解注入
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
		
		List<DataDictionary> dataDictionaries = systemDictionaryManager.getDataDictionariesByCategoryCode(systemDictionaryCode);
		
		for (Iterator<DataDictionary> iterator = dataDictionaries.iterator(); iterator.hasNext();) {
			DataDictionary dataDictionary = iterator.next();
			
			if (StringUtils.equals(dataDictionary.getValue(), value.toString())) {
				return dataDictionary.getName();
			}
		}
		return DefaultDictionaryValue; 
	}

	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典类别
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	public static List<DataDictionary> getDataDictionariesByCategoryCode(SystemDictionaryCode code, String ignoreValue) {
		return systemDictionaryManager.getDataDictionariesByCategoryCode(code, ignoreValue);
	}
	
	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典类别
	 * 
	 * @return List
	 */
	public static List<DataDictionary> getDataDictionariesByCategoryCode(SystemDictionaryCode code) {
		return systemDictionaryManager.getDataDictionariesByCategoryCode(code);
	}
	
	/**
	 * 获取当前安全模型
	 * 
	 * @return {@link SecurityModel}
	 */
	public static CommonVariableModel getCommonVariableModel() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.getPrincipal() != null && subject.getPrincipal() instanceof CommonVariableModel) {
			return (CommonVariableModel) subject.getPrincipal();
		}
		
		return null;
	}
	
	/**
	 * 判断当前会话是否登录
	 * 
	 * @return boolean
	 */
	public static boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}
	
}

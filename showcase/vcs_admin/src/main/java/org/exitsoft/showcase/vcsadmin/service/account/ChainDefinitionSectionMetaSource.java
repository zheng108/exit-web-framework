package org.exitsoft.showcase.vcsadmin.service.account;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 借助spring {@link FactoryBean} 对apache shiro的premission进行动态创建
 * 
 * @author vincent
 *
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section>{

	@Autowired
	private AccountManager accountManager;
	
	//shiro默认的链接定义
	private String filterChainDefinitions;
	
	public Section getObject() throws BeansException {
        
        List<Resource> resources = accountManager.getAllResources();
        List<Group> groups = accountManager.getAllGroup(GroupType.RoleGorup);
        
        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        //循环数据库资源的url
        for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
        	
        	Resource resource = it.next();
        	if(StringUtils.isNotEmpty(resource.getValue()) && StringUtils.isNotEmpty(resource.getPermission())) {
        		section.put(resource.getValue(), resource.getPermission());
        	}
        	
        }
        
        //循环数据库组的url
        for (Iterator<Group> it = groups.iterator(); it.hasNext();) {
        	
        	Group group = it.next();
        	if(StringUtils.isNotEmpty(group.getValue()) && StringUtils.isNotEmpty(group.getRole())) {
        		section.put(group.getValue(), group.getRole());
        	}
        	
        }
        
		return section;
	}
	
	/**
	 * 通过filterChainDefinitions对默认的链接过滤定义
	 * 
	 * @param filterChainDefinitions 默认的接过滤定义
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}


	
	public Class<?> getObjectType() {
		return this.getClass();
	}


	
	public boolean isSingleton() {
		return false;
	}
	
}

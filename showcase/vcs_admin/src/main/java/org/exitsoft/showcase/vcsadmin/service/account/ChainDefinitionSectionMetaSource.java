package org.exitsoft.showcase.vcsadmin.service.account;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
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
	
	private String filterChainDefinitions;
	
	/**
	 * 默认premission字符串
	 */
	public static final String PREMISSION_STRING="perms[\"{0}\"]";
	
	
	public Section getObject() throws BeansException {
        
        List<Resource> list = accountManager.getAllResources();
        
        Ini ini = new Ini();
        //加载默认的url
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        //循环数据库资源的url
        for (Iterator<Resource> it = list.iterator(); it.hasNext();) {
        	
        	Resource resource = it.next();
        	if(StringUtils.isNotEmpty(resource.getValue()) && StringUtils.isNotEmpty(resource.getPermission())) {
        		section.put(resource.getValue(),  MessageFormat.format(PREMISSION_STRING,resource.getPermission()));
        	}
        	
        }
        
		return section;
	}
	
	/**
	 * 通过filterChainDefinitions对默认的url过滤定义
	 * 
	 * @param filterChainDefinitions 默认的url过滤定义
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

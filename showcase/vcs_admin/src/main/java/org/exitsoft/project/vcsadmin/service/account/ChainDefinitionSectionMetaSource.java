package org.exitsoft.project.vcsadmin.service.account;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 借助spring {@link FactoryBean} 进行对apache shiro的premission动态创建
 * 
 * @author vincent
 *
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section>{

	@Autowired
	private AccountManager accountManager;
	
	private String filterChainDefinitions;
	
	public static final String PREMISSION_STRING="perms[\"{0}\"]";
	
	@Override
	public Section getObject() throws BeansException {
        
        List<Resource> list = accountManager.getAllResources();
        
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        
        for (Iterator<Resource> it = list.iterator(); it.hasNext();) {
        	
        	Resource resource = it.next();
        	if(StringUtils.isNotEmpty(resource.getValue()) && StringUtils.isNotEmpty(resource.getPermission())) {
        		section.put(resource.getValue(),  MessageFormat.format(PREMISSION_STRING,resource.getPermission()));
        	}
        	
        }
        
		return section;
	}
	
	
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}


	@Override
	public Class<?> getObjectType() {
		return this.getClass();
	}


	@Override
	public boolean isSingleton() {
		return false;
	}
	
}

package org.exitsoft.showcase.vcsadmin.service.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.showcase.vcsadmin.common.SystemVariableUtils;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.ResourceType;
import org.exitsoft.showcase.vcsadmin.dao.account.GroupDao;
import org.exitsoft.showcase.vcsadmin.dao.account.ResourceDao;
import org.exitsoft.showcase.vcsadmin.dao.account.UserDao;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.exitsoft.showcase.vcsadmin.entity.account.User;
import org.exitsoft.showcase.vcsadmin.service.ServiceException;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户管理业务逻辑
 * 
 * @author vincent
 *
 */
@Service
@Transactional
public class AccountManager {
	
	//用户数据访问
	@Autowired
	private UserDao userDao;
	
	//资源数据访问
	@Autowired
	private ResourceDao resourceDao;
	
	//组数据访问
	@Autowired
	private GroupDao groupDao;
	
	/**
	 * shrio授权缓存key
	 */
	private final String ShiroAuthorizationCache = "shiroAuthorizationCache";
	
	//------------------------------用户管理-----------------------------------//
	
	/**
	 * 更新当前用户密码
	 * 
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * 
	 * @return boolean
	 */
	public boolean updateUserPassword(String oldPassword, String newPassword) {
		User user = SystemVariableUtils.getCommonVariableModel().getUser();
		
		oldPassword = new SimpleHash("MD5", oldPassword.toCharArray()).toString();
		if(user.getPassword().equals(oldPassword)) {
			userDao.updatePassword(user.getId(),new SimpleHash("MD5",newPassword).toHex());
			user.setPassword(newPassword); 
			return true;
		}
		
		return false;
	}
	
	/**
	 * 通过id获取用户实体
	 * @param id 用户id
	 */
	public User getUser(String id) {
		return userDao.get(id);
	}
	
	/**
	 * 通过属性过滤器查询用户分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<User> searchUserPage(PageRequest request,List<PropertyFilter> filters) {
		
		return userDao.findPage(request, filters);
		
	}
	
	/**
	 * 新增用户
	 * 
	 * @param entity 用户实体
	 */
	public void insertUser(User entity) {
		if (!isUsernameUnique(entity.getUsername())) {
			throw new ServiceException("用户名已存在");
		}
		
		String password = new SimpleHash("MD5", entity.getPassword()).toHex();
		
		entity.setPassword(password);
		userDao.insert(entity);
	}
	
	/**
	 * 更新用户
	 * 
	 * @param entity 用户实体
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void updateUser(User entity) {
		userDao.update(entity);
	}
	
	/**
	 * 是否唯一的用户名 如果是返回true,否则返回false
	 * 
	 * @param username 用户名
	 * 
	 * @return boolean
	 */
	public boolean isUsernameUnique(String username) {
		return userDao.findUniqueByProperty("username", username) == null;
	}
	
	/**
	 * 删除用户
	 * 
	 * @param ids 用户id集合
	 */
	public void deleteUsers(List<String> ids) {
		userDao.deleteAll(ids);
	}

	/**
	 * 通过用户名获取用户实体
	 * 
	 * @param username 用户实体
	 * 
	 * @return {@link User}
	 */
	public User getUserByUsername(String username) {
		return userDao.findUniqueByProperty("username", username);
	}
	
	//------------------------------资源管理-----------------------------------//
	
	/**
	 * 通过id获取资源实体
	 * 
	 * @param id 资源id
	 * 
	 * @return {@link Resource}
	 */
	public Resource getResource(String id) {
		return resourceDao.get(id);
	}
	
	/**
	 * 通过id集合获取资源资源
	 * 
	 * @param ids 资源集合
	 * 
	 * @return List
	 */
	public List<Resource> getResources(List<String> ids) {
		return resourceDao.get(ids);
	}
	
	/**
	 * 通过属性过滤器查询资源分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Resource> searchResourcePage(PageRequest request,List<PropertyFilter> filters) {
		return resourceDao.findPage(request, filters);
	}
	
	/**
	 * 保存资源实体
	 * 
	 * @param entity 资源实体
	 */
	public void saveResource(Resource entity) {
		entity.setSort(resourceDao.entityCount() + 1);
		resourceDao.save(entity);
	}
	
	/**
	 * 通过资源id删除资源
	 * 
	 * @param ids 资源id集合 
	 */
	public void deleteResources(List<String> ids) {
		resourceDao.deleteAll(ids);
	}
	
	/**
	 * 获取所有菜单类型父类资源
	 * 
	 * @return List
	 */
	public List<Resource> getAllParentMenuResources() {
		return resourceDao.findByExpressions(new String[]{"EQ_S_parent.id","EQ_S_type"}, new String[]{null,ResourceType.Menu.getValue()});
	}
	
	/**
	 * 获取所有父类资源
	 * 
	 * @return List
	 */
	public List<Resource> getAllParentResources() {
		return resourceDao.findByProperty("parent.id", null);
	}
	
	/**
	 * 获取所有资源
	 * 
	 * @return List
	 */
	public List<Resource> getAllResources() {
		return getAllResources(null);
	}
	
	/**
	 * 获取所有资源
	 * 
	 * @param ignoreIdValue 要忽略的id属性值,如果是多个值，使用逗号分割
	 * 
	 * @return List
	 */
	public List<Resource> getAllResources(String ignoreIdValue) {
		
		if(StringUtils.isNotEmpty(ignoreIdValue)) {
			return resourceDao.findByExpression("NE_S_id", ignoreIdValue);
		}
		
		return resourceDao.getAll();
	}
	
	/**
	 * 通过用户id获取该用户下的所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Resource> getUserResourcesByUserId(String userId) {
		return resourceDao.findByQueryNamed(Resource.UserResources, userId);
	}
	
	/**
	 * 并合子类资源到父类中
	 * 
	 * @param list 资源集合
	 */
	public List<Resource> mergeResourcesToParent(List<Resource> list,ResourceType resourceType) {
		List<Resource> result = new ArrayList<Resource>();
		
		for (Iterator<Resource> it = list.iterator(); it.hasNext();) {
			Resource resource = it.next();
			if (resource.getParent() == null && !StringUtils.equals(resourceType.getValue(),resource.getType())) {
				mergeResourcesToParent(list,resource,resourceType);
				result.add(resource);
			}
		}
		
		return result;
	}
	
	/**
	 * 遍历list中的数据,如果数据的父类与parent相等，将数据加入到parent的children中
	 * 
	 * @param list 资源集合
	 */
	private void mergeResourcesToParent(List<Resource> list, Resource parent,ResourceType resourceType) {
		if (!parent.getLeaf()) {
			return ;
		}
		
		parent.setChildren(new ArrayList<Resource>());
		
		for (Iterator<Resource> iterator = list.iterator(); iterator.hasNext();) {
			
			Resource entity = iterator.next();
			
			if (!StringUtils.equals(entity.getType(), resourceType.getValue()) && StringUtils.equals(entity.getParentId(),parent.getId()) ) {
				entity.setChildren(null);
				mergeResourcesToParent(list,entity,resourceType);
				parent.getChildren().add(entity);
			}
			
		}
	}
	
	//------------------------------组管理-----------------------------------//
	
	/**
	 * 通过id获取组实体
	 * 
	 * @param id 组id
	 * 
	 * @return {@link Group}
	 */
	public Group getGroup(String id) {
		return groupDao.get(id);
	}
	
	/**
	 * 通过组id，获取组集合
	 * 
	 * @param ids id集合
	 * 
	 * @return List
	 */
	public List<Group> getGroups(List<String> ids) {
		return groupDao.get(ids);
	}
	
	/**
	 * 通过组类型，获取组集合
	 * 
	 * @param type 组类型，参考:{@link GroupType}
	 * 
	 * @return List
	 */
	public List<Group> getGroups(GroupType type) {
		return groupDao.findByProperty("type", type.getValue());
	}
	
	/**
	 * 保存组实体
	 * 
	 * @param entity 组实体
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void saveGroup(Group entity) {
		groupDao.save(entity);
	}
	
	/**
	 * 删除组实体
	 * @param ids 组id
	 */
	@CacheEvict(value=ShiroAuthorizationCache,allEntries=true)
	public void deleteGroups(List<String> ids) {
		groupDao.deleteAll(ids);
	}

	/**
	 * 通过属性过滤器查询组分页
	 * 
	 * @param request 分页参数
	 * @param filters 属性过滤器集合
	 * 
	 * @return {@link Page}
	 */
	public Page<Group> searchGroupPage(PageRequest request,List<PropertyFilter> filters) {
		
		return groupDao.findPage(request, filters);
	}

	/**
	 * 根据组类型获取所有组信息
	 * 
	 * @param groupType 组类型
	 * 
	 * @return List
	 */
	public List<Group> getAllGroup(GroupType groupType) {
		return getAllGroup(groupType,null);
	}
	
	/**
	 * 根据组类型获取所有组信息
	 * 
	 * @param groupType 组类型
	 * @param ignoreIdValue 要忽略的id属性值,如果是多个值，使用逗号分割
	 * 
	 * @return List
	 */
	public List<Group> getAllGroup(GroupType groupType,String ignoreIdValue) {
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		if (StringUtils.isNotEmpty(ignoreIdValue)) {
			filters.add(PropertyFilterRestrictionHolder.createPropertyFilter("NE_S_id", ignoreIdValue));
		}
		
		filters.add(PropertyFilterRestrictionHolder.createPropertyFilter("EQ_S_type", groupType.getValue()));
		
		return groupDao.findByPropertyFilters(filters);
	}

	/**
	 * 通过用户id获取所有资源
	 * 
	 * @param userId 用户id
	 * 
	 * @return List
	 */
	public List<Group> getUserGroupsByUserId(String userId) {
		return groupDao.findByQueryNamed(Group.UserGroups, userId);
	}

}

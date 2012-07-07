package org.exitsoft.showcase.vcsadmin.dao.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exitsoft.orm.core.hibernate.HibernateSuperDao;
import org.exitsoft.showcase.vcsadmin.common.enumeration.entity.GroupType;
import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.springframework.stereotype.Repository;

/**
 * 部门数据访问
 * 
 * @author vincent
 *
 */
@Repository
public class GroupDao extends HibernateSuperDao<Group, String>{

	/**
	 * 通过组类型获取组集合并且将存在父子关联的数据并合
	 * 
	 * @param type 组类型，可以参考{@link GroupType}
	 * 
	 * @return List
	 */
	public List<Group> getAllParentGroupsByType(String type) {
		List<Group> list = findByProperty("type", type);
		return mergeGroupsToParent(list);
	}
	
	/**
	 * 并合子类组到父类中
	 * 
	 * @param list 组集合
	 */
	private List<Group> mergeGroupsToParent(List<Group> list) {
		List<Group> result = new ArrayList<Group>();
			
		for (Iterator<Group> it = list.iterator(); it.hasNext();) {
			Group group = it.next();
			if (group.getParent() == null) {
				mergeGroupsToParent(list,group);
				result.add(group);
			}
		}
		
		return result;
	}
	
	/**
	 * 遍历list中的数据,如果数据的父类与parent相等，将数据加入到parent的children中
	 * 
	 * @param list 组集合
	 */
	private void mergeGroupsToParent(List<Group> list, Group parent) {
		if (parent.getLeaf()) {
			return ;
		}
		
		parent.setChildren(new ArrayList<Group>());
		
		for (Iterator<Group> iterator = list.iterator(); iterator.hasNext();) {
			
			Group entity = iterator.next();
			
			if (entity.getParent() != null && StringUtils.equals(entity.getParentId(),parent.getId()) ) {
				
				entity.setChildren(null);
				mergeGroupsToParent(list,entity);
				parent.getChildren().add(entity);
			}
			
		}
	}
}

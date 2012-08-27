package org.exitsoft.showcase.vcsadmin.common.model;

import java.io.Serializable;
import java.util.List;

import org.exitsoft.showcase.vcsadmin.entity.account.Group;
import org.exitsoft.showcase.vcsadmin.entity.account.Resource;
import org.exitsoft.showcase.vcsadmin.entity.account.User;

/**
 * 
 * 系统常用变量模型实体
 * 
 * @author vincent
 *
 */
@SuppressWarnings("serial")
public class CommonVariableModel implements Serializable {
		//当前用户
		private User user;
		
		//当前用户所在的组集合
		private List<Group> groupsList;
		
		//当前用户的授权资源集合
		private List<Resource> authorizationInfo;
		
		//当前用户的菜单集合
		private List<Resource> menusList;
		
		public CommonVariableModel() {
			
		}

		public CommonVariableModel(User user) {
			this.user = user;
		}

		public CommonVariableModel(User user, List<Group> groupsList,List<Resource> authorizationInfo, List<Resource> menusList) {
			this.user = user;
			this.groupsList = groupsList;
			this.authorizationInfo = authorizationInfo;
			this.menusList = menusList;
		}

		/**
		 * 获取当前用户
		 * 
		 * @return {@link User}
		 */
		public User getUser() {
			return user;
		}

		/**
		 * 设置当前用户
		 * 
		 * @param user 当前用户
		 */
		public void setUser(User user) {
			this.user = user;
		}

		/**
		 * 获取当前用户所在的组集合
		 * 
		 * @return List
		 */
		public List<Group> getGroupsList() {
			return groupsList;
		}
		
		/**
		 * 设置当前用户所在的组集合
		 * 
		 * @param groupsList 组集合
		 */
		public void setGroupsList(List<Group> groupsList) {
			this.groupsList = groupsList;
		}
		
		/**
		 * 获取当前用户的所有授权资源集合
		 * 
		 * @return List
		 */
		public List<Resource> getAuthorizationInfo() {
			return authorizationInfo;
		}
		
		/**
		 * 设置当前用户的所有授权资源集合
		 * 
		 * @param authorizationInfo 资源集合
		 */
		public void setAuthorizationInfo(List<Resource> authorizationInfo) {
			this.authorizationInfo = authorizationInfo;
		}
		
		/**
		 * 获取当前用户拥有的菜单集合
		 * 
		 * @return List
		 */
		public List<Resource> getMenusList() {
			return menusList;
		}

		/**
		 * 设置当前用户拥有的菜单集合
		 * 
		 * @param menusList 资源集合
		 */
		public void setMenusList(List<Resource> menusList) {
			this.menusList = menusList;
		}

		
		public String toString() {
			
			return user.getUsername();
		}
}

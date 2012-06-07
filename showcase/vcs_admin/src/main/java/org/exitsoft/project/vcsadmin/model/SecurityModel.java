package org.exitsoft.project.vcsadmin.model;

import java.io.Serializable;
import java.util.List;

import org.exitsoft.project.vcsadmin.entity.account.Group;
import org.exitsoft.project.vcsadmin.entity.account.Resource;
import org.exitsoft.project.vcsadmin.entity.account.User;

public class SecurityModel implements Serializable{

	private User user;
	
	private List<Group> groupsList;
	
	private List<Resource> authorizationInfo;
	
	private List<Resource> menusList;
	
	public SecurityModel() {
		
	}

	public SecurityModel(User user, List<Group> groupsList, List<Resource> authorizationInfo,List<Resource> menusList) {
		this.user = user;
		this.groupsList = groupsList;
		this.authorizationInfo = authorizationInfo;
		this.menusList = menusList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Group> getGroupsList() {
		return groupsList;
	}
	
	public List<Resource> getAuthorizationInfo() {
		return authorizationInfo;
	}
	
	public List<Resource> getResourcesList() {
		return menusList;
	}

	@Override
	public String toString() {
		
		return user.getUsername();
	}
	
	
}

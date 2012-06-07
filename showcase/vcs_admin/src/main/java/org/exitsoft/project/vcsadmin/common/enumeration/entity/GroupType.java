package org.exitsoft.project.vcsadmin.common.enumeration.entity;

/**
 * 组类型
 * 
 * @author vincent
 *
 */
public enum GroupType {
	
	/**
	 * 部门类型 
	 */
	Department("01","部门"),
	/**
	 * 机构类型
	 */
	Organization("02","机构"),
	/**
	 * 角色组类型
	 */
	RoleGorup("03","角色组");
	
	private GroupType(String value,String name) {
		this.value = value;
		this.name = name;
	}
	
	private String value;
	
	private String name;

	/**
	 * 获取类型值
	 * 
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获取类型名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
}

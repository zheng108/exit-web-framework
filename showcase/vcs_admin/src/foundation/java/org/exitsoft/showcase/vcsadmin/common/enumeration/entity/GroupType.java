package org.exitsoft.showcase.vcsadmin.common.enumeration.entity;

/**
 * 组类型
 * 
 * 机构类型 = Organization,
 * 部门类型 = Department,
 * 角色组 = RoleGorup,
 * 
 * @author vincent
 *
 */
public enum GroupType {

	/**
	 * 机构类型
	 */
	Organization("01","机构"),
	/**
	 * 部门类型 
	 */
	Department("02","部门"),
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

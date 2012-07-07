package org.exitsoft.showcase.vcsadmin.common.enumeration.entity;

/**
 * 资源类型枚举
 * 
 * @author vincent
 *
 */
public enum ResourceType {
	
	/**
	 * 菜单类型，该类型为用户可以见的
	 */
	Menu("01","菜单类型"),
	/**
	 * 安全类型，该类型为Spring Security拦截的并且用户不可见的
	 */
	Security("02","安全类型");
	
	ResourceType(String value,String name) {
		this.name = name;
		this.value = value;
	}
	
	private String name;
	
	private String value;
	
	/**
	 * 获取资源类型名称
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取资源类型值
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	
}

package org.exitsoft.showcase.vcsadmin.common.enumeration.entity;

/**
 * 状态枚举
 * 
 * @author vincent
 *
 */
public enum State{
	
	/**
	 * 启用
	 */
	Enable(1,"启用"),
	/**
	 * 禁用
	 */
	Disable(2,"禁用"),
	/**
	 * 删除
	 */
	Delete(3,"删除");
	
	//值
	private Integer value;
	//名称
	private String name;
	
	private State(Integer value,String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * 获取值
	 * @return Integer
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 获取名称
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
}

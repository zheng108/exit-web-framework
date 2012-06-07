package org.exitsoft.project.vcsadmin.common.enumeration;

/**
 * 字典类型枚举
 * 
 * @author vincent
 *
 */
public enum SystemDictionaryCode {
	
	/**
	 * 状态类型
	 */
	State("state"),
	
	/**
	 * 资源类型
	 */
	ResourceType("resourceType"),
	
	/**
	 * 组类型
	 */
	GroupType("groupType");
	
	private String code;
	
	private SystemDictionaryCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取类型代码
	 * 
	 * @return String
	 */
	public String getCode() {
		return this.code;
	}
}

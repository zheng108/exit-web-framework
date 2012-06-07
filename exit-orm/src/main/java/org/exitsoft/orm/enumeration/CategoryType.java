package org.exitsoft.orm.enumeration;

/**
 * 状态删除的数据类型
 * 
 * @author vincent
 *
 */
public enum CategoryType {
	
	/**
	 * String
	 */
	S(String.class),
	
	/**
	 * Boolean
	 */
	B(Boolean.class),
	
	/**
	 * Integer
	 */
	I(Integer.class);
	
	private Class<?> clazz;

	private CategoryType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * 获取值的Class
	 * @return
	 */
	public Class<?> getValue() {
		return clazz;
	}
}

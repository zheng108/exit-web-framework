package org.exitsoft.orm.core;

import java.util.Date;

/**
 * 属性数据类型
 * S代表String,I代表Integer,L代表Long, N代表Double, D代表Date,B代表Boolean
 * @author vincent
 * 
 */
public enum PropertyType {
	
	/**
	 * String
	 */
	S(String.class),
	/**
	 * Integer
	 */
	I(Integer.class),
	/**
	 * Long
	 */
	L(Long.class),
	/**
	 * Double
	 */
	N(Double.class), 
	/**
	 * Date
	 */
	D(Date.class), 
	/**
	 * Boolean
	 */
	B(Boolean.class);

	private Class<?> clazz;

	private PropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * 获取类型Class
	 * 
	 * @return Class
	 */
	public Class<?> getValue() {
		return clazz;
	}
}

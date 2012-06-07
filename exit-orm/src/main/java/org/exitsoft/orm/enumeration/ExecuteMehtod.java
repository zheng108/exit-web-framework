package org.exitsoft.orm.enumeration;

/**
 * 在转码策略时，根据该枚举对Hibernate的哪个操作进行转码
 * 
 * @author vincent
 *
 */
public enum ExecuteMehtod {
	
	/**
	 * 在插入时
	 */
	Insert,
	
	/**
	 * 在更新时
	 */
	Update,
	
	/**
	 * 在保存时（插入或更新时）
	 */
	Save;
	
}

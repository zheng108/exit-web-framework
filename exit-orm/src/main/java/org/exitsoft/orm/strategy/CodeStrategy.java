package org.exitsoft.orm.strategy;

/**
 * 编码策略接口,在orm数据对象添加获取更新时，根据实现类来对编码进行转型
 * 
 * @author vincent
 *
 */
public interface CodeStrategy {
	
	/**
	 * 通过该方法将 orm 对象的值和属性名传入进行转码
	 * 
	 * @param value 值
	 * @param propertyName 属性名称
	 */
	public Object convertCode(Object value,String propertyName);
	
}

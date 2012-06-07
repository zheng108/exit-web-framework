package org.exitsoft.orm.strategy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.exitsoft.orm.strategy.CodeStrategy;


/**
 * 需要转码的字段注解
 * 
 * @author vincent
 *
 */
@Target(ElementType.ANNOTATION_TYPE)
public @interface ConvertProperty {
	
	/**
	 * 转码的字段
	 * 
	 * @return String[]
	 */
	public String[] propertyNames();
	
	/**
	 * 转码策略实现类
	 * 
	 * @return Class
	 */
	public Class<? extends CodeStrategy> strategyClass();
}

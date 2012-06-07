package org.exitsoft.orm.strategy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.exitsoft.orm.enumeration.ExecuteMehtod;


/**
 * 对orm实体的某个字段转码复制到其他字段,在Hibernate添加或者更新操作时，会根据
 * convertPropertys配置进行转码
 * 
 * @author vincent
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertCode {
	
	/**
	 * 要转码的字段名
	 * 
	 * @return String
	 */
	public String fromProperty();
	
	/**
	 * 需要转码的字段
	 * 
	 * @see ConvertProperty
	 * @return ConvertField[]
	 */
	public ConvertProperty[] convertPropertys();
	
	/**
	 * 在Hibernate的哪个方法进行转码,默认为save
	 * 
	 * @return {@link ExecuteMehtod}
	 */ 
	public ExecuteMehtod executeMehtod() default ExecuteMehtod.Insert;
}

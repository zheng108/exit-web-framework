package org.exitsoft.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.exitsoft.orm.enumeration.CategoryType;



/**
 * Hibernate状态删除，如果在orm实体配置该注解，将不会物理删除数据，会根据该配置来进行对orm实体的update操作
 * 
 * 
 * <pre>
 * &#064;Entity
 * &#064;Table(name="tb_user")
 * &#064;StateDelete(propertyName = "state",type = CategoryType.S,value="2")
 * public class User{
 * 	private String username;
 * 	private String state;
 * 
 * 	public User() {
 * 	}
 * 	getter/setter.....
 * }
 * User user = dao.get(1);
 * dao.delete(user);
 * ----------------------------------
 * sql:update tb_user set state = ? where id = ?
 * </pre>
 * 
 * @author vincent
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StateDelete {
	
	/**
	 * 
	 * @return
	 */
	public String propertyName();
	
	public String value();
	
	public CategoryType type() default CategoryType.I;
}

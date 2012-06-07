package org.exitsoft.orm.core.hibernate.property;

import org.exitsoft.orm.core.PropertyFilter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

/**
 * 
 * 辅助{@link PropertyFilterRestrictionHolder#createPropertyFilter(String, String)}
 * 方法创建PropertyFilter后使用哪种约束条件向Hibernate的{@link Criteria}进行条件过滤查询的接口，
 * 
 * @author vincent
 *
 */
public interface PropertyCriterionBuilder {
	
	/**
	 * 获取Hibernate的约束标准
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Criterion}
	 * 
	 */
	public Criterion build(PropertyFilter filter);
	
	/**
	 * 获取Criterion标准的约束名称
	 * 
	 * @return String
	 */
	public String getRestrictionName();
}

package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.MultipleValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 在范围内的约束 (from object o where o.value between ? and ?) RestrictionName:BETWEEN
 * <p>
 * 表达式:BETWEEN_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class BetweenRestriction extends MultipleValueRestrictionSuper{

	public static final String RestrictionName = "BETWEEN";
	
	
	public String getRestrictionName() {
		return RestrictionName;
	}

	
	public Criterion buildRestriction(String propertyName, Object[] values) {
		
		if (values.length > 2) {
			throw new IllegalAccessError("范围值不能大于2,本次的值长度为:" + values.length);
		}
		
		return Restrictions.between(propertyName, values[0], values[1]);
	}

}

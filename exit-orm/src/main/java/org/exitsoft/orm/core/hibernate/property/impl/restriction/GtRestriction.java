package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 大于约束 (from object o where o.value > ?)RestrictionName:GT
 * <p>
 * 表达式:GT_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class GtRestriction extends PropertyValueRestrictionSuper{
	
	public final static String RestrictionName = "GT";
	
	
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	
	public Criterion build(String propertyName, Object value) {
		return Restrictions.gt(propertyName, value);
	}

}

package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 小于等于约束 ( from object o where o.value <= ?) RestrictionName:LR
 * <p>
 * 表达式:LE_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LeRestriction extends PropertyValueRestrictionSuper{

	public final static String RestrictionName = "LE";
	
	@Override
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	@Override
	public Criterion buildRestriction(String propertyName, Object value) {
		return Restrictions.le(propertyName, value);
	}

}

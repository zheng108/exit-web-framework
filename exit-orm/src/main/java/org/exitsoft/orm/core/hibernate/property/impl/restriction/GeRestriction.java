package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 大于约束 (from object o where o.value >= ?)RestrictionName:GE
 * <p>
 * 表达式:GE_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class GeRestriction extends PropertyValueRestrictionSuper{
	
	public final static String RestrictionName = "GE";
	
	@Override
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	@Override
	public Criterion buildRestriction(String propertyName, Object value) {
		
		return Restrictions.ge(propertyName, value);
	}

}

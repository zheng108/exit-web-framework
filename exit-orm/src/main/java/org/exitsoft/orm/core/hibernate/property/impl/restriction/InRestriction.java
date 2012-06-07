package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.MultipleValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 包含约束 (from object o where o.value in (?,?,?,?,?))RestrictionName:IN
 * <p>
 * 表达式:IN_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class InRestriction extends MultipleValueRestrictionSuper{

	public final static String RestrictionName = "IN";
	
	@Override
	public String getRestrictionName() {
		return RestrictionName;
	}
	
	@Override
	public Criterion buildRestriction(String propertyName, Object[] values) {
		return Restrictions.in(propertyName,values);
	}

}


package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.MultipleValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 不包含约束 (from object o where o.value not in (?,?,?,?,?))RestrictionName:NIN
 * <p>
 * 表达式:NIN_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class NinRestriction extends MultipleValueRestrictionSuper{

	public final static String RestrictionName = "NIN";
	
	@Override
	public String getRestrictionName() {
		
		return RestrictionName;
	}

	@Override
	public Criterion buildRestriction(String propertyName, Object[] values) {
		
		return Restrictions.not(Restrictions.in(propertyName, values));
	}

}


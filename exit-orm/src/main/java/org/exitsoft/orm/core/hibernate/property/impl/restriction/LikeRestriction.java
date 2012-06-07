package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 模糊约束 ( from object o where o.value like '%?%') RestrictionName:LIKE
 * <p>
 * 表达式:LIKE_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class LikeRestriction extends PropertyValueRestrictionSuper{

	public final static String RestrictionName = "LIKE";
	
	@Override
	public String getRestrictionName() {
		return RestrictionName;
	}

	@Override
	public Criterion buildRestriction(String propertyName, Object value) {
		return Restrictions.like(propertyName, value.toString(), MatchMode.ANYWHERE);
	}

}


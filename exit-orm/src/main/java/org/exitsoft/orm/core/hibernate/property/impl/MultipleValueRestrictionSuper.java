package org.exitsoft.orm.core.hibernate.property.impl;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.AssertUtils;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

/**
 * 对{@link PropertyFilter#getMatchValue()}的特殊情况值做处理，例如 in, not in, between的多值情况,
 * 该类值处理一种情况
 * 
 * <p>
 * 	例如:
 * </p>
 * 
 * IN_I_property = "1,2,3,4";
 * <p>
 * 会产生的sql为: property in (1,2,3,4)
 * 
 * @author vincent
 *
 */
public abstract class MultipleValueRestrictionSuper extends PropertyValueRestrictionSuper{
	
	public Object convertMatchValue(String value, Class<?> type) {
		AssertUtils.notNull(value,"值不能为空");
		String[] result = StringUtils.splitByWholeSeparator(value, ",");
		
		return  ConvertUtils.convertToObject(result,type);
	}
	
	@Override
	public Criterion build(PropertyFilter filter) {
		Object value = convertMatchValue(filter.getMatchValue(), filter.getPropertyType());
		Criterion criterion = null;
		if (filter.hasMultiplePropertyNames()) {
			Disjunction disjunction = Restrictions.disjunction();
			for (String propertyName:filter.getPropertyNames()) {
				disjunction.add(buildRestriction(propertyName,value));
			}
			criterion = disjunction;
		} else {
			criterion = buildRestriction(filter.getSinglePropertyName(),value);
		}
		return criterion;
	}
	
	@Override
	public Criterion buildRestriction(String propertyName, Object value) {
		
		return buildRestriction(propertyName, (Object[])value);
	}
	
	public abstract Criterion buildRestriction(String propertyName,Object[] values);
}

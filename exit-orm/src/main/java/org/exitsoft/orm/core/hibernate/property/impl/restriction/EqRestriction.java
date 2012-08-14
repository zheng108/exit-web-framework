package org.exitsoft.orm.core.hibernate.property.impl.restriction;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.MatchValue;
import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 等于约束 (from object o where o.value = ?) RestrictionName:EQ
 * <p>
 * 表达式:EQ_属性类型_属性名称[_OR_属性名称...]
 * </p>
 * 
 * @author vincent
 *
 */
public class EqRestriction extends PropertyValueRestrictionSuper {

	public final static String RestrictionName = "EQ";

	
	public String getRestrictionName() {
		return RestrictionName;
	}
	
	
	public MatchValue createMatchValueModel(String matchValue,Class<?> type) {
		
		MatchValue matchValueModel = super.createMatchValueModel(matchValue, type);
		for (int i = 0; i < matchValueModel.getValues().size(); i++) {
			Object value = matchValueModel.getValues().get(i);
			if (value instanceof String && StringUtils.equals(value.toString(),"null")) {
				matchValueModel.getValues().remove(i);
				matchValueModel.getValues().add(i, null);
			}
		}
		return matchValueModel;
	}
	
	
	public Criterion build(String propertyName, Object value) {
		
		if (value == null) {
			return Restrictions.isNull(propertyName);
		} else {
			return Restrictions.eq(propertyName, value);
		}
		
	}
	
}

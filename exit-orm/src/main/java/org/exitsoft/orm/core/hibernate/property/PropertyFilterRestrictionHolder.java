package org.exitsoft.orm.core.hibernate.property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.ServletUtils;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PropertyType;
import org.exitsoft.orm.core.hibernate.property.impl.PropertyValueRestrictionSuper;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.BetweenRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.EqRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.GeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.GtRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.InRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LikeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LlikeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.LtRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.NeRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.NinRestriction;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.RlikeRestriction;
import org.hibernate.criterion.Criterion;

/**
 * Hibernate属性过滤器约束持有者，帮助HibernateDao对buildCriterion方法创建相对的Criterion对象给Hibernate查询
 * 
 * @author vincent
 *
 */
public class PropertyFilterRestrictionHolder {
	
	
	private static Map<String, PropertyValueRestrictionSuper> criterionMap = new HashMap<String, PropertyValueRestrictionSuper>();
	
	static {
		PropertyValueRestrictionSuper eqRestriction = new EqRestriction();
		PropertyValueRestrictionSuper neRestriction = new NeRestriction();
		PropertyValueRestrictionSuper geRestriction = new GeRestriction();
		PropertyValueRestrictionSuper gtRestriction = new GtRestriction();
		PropertyValueRestrictionSuper inRestriction = new InRestriction();
		PropertyValueRestrictionSuper lLikeRestriction = new LlikeRestriction();
		PropertyValueRestrictionSuper leRestriction = new LeRestriction();
		PropertyValueRestrictionSuper likeRestriction = new LikeRestriction();
		PropertyValueRestrictionSuper ltRestriction = new LtRestriction();
		PropertyValueRestrictionSuper notInRestriction = new NinRestriction();
		PropertyValueRestrictionSuper rLikeRestriction = new RlikeRestriction();
		PropertyValueRestrictionSuper betweenRestriction = new BetweenRestriction();
		
		criterionMap.put(eqRestriction.getRestrictionName(), eqRestriction);
		criterionMap.put(neRestriction.getRestrictionName(), neRestriction);
		criterionMap.put(geRestriction.getRestrictionName(), geRestriction);
		criterionMap.put(inRestriction.getRestrictionName(), inRestriction);
		criterionMap.put(gtRestriction.getRestrictionName(), gtRestriction);
		criterionMap.put(lLikeRestriction.getRestrictionName(), lLikeRestriction);
		criterionMap.put(leRestriction.getRestrictionName(), leRestriction);
		criterionMap.put(likeRestriction.getRestrictionName(), likeRestriction);
		criterionMap.put(ltRestriction.getRestrictionName(), ltRestriction);
		criterionMap.put(rLikeRestriction.getRestrictionName(), rLikeRestriction);
		criterionMap.put(notInRestriction.getRestrictionName(), notInRestriction);
		criterionMap.put(betweenRestriction.getRestrictionName(), betweenRestriction);
	}
	
	/**
	 * 通过{@link PropertyFilter} 创建Hibernate约束标准
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Criterion}
	 */
	public static Criterion getCriterion(PropertyFilter filter) {
		PropertyCriterionBuilder criterionBuilder = criterionMap.get(filter.getRestrictionName());
		return criterionBuilder.build(filter);
	}
	
	/**
	 * 创建Hibernate约束标准
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束名称
	 * 
	 * @return {@link Criterion}
	 */
	public static Criterion getCriterion(String propertyName,Object value,String restrictionName) {
		PropertyValueRestrictionSuper restriction = criterionMap.get(restrictionName);
		return restriction.buildRestriction(propertyName, value);
	}
	
	/**
	 * 通过表达式和对比值创建条件过滤器集合,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilerRestriction.createrPropertyFilter(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return List
	 */
	public static List<PropertyFilter> createPropertyFilter(String[] expressions,String[] matchValues) {
		if (ArrayUtils.isEmpty(expressions) && ArrayUtils.isEmpty(matchValues)) {
			return Collections.emptyList();
		}
		
		if (expressions.length != matchValues.length) {
			throw new IllegalAccessError("expressions中的值与matchValues不匹配，matchValues的长度为:" + matchValues.length + "而expressions的长度为:" + expressions.length);
		}
		
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		
		for (int i = 0; i < expressions.length; i++) {
			filters.add(createPropertyFilter(expressions[i], matchValues[i]));
		}
		
		return filters;
	}
	
	/**
	 * 通过表达式和对比值创建条件过滤器
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	PropertyFilerRestriction.createrPropertyFilter("EQ_S_propertyName","vincent")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return {@link PropertyFilter}
	 */
	public static PropertyFilter createPropertyFilter(String expression,String matchValue) {
		
		String restrictionsName = StringUtils.substringBefore(expression, "_");
		
		if (!criterionMap.containsKey(restrictionsName)) {
			throw new IllegalAccessError("[" + expression + "]表达式找不到相应的约束名称,获取的值为:" + restrictionsName);

		}
		String classType = StringUtils.substringBetween(expression, "_");
		PropertyType propertyType = null;
		try {
			propertyType = PropertyType.valueOf(classType);
		} catch (Exception e) {
			throw new IllegalAccessError("[" + expression + "]表达式找不到相应的属性类型,获取的值为:" + classType);
		}
		
		String[] propertyNames = null;
		
		if (StringUtils.contains(expression,"_OR_")) {
			String temp = StringUtils.substringAfter(expression, restrictionsName + "_" + classType + "_");
			propertyNames = StringUtils.splitByWholeSeparator(temp, "_OR_");
		} else {
			propertyNames = new String[1];
			propertyNames[0] = StringUtils.substringAfterLast(expression, "_");
		}
		
		return new PropertyFilter(restrictionsName, propertyType, propertyNames,matchValue);
	}
	
	/**
	 * 获取Criterion的Map
	 * 
	 * @return Map
	 */
	public static Map<String, PropertyValueRestrictionSuper> getCriterionMap() {
		return criterionMap;
	}
	
	/**
	 * 设置Criterion的Map
	 * 
	 * @return Map
	 */
	public static void setCriterionMap(Map<String, PropertyValueRestrictionSuper> map) {
		criterionMap.putAll(map);
	}
	
	/**
	 * 从HttpRequest中创建PropertyFilter列表, 默认Filter属性名前缀为filter.
	 * 
	 * @see #buildFromHttpRequest(HttpServletRequest, String)
	 */
	public static List<PropertyFilter> buildFromHttpRequest(final HttpServletRequest request) {
		return buildPropertyFilter(request, "filter");
	}

	/**
	 * 从HttpRequest中创建PropertyFilter列表
	 * 
	 */
	public static List<PropertyFilter> buildPropertyFilter(final HttpServletRequest request, final String filterPrefix) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();

		// 从request中获取含属性前缀名的参数,构造去除前缀名后的参数Map.
		Map<String, Object> filterParamMap = ServletUtils.getParametersStartingWith(request, filterPrefix + "_");

		// 分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, Object> entry : filterParamMap.entrySet()) {
			String expression = entry.getKey();
			Object value = entry.getValue();
			
			String[] matchValues = null;
			
			if (value instanceof Object[]) {
				matchValues = (String[]) value;
			} else {
				matchValues = new String[]{value.toString()};
			}
			
			String matchValue = "";
			
			for (String v : matchValues) {
				matchValue += v + "_AND_";
			}
			
			matchValue = StringUtils.substringBeforeLast(matchValue, "_AND_");
			
			PropertyFilter filter = createPropertyFilter(expression, matchValue);
			filterList.add(filter);
			
		}
		return filterList;
	}
	
	/**
	 * 从Map中创建PropertyFilter列表
	 * 
	 */
	public static List<PropertyFilter> buildPropertyFilter(Map<String, String> filters) {
		List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
		// 分析参数Map,构造PropertyFilter列表
		for (Map.Entry<String, String> entry : filters.entrySet()) {
			String expression = entry.getKey();
			String matchValue = entry.getValue();
			// 如果value值为空,则忽略此filter.
			PropertyFilter filter = createPropertyFilter(expression, matchValue);
			filterList.add(filter);
			
		}
		return filterList;
	}
}

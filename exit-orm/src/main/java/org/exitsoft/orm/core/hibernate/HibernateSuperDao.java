package org.exitsoft.orm.core.hibernate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.AssertUtils;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.hibernate.property.PropertyFilterRestrictionHolder;
import org.exitsoft.orm.core.hibernate.property.impl.restriction.EqRestriction;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.CodeStrategy;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public class HibernateSuperDao<T,PK extends Serializable> extends BasicHibernateDao<T, PK>{

	public HibernateSuperDao(){
		
	}
	
	public HibernateSuperDao(Class entityClass){
		super(entityClass);
	}
	
	/**
	 * 通过表达式和对比值创建Criteria,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	createCriteria(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(String[] expressions,String[] matchValues) {
		return createCriteria(expressions, matchValues, StringUtils.EMPTY);
	}
	
	/**
	 * 通过表达式和对比值创建Criteria,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	createCriteria(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(String[] expressions,String[] matchValues,String orderBy) {
		return createCriteria(expressions, matchValues, orderBy,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值创建Criteria,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	createCriteria(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(String[] expressions,String[] matchValues,Class<?> persistentClass) {
		return createCriteria(expressions,matchValues,StringUtils.EMPTY,persistentClass);
	}
	
	/**
	 * 通过表达式和对比值创建Criteria,要求表达式与值必须相等
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	createCriteria(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(String[] expressions,String[] matchValues,String orderBy,Class<?> persistentClass) {
		List<PropertyFilter> filters = PropertyFilterRestrictionHolder.createPropertyFilter(expressions, matchValues);
		return createCriteria(filters,orderBy,persistentClass);
	}
	
	/**
	 * 根据{@link PropertyFilter}创建Criteria
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(List<PropertyFilter> filters) {
		return createCriteria(filters,StringUtils.EMPTY);
	}
	
	/**
	 * 根据{@link PropertyFilter}创建Criteria
	 * 
	 * @param filters 属性过滤器
	 * @param persistentClass orm实体Class
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(List<PropertyFilter> filters,Class<?> persistentClass) {
		return createCriteria(filters,StringUtils.EMPTY,persistentClass);
	}
	
	/**
	 * 根据{@link PropertyFilter}创建Criteria
	 * 
	 * @param filters 属性过滤器
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(List<PropertyFilter> filters,String orderBy) {
		return createCriteria(filters,orderBy, this.entityClass);
	}
	
	/**
	 * 根据{@link PropertyFilter}创建Criteria
	 * 
	 * @param filters 属性过滤器
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(List<PropertyFilter> filters,String orderBy,Class<?> persistentClass) {
		
		if(persistentClass == null) {
			persistentClass = this.entityClass;
		}
		
		Criteria criteria = createCriteria(persistentClass,orderBy);
		
		if (CollectionUtils.isEmpty(filters)) {
			return criteria;
		}
		for (PropertyFilter filter : filters) {
			criteria.add(createCriterion(filter));
		}
		return criteria;
	}
	
	/**
	 * 通过表达式和对比值创建Criterion
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	createCriterion("EQ_S_propertyName","vincent")
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return {@link Criterion}
	 */
	protected Criterion createCriterion(String expression,String matchValue) {
		PropertyFilter filter = PropertyFilterRestrictionHolder.createPropertyFilter(expression, matchValue);
		return createCriterion(filter);
	}
	
	/**
	 * 通过{@link PropertyFilter} 创建 Criterion
	 * 
	 * @param filter 属性过滤器
	 * 
	 * @return {@link Criterion}
	 */
	protected Criterion createCriterion(PropertyFilter filter) {
		AssertUtils.notNull(filter, "filter不能为空");
		return PropertyFilterRestrictionHolder.getCriterion(filter);
	}
	
	/**
	 * 根据{@link PropertyFilter} 查询全部
	 * 
	 * @param filters 属性过滤器
	 * 
	 * @return List
	 */
	public List<T> findByPropertyFilters(List<PropertyFilter> filters) {
		return findByPropertyFilters(filters,this.entityClass);
	}
	
	/**
	 * 根据{@link PropertyFilter} 查询全部
	 * 
	 * @param filters 属性过滤器
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public List<T> findByPropertyFilters(List<PropertyFilter> filters,String orderBy) {
		return findByPropertyFilters(filters,orderBy,this.entityClass);
	}
	
	/**
	 * 根据{@link PropertyFilter} 查询全部
	 * 
	 * @param filters 属性过滤器
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByPropertyFilters(List<PropertyFilter> filters,Class<?> persistentClass) {
		return findByPropertyFilters(filters,StringUtils.EMPTY,persistentClass);
	}
	
	/**
	 * 根据{@link PropertyFilter} 查询全部
	 * 
	 * @param filters 属性过滤器
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByPropertyFilters(List<PropertyFilter> filters,String orderBy,Class<?> persistentClass) {
		return createCriteria(filters, orderBy,persistentClass).list();
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
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
	public List<T> findByExpressions(String[] expressions,String[] matchValues) {
		return findByExpressions(expressions,matchValues,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"},"propertyName_asc")
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public List<T> findByExpressions(String[] expressions,String[] matchValues,String orderBy) {
		return findByExpressions(expressions,matchValues,orderBy,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"},OtherOrm.class)
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByExpressions(String[] expressions,String[] matchValues,Class<?> persistentClass) {
		return findByExpressions(expressions,matchValues,StringUtils.EMPTY,persistentClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"},"propertyName_asc",OtherOrm.class)
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByExpressions(String[] expressions,String[] matchValues,String orderBy,Class<?> persistentClass) {
		return createCriteria(expressions, matchValues, orderBy, persistentClass).list();
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpression("EQ_S_propertyName","vincent")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public List<T> findByExpression(String expression,String matchValue) {
		return findByExpression(expression,matchValue,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpression("EQ_S_propertyName","vincent","propertyName_asc")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public List<T> findByExpression(String expression,String matchValue,String orderBy) {
		return findByExpression(expression,matchValue,orderBy,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpression("EQ_S_propertyName","vincent",OtherOrm.class)
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByExpression(String expression,String matchValue,Class<?> persistentClass) {
		return findByExpression(expression,matchValue,StringUtils.EMPTY,persistentClass);
	}
	
	/**
	 * 通过表达式和对比值查询全部
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findByExpression("EQ_S_propertyName","vincent",OtherOrm.class)
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByExpression(String expression,String matchValue,String orderBy,Class<?> persistentClass) {
		return createCriteria(persistentClass, orderBy, createCriterion(expression, matchValue)).list();
	}
	

	/**
	 * 根据Criterion查询全部
	 * 
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return List
	 */
	public List<T> findByCriterion(Criterion...criterions) {
		return findByCriterion(this.entityClass,criterions);
	}

	/**
	 * 根据Criterion查询全部
	 * 
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return List
	 */
	public List<T> findByCriterion(String orderBy,Criterion...criterions) {
		return findByCriterion(this.entityClass,orderBy,criterions);
	}
	
	/**
	 * 根据Criterion查询全部
	 * 
	 * @param persistentClass orm实体Class
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return List
	 */
	public <X> List<X> findByCriterion(Class<?> persistentClass,Criterion...criterions) {
		return findByCriterion(persistentClass,StringUtils.EMPTY,criterions);
	}
	
	/**
	 * 根据Criterion查询全部
	 * 
	 * @param persistentClass orm实体Class
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return List
	 */
	public <X> List<X> findByCriterion(Class<?> persistentClass,String orderBy,Criterion...criterions) {
		return createCriteria(persistentClass, orderBy, criterions).list();
	}
	
	/**
	 * 通过HQL查询全部
	 * 
	 * @param queryString hql语句
	 * @param values 可变长度的hql值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQuery(String queryString,Object... values) {
		return createQuery(queryString, values).list();
	}
	
	/**
	 * 通过HQL查询全部
	 * 
	 * @param queryString hql语句
	 * @param values 与属性名方式的hql值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQuery(String queryString,Map<String,Object> values) {
		return createQuery(queryString, values).list();
	}
	
	/**
	 * 通过QueryName查询全部
	 * 
	 * @param queryName queryName
	 * @param values 值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryName(String queryName,Object... values) {
		return createQueryByQueryName(queryName, values).list();
	}
	
	/**
	 * 通过QueryName查询全部
	 * 
	 * @param queryName queryName
	 * @param values 属性名参数规则
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryName(String queryName,Map<String, Object> values) {
		return createQueryByQueryName(queryName, values).list();
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * 
	 * @return List
	 */
	public List<T> findByProperty(String propertyName,Object value) {
		return findByProperty(propertyName, value, EqRestriction.RestrictionName);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param restrictionName 约束名称,参考{@link PropertyCriterionBuilder}的实现类
	 * 
	 * @return List
	 */
	public List<T> findByProperty(String propertyName,Object value,String restrictionName) {
		return findByPropertyWithOrderBy(propertyName, value, StringUtils.EMPTY,restrictionName);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public List<T> findByPropertyWithOrderBy(String propertyName,Object value,String orderBy) {
		return findByPropertyWithOrderBy(propertyName, value, orderBy, EqRestriction.RestrictionName);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param restrictionName 约束名称,参考{@link PropertyCriterionBuilder}的实现类
	 * 
	 * @return List
	 */
	public List<T> findByPropertyWithOrderBy(String propertyName,Object value,String orderBy,String restrictionName) {
		return findByProperty(propertyName, value, restrictionName, this.entityClass,orderBy);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByProperty(String propertyName,Object value,Class<?> persistentClass) {
		return findByProperty(propertyName, value, EqRestriction.RestrictionName,persistentClass);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param persistentClass orm实体Class
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public <X> List<X> findByPropertyWithOrderBy(String propertyName,Object value,Class<?> persistentClass,String orderBy) {
		return findByProperty(propertyName, value, EqRestriction.RestrictionName,persistentClass,orderBy);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param restrictionName 约束名称,参考{@link PropertyCriterionBuilder}的实现类
	 * @param persistentClass orm实体Class
	 * 
	 * @return List
	 */
	public <X> List<X> findByProperty(String propertyName,Object value,String restrictionName,Class<?> persistentClass) {
		return findByProperty(propertyName, value, restrictionName, persistentClass, StringUtils.EMPTY);
	}
	
	/**
	 * 通过orm实体属性名称查询全部
	 * 
	 * @param propertyName orm实体属性名称
	 * @param value 值
	 * @param restrictionName 约束名称,参考{@link PropertyCriterionBuilder}的实现类
	 * @param persistentClass orm实体Class
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return List
	 */
	public <X> List<X> findByProperty(String propertyName,Object value,String restrictionName,Class<?> persistentClass,String orderBy) {
		Criterion criterion = PropertyFilterRestrictionHolder.getCriterion(propertyName, value, restrictionName);
		return createCriteria(persistentClass, orderBy, criterion).list();
	}
	
	/**
	 * 通过{@link PropertyFilter} 查询单个orm实体
	 * 
	 * @param filters 条件过滤器
	 * 
	 * @return Object
	 * 
	 */
	public T findUniqueByPropertyFilters(List<PropertyFilter> filters) {
		return (T)findUniqueByPropertyFilters(filters, this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询单个orm实体
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findUniqueByExpression("EQ_S_propertyName","vincent")
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return Object
	 */
	public T findUniqueByExpression(String expression,String matchValue) {
		return (T)findUniqueByExpression(expression, matchValue,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值查询单个orm实体
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findUniqueByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return Object
	 */
	public T findUniqueByExpressions(String[] expressions,String[] matchValues) {
		return (T)findUniqueByExpressions(expressions,matchValues,this.entityClass);
	}
	
	/**
	 * 通过criterion数组查询单个orm实体
	 * 
	 * @param criterions criterion数组
	 * 
	 * @return Object
	 */
	public T findUniqueByCriterions(Criterion[] criterions){
		return (T)findUniqueByCriterions(criterions,this.entityClass);
	}
	
	/**
	 * 通过{@link PropertyFilter} 查询单个orm实体
	 * 
	 * @param filters 条件过滤器
	 * @param persistentClass orm 实体Class
	 * 
	 * @return Object
	 * 
	 */
	public <X> X findUniqueByPropertyFilters(List<PropertyFilter> filters,Class<?> persistentClass) {
		return (X) createCriteria(filters, persistentClass).uniqueResult();
	}
	
	/**
	 * 通过表达式和对比值查询单个orm实体
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findUniqueByExpression("EQ_S_propertyName","vincent",OtherOrm.class)
	 * </code>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByExpression(String expression,String matchValue,Class<?> persistentClass) {
		Criterion criterion = createCriterion(expression, matchValue);
		return (X)findUniqueByCriterions(new Criterion[]{criterion}, persistentClass);
	}
	
	/**
	 * 通过表达式和对比值查询单个orm实体
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findUniqueByExpressions(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"},OtherOrm.class)
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByExpressions(String[] expressions,String[] matchValues,Class<?> persistentClass) {
		return (X)createCriteria(expressions, matchValues, StringUtils.EMPTY, persistentClass).uniqueResult();
	}

	/**
	 * 通过criterion数组查询单个orm实体
	 * 
	 * @param criterions criterion数组
	 * @param persistentClass orm实体Class
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByCriterions(Criterion[] criterions,Class<?> persistentClass){
		return (X)createCriteria(persistentClass,criterions).uniqueResult();
	}
	
	/**
	 * 通过hql查询单个orm实体
	 * 
	 * @param queryString hql
	 * @param values 可变长度的hql值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQuery(String queryString,Object... values){
		return (X)createQuery(queryString, values).uniqueResult();
	}

	/**
	 * 通过hql查询单个orm实体
	 * 
	 * @param queryString hql
	 * @param values 以属性名的hql值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQuery(String queryString,Map<String, Object> values){
		return (X)createQuery(queryString, values).uniqueResult();
	}
	
	/**
	 * 通过QueryName查询单个orm实体
	 * 
	 * @param queryName queryName
	 * @param values 值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryName(String queryName,Object... values) {
		return (X) createQueryByQueryName(queryName, values).list();
	}
	
	/**
	 * 通过QueryName查询单个orm实体
	 * 
	 * @param queryName queryName
	 * @param values 属性名参数规则
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryName(String queryName,Map<String, Object> values) {
		return (X)createQueryByQueryName(queryName, values).list();
	}
	
	/**
	 * 通过orm实体的属性名称查询单个orm实体
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * 
	 * @return Object
	 */
	public T findUniqueByProperty(String propertyName,Object value) {
		return (T)findUniqueByProperty(propertyName,value,EqRestriction.RestrictionName);
	}
	
	/**
	 * 通过orm实体的属性名称查询单个orm实体
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束名称 参考{@link PropertyCriterionBuilder}的所有实现类
	 * 
	 * @return Object
	 */
	public T findUniqueByProperty(String propertyName,Object value,String restrictionName) {
		return (T)findUniqueByProperty(propertyName,value,restrictionName,this.entityClass);
	}
	
	public <X> X findUniqueByProperty(String propertyName,Object value,Class<?> persistentClass) {
		return (X)findUniqueByProperty(propertyName,value,EqRestriction.RestrictionName,persistentClass);
	}
	
	/**
	 * 通过orm实体的属性名称查询单个orm实体
	 * 
	 * @param propertyName 属性名称
	 * @param value 值
	 * @param restrictionName 约束名称 参考{@link PropertyCriterionBuilder}的所有实现类
	 * @param persistentClass orm实体Class
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByProperty(String propertyName,Object value,String restrictionName,Class<?> persistentClass) {
		Criterion criterion = PropertyFilterRestrictionHolder.getCriterion(propertyName, value, restrictionName);
		return (X) createCriteria(persistentClass, criterion).uniqueResult();
	}
	
	/**
	 * 通过Criterion和分页请求参数获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param criterions 可变的Criterion对象
	 * 
	 * @return {@link Page}
	 */
	public Page<T> findPage(PageRequest request, Criterion... criterions) {
		return findPage(request,this.entityClass,criterions);
	}
	
	/**
	 * 通过Criterion和分页请求参数获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param persistentClass orm实体Class
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request,Class<?> persistentClass, Criterion... criterions) {
		Criteria c = createCriteria(persistentClass,criterions);
		return findPage(request,c);
	}
	
	public Page<T> findPage(PageRequest request,List<PropertyFilter> filters) {
		return findPage(request,filters,this.entityClass);
	}
	
	/**
	 * 通过{@link PropertyFilter}和分页请求参数获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param filters 属性过滤器集合
	 * @param persistentClass orm实体Class
	 * 
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request,List<PropertyFilter> filters,Class<?> persistentClass) {
		Criteria c = createCriteria(filters, persistentClass);
		return findPage(request,c);
	}
	
	/**
	 * 通过分页请求参数和表达式与对比值获取分页对象
	 *  <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findPage(request,"EQ_S_propertyName","vincent")
	 * </code>
	 * @param request 分页请求参数
	 * @param expression 表达式
	 * @param matchValue 对比值
	 * 
	 * @return {@link Page}
	 */
	public Page<T> findPage(PageRequest request,String expression,String matchValue) {
		
		return findPage(request, expression,matchValue,this.entityClass);
	}
	
	/**
	 * 通过分页请求参数和表达式与对比值获取分页对象
	 *  <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findPage(request,"EQ_S_propertyName","vincent",OtherOrm.class)
	 * </code>
	 * 
	 * @param request 分页请求参数
	 * @param expression 表达式
	 * @param matchValue 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request,String expression,String matchValue,Class<?> persistentClass) {
		Criterion criterion = createCriterion(expression, matchValue);
		Criteria criteria = createCriteria(persistentClass,criterion);
		return findPage(request, criteria);
	}
	
	/**
	 * 通过表达式和对比值获取分页对象
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findPage(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"})
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * 
	 * @return Object
	 */
	public Page<T> findPage(PageRequest request,String[] expressions,String[] matchValues) {
		return findPage(request, expressions,matchValues,this.entityClass);
	}
	
	/**
	 * 通过表达式和对比值获取分页对象
	 * <p>
	 * 	如：
	 * </p>
	 * <code>
	 * 	findPage(new String[]{"EQ_S_propertyName1","NE_I_propertyName2"},new String[]{"vincent","vincent_OR_admin"},OtherOrm.class)
	 * </code>
	 * <p>
	 * 	对比值长度与表达式长度必须相等
	 * </p>
	 * 
	 * @param expressions 表达式
	 * @param matchValues 对比值
	 * @param persistentClass orm实体Class
	 * 
	 * @return Object
	 */
	public <X> Page<X> findPage(PageRequest request,String[] expressions,String[] matchValues,Class<?> persistentClass) {
		Criteria criteria = createCriteria(expressions, matchValues, persistentClass);
		return findPage(request, criteria);
	}
	
	/**
	 * 根据分页参数与Criteria获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param c Criteria对象
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request, Criteria c) {
		AssertUtils.notNull(request, "page不能为空");

		Page<X> page = new Page<X>(request);

		if (request.isCountTotal()) {
			long totalCount = countCriteriaResult(c);
			page.setTotalItems(totalCount);
		}
		
		setPageRequestToCriteria(c, request);

		List result = c.list();
		page.setResult(result);
		
		return page;
	}
	
	/**
	 * 通过分页参数与HQL语句获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param queryString HQL语句
	 * @param values 值
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request,String queryString,Object... values) {
		
		Page<X> page = createQueryPage(request, queryString, values);
		Query q = createQuery(queryString, values);

		setPageParameterToQuery(q, request);

		List result = q.list();
		page.setResult(result);
		return page;
	}
	
	/**
	 * 通过分页参数与HQL语句获取分页对象
	 * 
	 * @param request 分页请求参数
	 * @param queryString HQL语句
	 * @param values 值
	 * 
	 * @return {@link Page}
	 */
	public <X> Page<X> findPage(PageRequest request, String queryString,Map<String,Object> values) {
		
		Page<X> page = createQueryPage(request, queryString, values);
		Query q = createQuery(queryString, values);

		setPageParameterToQuery(q, request);

		List result = q.list();
		page.setResult(result);
		return page;
	}
	
	/**
	 * 通过分页请求参数和HQL创建分页对象
	 * 
	 * @param pageRequest 分页请求参数
	 * @param queryString HQL
	 * @param values 值
	 * 
	 * @return {@link Page}
	 */
	protected <X> Page<X> createQueryPage(PageRequest pageRequest, String queryString, Object... values) {
		
		AssertUtils.notNull(pageRequest, "pageRequest不能为空");

		Page<X> page = new Page<X>(pageRequest);

		if (pageRequest.isCountTotal()) {
			long totalCount = countHqlResult(queryString, values);
			page.setTotalItems(totalCount);
		}

		if (pageRequest.isOrderBySetted()) {
			queryString = setOrderParameterToHql(queryString, pageRequest);
		}
		
		return page;
	}
	
	/**
	 * 在HQL的后面添加分页参数定义的orderBy, 辅助函数.
	 */
	protected String setOrderParameterToHql( String hql, PageRequest pageRequest) {
		StringBuilder builder = new StringBuilder(hql);
		builder.append(" order by");

		for (Sort orderBy : pageRequest.getSort()) {
			builder.append(String.format(" %s.%s %s,", DEFAULT_ALIAS,orderBy.getProperty(), orderBy.getDir()));
		}

		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();
	}
	
	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery( Query q, PageRequest pageRequest) {
		q.setFirstResult(pageRequest.getOffset());
		q.setMaxResults(pageRequest.getPageSize());
		return q;
	}
	
	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 * 
	 * @param c Hibernate Criteria
	 * @param pageRequest 分页请求参数
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria setPageRequestToCriteria( Criteria c,  PageRequest pageRequest) {
		AssertUtils.isTrue(pageRequest.getPageSize() > 0, "分页大小必须大于0");

		c.setFirstResult(pageRequest.getOffset());
		c.setMaxResults(pageRequest.getPageSize());

		if (pageRequest.isOrderBySetted()) {
			for (Sort sort : pageRequest.getSort()) {
				setOrderToCriteria(c,sort.getProperty(),sort.getDir());
			}
		}
		return c;
	}
}

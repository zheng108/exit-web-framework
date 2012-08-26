
package org.exitsoft.orm.core;

/**
 * orm条件过滤器，可以通过{@link PropertyFilterRestrictionHolder#createPropertyFilter(String, String)}创建，使用他创建可以直接
 * 写入表达式即可，相关表达式样式查看{@link PropertyCriterionBuilder}的实现类,如果直接创建查看{@link PropertyCriterionBuilder}
 * 实现类的实际restrictionName值和{@link PropertyType}枚举值,如果一个属性对比多个值可以使用逗号(,)分割
 * <p>
 * 	例子:
 * </p>
 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName"},"a,b,c");<code> 
 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where propertyName = 'a' and propertyName = 'b' and propertyName = 'c'</p>
 * 
 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a");<code> 
 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where propertyName1 = 'a' or propertyName2 = 'a'</p>
 * 
 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a,b");<code> 
 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
 * <p>sql:selete * from table where (propertyName1 = 'a' or propertyName1 = 'b') and (propertyName2 = 'a' or propertyName2 = 'b')</p>
 * 
 * @see PropertyFilterRestrictionHolder#createPropertyFilter(String, String)
 * @see PropertyFilterRestrictionHolder#createPropertyFilter(String[], String[])
 * @see PropertyCriterionBuilder
 * 
 * @author vincent
 */
public class PropertyFilter {
	
	//约束名称
	private String restrictionName;
	
	//属性类型
	private Class<?> propertyType;
	
	//属性名称
	private String[] propertyNames;
	
	//对比值
	private String matchValue;
	
	public PropertyFilter() {
		
	}
	
	/**
	 * 构造方法，可以通过{@link PropertyFilterRestrictionHolder#createPropertyFilter(String, String)}创建，使用他创建可以直接
	 * 写入表达式即可，相关表达式样式查看{@link PropertyCriterionBuilder}的实现类,如果直接创建查看{@link PropertyCriterionBuilder}
	 * 实现类的实际restrictionName值和{@link PropertyType}枚举值,如果一个属性对比多个值可以使用逗号(,)分割
	 * 
	 * <p>
	 * 	例子:
	 * </p>
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName"},"a,b,c");<code> 
	 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where propertyName = 'a' and propertyName = 'b' and propertyName = 'c'</p>
	 * 
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a");<code> 
	 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where propertyName1 = 'a' or propertyName2 = 'a'</p>
	 * 
	 * <code>PropertyFilter filter = new PropertyFilter("EQ",PropertyType.S.getValue(),new String[]{"propertyName1","propertyName2"},"a,b");<code> 
	 * <p>当使用以上filter到{@link HibernateDao#findByPropertyFilters(java.util.List)}是会产生以下sql</p>
	 * <p>sql:selete * from table where (propertyName1 = 'a' or propertyName1 = 'b') and (propertyName2 = 'a' or propertyName2 = 'b')</p>
	 * 
	 * @param restrictionName 约束名称
	 * @param propertyType 属性类型
	 * @param propertyNames 属性名称
	 * @param matchValue 对比值
	 */
	public PropertyFilter(String restrictionName, PropertyType propertyType,String[] propertyNames,String matchValue) {
		super();
		this.restrictionName = restrictionName;
		this.propertyType = propertyType.getValue();
		this.propertyNames = propertyNames;
		this.matchValue = matchValue;
	}

	/**
	 * 获取约束名称
	 * 
	 * @return String
	 */
	public String getRestrictionName() {
		return restrictionName;
	}

	/**
	 * 设置约束名称
	 * 
	 * @param restrictionName 约束名称
	 */
	public void setRestrictionName(String restrictionName) {
		this.restrictionName = restrictionName;
	}

	/**
	 * 获取属性类型
	 * 
	 * @return Class
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * 设置属性类型
	 * 
	 * @param propertyType 属性类型
	 */
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}
	
	/**
	 * 获取属性名称
	 * 
	 * @return String[]
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * 设置属性名称
	 * 
	 * @param propertyNames 属性名称
	 */
	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	/**
	 * 获取对比值
	 * 
	 * @return String
	 */
	public String getMatchValue() {
		return matchValue;
	}

	/**
	 * 设置对比值
	 * 
	 * @param matchValue 对比值
	 */
	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}
	/**
	 * 是否存在多个属性，如果是返回true,否则返回false
	 * 
	 * @return boolean
	 */
	public boolean hasMultiplePropertyNames() {
		return this.propertyNames != null || this.propertyNames.length > 1;
	}
	
	/**
	 * 获取单个属性名称
	 * 
	 * @return String
	 */
	public String getSinglePropertyName() {
		if (hasMultiplePropertyNames()) {
			throw new IllegalAccessError("PropertyFilter中存在多个propertyName:" + this.propertyNames);
		}
		return this.propertyNames[0];
	}
	
	
}

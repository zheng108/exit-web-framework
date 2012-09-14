package org.exitsoft.orm.core.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.exitsoft.common.utils.CollectionUtils;
import org.exitsoft.common.utils.ConvertUtils;
import org.exitsoft.common.utils.ReflectionUtils;
import org.exitsoft.orm.annotation.StateDelete;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.enumeration.ExecuteMehtod;
import org.exitsoft.orm.strategy.CodeStrategy;
import org.exitsoft.orm.strategy.annotation.ConvertCode;
import org.exitsoft.orm.strategy.annotation.ConvertProperty;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 
 * Hibernate基础类,包含对Hibernate的CURD和其他Hibernate操作
 * 
 * @author vincent
 *
 * @param <T> ROM对象
 * @param <PK> ORM主键ID类型
 */
public class BasicHibernateDao<T,PK extends Serializable> {
	
	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;
	
	protected final String DEFAULT_ALIAS = "X";
	
	private static Logger logger = LoggerFactory.getLogger(BasicHibernateDao.class); 
	/**
	 * 构造方法
	 */
	public BasicHibernateDao() {
		entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 构造方法
	 * 
	 * @param entityClass orm实体类型class
	 */
	public BasicHibernateDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * 设置Hibernate sessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获取Hibernate SessionFactory
	 * 
	 * @return {@link SessionFactory}
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 取得当前Session.
	 * 
	 * @return {@link Session}
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 对修改orm实体前的处理, 如果orm实体不为null返回true,并且将该orm实体进行转码,否则返回flase，
	 * 
	 * @param entity orm实体
	 * @param executeMehtods 
	 * 
	 * @return boolean
	 */
	private boolean preproModifyEntity(T entity,ExecuteMehtod... executeMehtods) {
		
		if (entity == null) {
			
			return false;
		}
		
		convertObject(entity, executeMehtods);
		
		return true;
	}
	
	
	/**
	 * 新增对象.
	 * 
	 * @param entity orm实体
	 */
	public void insert(T entity) {
		
		if (!preproModifyEntity(entity,ExecuteMehtod.Insert)) {
			return ;
		}
		getSession().save(entity);
	}
	
	/**
	 * 批量新增对象
	 * 
	 * @param list orm实体集合
	 */
	public void insertAll(List<T> list) {
		
		if (CollectionUtils.isEmpty(list)) {
			return ;
		}
		
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			insert(it.next());
		}
		
	}
	
	/**
	 * 更新对象
	 * @param entity orm实体
	 */
	public void update(T entity) {
		if (!preproModifyEntity(entity,ExecuteMehtod.Update)) {
			return ;
		}
		getSession().update(entity);
	}
	
	/**
	 * 批量更新对象
	 * @param list orm实体集合
	 */
	public void updateAll(List<T> list) {
		if (CollectionUtils.isEmpty(list)) {
			return ;
		}
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			update(it.next());
		}
	}
	
	/**
	 * 新增或修改对象
	 * 
	 * @param entity orm实体
	 */
	public void save(T entity) {
		if (!preproModifyEntity(entity, ExecuteMehtod.Save)) {
			return ;
		}
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 保存或更新全部对象
	 * 
	 * @param list orm实体集合
	 */
	public void saveAll(List<T> list) {
		
		if (CollectionUtils.isEmpty(list)) {
			return ;
		}
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			save(it.next());
		}
	}

	/**
	 * 删除对象.
	 * 
	 * @param entity 对象必须是session中的对象或含PK属性的transient对象.
	 */
	public void deleteByEntity(T entity) {
		
		if (entity == null) {
			logger.warn("要删除的对象为:null");
			return ;
		}
		
		StateDelete stateDelete = ReflectionUtils.getAnnotation(entity.getClass(),StateDelete.class);
		if (stateDelete != null) {
			Object value = ConvertUtils.convertToObject(stateDelete.value(), stateDelete.type().getValue());
			ReflectionUtils.invokeSetterMethod(entity, stateDelete.propertyName(), value);
			update(entity);
		} else {
			getSession().delete(entity);
		}
		
	}

	/**
	 * 按PK删除对象.
	 * 
	 * @param id 主键ID
	 */
	public void delete(PK id) {
		deleteByEntity(get(id));
	}

	/**
	 * 按PK批量删除对象
	 * 
	 * @param ids 主键ID集合
	 */
	public void deleteAll(List<PK> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return ;
		}
		for (Iterator<PK> it = ids.iterator(); it.hasNext();) {
			delete(it.next());
		}
		
	}
	
	/**
	 * 按orm实体集合删除对象 
	 * @param list
	 */
	public void deleteAllByEntities(List<T> list) {
		if (!CollectionUtils.isEmpty(list)) {
			return ;
		}
		
		for (Iterator<T> it = list.iterator(); it.hasNext();) {
			deleteByEntity(it.next());
		}
	}
	

	/**
	 * 按PK获取对象实体.如果找不到对象或者id为null值时，返回null,参考{@link Session#get(Class, Serializable)}
	 * 
	 * @see Session#get(Class, Serializable)
	 * 
	 * @param id 主键ID
	 * 
	 */
	public T get(PK id) {
		
		if (id == null) {
			return null;
		}
		
		return (T) getSession().get(entityClass, id);
	}
	
	/**
	 * 按PK获取对象代理.如果id为null，返回null。参考{@link Session#load(Class, Serializable)}
	 * 
	 * @see Session#load(Class, Serializable)
	 * 
	 * @param id 主键ID
	 * 
	 */
	public T load(PK id) {
		if (id == null) {
			return null;
		}
		
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 按PK列表获取对象列表.
	 * 
	 * @param ids 主键ID集合
	 * 
	 * @return List
	 */
	public List<T> get(Collection<PK> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return createCriteria(Restrictions.in(getIdName(), ids)).list();
	}
	
	/**
	 * 按PK列表获取对象列表.
	 * 
	 * @param ids 主键ID数据
	 * 
	 * @return List
	 */
	public List<T> get(PK[] ids) {
		return createCriteria(Restrictions.in(getIdName(), ids)).list();
	}

	/**
	 * 取得对象的主键名.
	 * 
	 * @return String
	 */
	public String getIdName() {
		ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}
	
	/**
	 * 获取实体名称
	 * 
	 * @return String
	 */
	public String getEntityName() {
		ClassMetadata meta = sessionFactory.getClassMetadata(entityClass);
		return meta.getEntityName();
	}

	/**
	 * 获取全部对象.
	 * 
	 * @return List
	 */
	public List<T> getAll() {
		return createCriteria().list();
	}
	
	/**
	 * 通过HQL查询全部。参数形式为命名参数形式
	 * <pre>
	 * from Object o where o.property1 = :property1 and o.property2 = :proerty2
	 * </pre>
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
	 * 通过HQL查询全部.参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
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
	 * 通过HQL查询全部.参数形式为jpa风格形式:
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryString hql语句
	 * @param values 可变长度的hql值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryUseJpaStyle(String queryString,Object... values) {
		return createQueryUseJpaStyle(queryString, values).list();
	}
	
	/**
	 * 通过queryNamed查询全部。参数形式为命名参数形式
	 * <pre>
	 * from Object o where o.property1 = :property1 and o.property2 = :proerty2
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 属性名参数规则
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryNamed(String queryNamed,Map<String, Object> values) {
		return createQueryByQueryNamed(queryNamed, values).list();
	}

	/**
	 * 通过queryNamed查询全部,参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryNamed(String queryNamed,Object... values) {
		return createQueryByQueryNamed(queryNamed, values).list();
	}
	
	/**
	 * 通过queryNamed查询全部,参数形式为jpa风格形式:
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 值
	 * 
	 * @return List
	 */
	public <X> List<X> findByQueryNamedUseJpaStyle(String queryNamed,Object... values) {
		return createQueryByQueryNamedUseJpaStyle(queryNamed, values).list();
	}
	
	/**
	 * 通过hql查询单个orm实体，参数形式为命名参数形式
	 * <pre>
	 * from Object o where o.property1 = :property1 and o.property2 = :proerty2
	 * </pre>
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
	 * 通过hql查询单个orm实体,参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
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
	 * 通过hql查询单个orm实体,参数形式为jpa风格形式:
	 * 
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryString hql
	 * @param values 以属性名的hql值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryUseJpaStyle(String queryString,Object... values){
		return (X)createQueryUseJpaStyle(queryString, values).uniqueResult();
	}

	/**
	 * 通过QueryName查询单个orm实体,参数形式为命名参数形式
	 * <pre>
	 * from Object o where o.property1 = :property1 and o.property2 = :proerty2
	 * </pre>
	 * 
	 * @param queryName queryName
	 * @param values 属性名参数规则
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryNamed(String queryNamed,Map<String, Object> values) {
		return (X)createQueryByQueryNamed(queryNamed, values).uniqueResult();
	}

	/**
	 * 通过QueryName查询单个orm实体,参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * @param queryName queryName
	 * @param values 值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryNamed(String queryNamed,Object... values) {
		return (X) createQueryByQueryNamed(queryNamed, values).uniqueResult();
	}
	
	/**
	 * 通过QueryName查询单个orm实体,该QueryName参数形式为jpa风格形式:
	 * 
	 * <pre>
	 * //该QueryName使用是jpa参数风格(问号后面带顺序值"?1")
	 * rom object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryName queryName
	 * @param values 值
	 * 
	 * @return Object
	 */
	public <X> X findUniqueByQueryNamedUseJapStyle(String queryNamed,Object... values) {
		return (X) createQueryByQueryNamedUseJpaStyle(queryNamed, values).uniqueResult();
	}
	
	/**
	 * 获取全部对象
	 * 
	 * @param orderByProperty 要排序的字段名称
	 * @param isAsc 是否顺序排序 true表示顺序排序，false表示倒序
	 * 
	 * @return List
	 */
	public List<T> getAll(String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria();
		setOrderToCriteria(c, isAsc ? Sort.ASC : Sort.DESC);
		return c.list();
	}
	
	/**
	 * 获取实体的总记录数
	 * 
	 * @return int
	 */
	public int entityCount() {
		return countHqlResult("from " + getEntityName() + " " + DEFAULT_ALIAS).intValue();
	}
	
	/**
	 * 根据Criterion可变数组创建Criteria对象
	 * 
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(Criterion... criterions) {
		return createCriteria(this.entityClass,criterions);
	}
	
	/**
	 * 根据Criterion可变数组创建Criteria对象
	 * 
	 * @param criterions 可变长度的Criterion数组
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(String orderBy,Criterion... criterions) {
		return createCriteria(this.entityClass, orderBy,criterions);
	}
	
	/**
	 * 根据Criterion可变数组创建Criteria对象
	 * 
	 * @param persistentClass orm实体class
	 * @param criterions 可变长度的Criterion数组
	 * 
	 * @return {@link Criteria}
	 */
	protected Criteria createCriteria(Class<?> persistentClass,Criterion... criterions) {
		return createCriteria(persistentClass,StringUtils.EMPTY,criterions);
	}

	/**
	 * 根据Criterion可变数组创建Criteria对象
	 * 
	 * @param persistentClass orm实体class
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 * @param criterions 可变长度的Criterion数组 
	 * 
	 * @return @return {@link Criteria}
	 */
	protected Criteria createCriteria(Class<?> persistentClass,String orderBy,Criterion... criterions) {
		
		Criteria criteria = getSession().createCriteria(persistentClass);
		
		for (Criterion criterion :criterions) {
			
			criteria.add(criterion);
		}
		setOrderToCriteria(criteria,orderBy);
		return criteria;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象,参数形式为命名参数形式:
	 * <pre>
	 * //使用的是命名参数风格
	 * from object o where o.condition = :condition
	 * </pre>
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 *            
	 * @return {@link Query}           
	 * 
	 */
	protected Query createQuery( String queryString, Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}
	
	/**
	 * 根据hql创建Hibernate Query对象，参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * @param queryString hql
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 *            
	 * @return {@link Query}
	 */
	protected Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		setQueryValues(query, values);
		return query;
	}
	
	/**
	 * 根据hql创建Hibernate Query对象，参数形式为jpa风格形式:
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 值
	 * 
	 * @return {@link Query}
	 */
	protected Query createQueryUseJpaStyle(String queryString,Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		setQueryValuesByJpaStley(query, values);
		return query;
	}
	
	/**
	 * 通过queryNamed 创建Query,参数形式为命名参数形式:
	 * <pre>
	 * //使用的是命名参数风格
	 * from object o where o.condition = :condition
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 属性名参数规则
	 * 
	 * @return {@link Query}
	 */
	protected Query createQueryByQueryNamed(String queryNamed,Map<String, Object> values) {
		Query query = getSession().getNamedQuery(queryNamed);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 通过queryNamed创建Query。参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 值
	 * 
	 * @return {@link Query}
	 */
	protected Query createQueryByQueryNamed(String queryNamed,Object... values) {
		Assert.hasText(queryNamed, "QueryNamed不能为空");
		Query query = getSession().getNamedQuery(queryNamed);
		setQueryValues(query, values);
		return query;
	}
	
	/**
	 * 根据queryNamed创建Hibernate Query对象，参数形式为jpa风格形式:
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param queryNamed queryNamed
	 * @param values 值
	 * 
	 * @return {@link Query}
	 */
	protected Query createQueryByQueryNamedUseJpaStyle(String queryNamed,Object... values) {
		Query query = getSession().getNamedQuery(queryNamed);
		setQueryValuesByJpaStley(query, values);
		return query;
	}
	
	/**
	 * 根据查询HQL与参数列表创建Query对象，数形式为命名参数形式:
	 * <pre>
	 * //使用的是命名参数风格
	 * from object o where o.condition = :condition
	 * </pre>
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 *            
	 * @return {@link Query}           
	 * 
	 */
	protected SQLQuery createSQLQuery( String queryString, Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = getSession().createSQLQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query.addEntity(entityClass);
	}

	/**
	 * 根据查询SQL与参数列表创建SQLQuery对象，参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 *            
	 * @return {@link SQLQuery}
	 */
	protected SQLQuery createSQLQuery( String queryString,  Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = getSession().createSQLQuery(queryString);
		setQueryValues(query, values);
		return query.addEntity(entityClass);
	}
	
	/**
	 * 根据查询SQL与参数列表创建SQLQuery对象，参数形式为jpa风格形式:
	 * <pre>
	 * //使用是jpa参数风格(问号后面带顺序值"?1")
	 * from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 *            
	 * @return {@link SQLQuery}
	 */
	protected SQLQuery createSQLQueryUseJpaStyle( String queryString,  Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = getSession().createSQLQuery(queryString);
		setQueryValuesByJpaStley(query, values);
		return query.addEntity(entityClass);
	}
	
	/**
	 * 通过orm实体属性名称和排序值向Criteria设置排序方式
	 * 
	 * @param criteria Criteria设置排序方式,
	 * @param property orm实体属性名称
	 * @param dir 排序方法，"asc"或"desc"
	 */
	protected void setOrderToCriteria(Criteria criteria,String property,String dir) {
		if(StringUtils.equals(dir.toLowerCase(), Sort.ASC)) {
			criteria.addOrder(Order.asc(property));
		} else {
			criteria.addOrder(Order.desc(property));
		}
	}
	
	/**
	 * 设置参数值到query的hql中,该参数是属于jpa风格的参数
	 *
	 * @param query Hibernate Query
	 * @param values 参数值可变数组
	 */
	protected void setQueryValuesByJpaStley(Query query ,Object... values) {
		
		if (ArrayUtils.isEmpty(values)) {
			return ;
		}
		
		for (Integer i = 1; i <= values.length; i++) {
			query.setParameter(i.toString(), values[i - 1]);
		}
	
	}
	
	/**
	 * 设置参数值到query的hql中,该参数是属于jdbc风格的参数
	 *
	 * @param query Hibernate Query
	 * @param values 参数值可变数组
	 */
	protected void setQueryValues(Query query ,Object... values) {
		if (ArrayUtils.isEmpty(values)) {
			return ;
		}
		
		for (Integer i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
	}
	
	/**
	 * 
	 * 将对象集合执行转码操作
	 * 
	 * @param source 要转码的对象集合
	 * @param executeMehtods 在什么方法进行转码
	 */
	protected void convertObjects(List<Object> source,ExecuteMehtod... executeMehtods) {
		for (Iterator<Object> it = source.iterator(); it.hasNext();) {
			convertObject(it.next(), executeMehtods);
		}
	}
	
	/**
	 * 
	 * 将对象执行转码操作
	 * 
	 * @param source 要转码的对象
	 * @param executeMehtods 在什么方法进行转码
	 */
	protected void convertObject(Object source,ExecuteMehtod...executeMehtods) {
		if (executeMehtods == null) {
			return ;
		}
		
		ConvertCode convertCode = ReflectionUtils.getAnnotation(source.getClass(),ConvertCode.class);
		
		if (convertCode == null) {
			return ;
		}
		
		for (ExecuteMehtod em:executeMehtods) {
			if (convertCode.executeMehtod().equals(em)) {
				for (ConvertProperty convertProperty : convertCode.convertPropertys()) {
					
					CodeStrategy strategy = ReflectionUtils.newInstance(convertProperty.strategyClass());
					
					for (String property :convertProperty.propertyNames()) {
						
						Object fromValue = ReflectionUtils.invokeGetterMethod(source, convertCode.fromProperty());
						Object convertValue = strategy.convertCode(fromValue,property);
						ReflectionUtils.invokeSetterMethod(source, property, convertValue);
						
					}
				}
			}
		}
		
	}
	
	/**
	 * 通过排序表达式向Criteria设置排序方式,
	 * @param criteria Criteria
	 * @param orderBy 排序表达式，规则为:属性名称_排序规则,如:property_asc或property_desc,可以支持多个属性排序，用逗号分割,如:"property1_asc,proerty2_desc",也可以"property"不加排序规则时默认是desc
	 */
	protected void setOrderToCriteria(Criteria criteria, String orderBy) {
		if (StringUtils.isEmpty(orderBy)) {
			return ;
		}
		
		String[] orderBys = null;
		if (StringUtils.contains(orderBy,",")) {
			orderBys = StringUtils.splitByWholeSeparator(orderBy, ",");
		} else {
			orderBys = new String[]{orderBy};
		}
		
		for (String ob: orderBys) {
		
			if (StringUtils.contains(ob, "_")) {
				String[] temp = StringUtils.splitByWholeSeparator(ob, "_");
				String property = temp[0];
				String dir = temp[1];
				if (!StringUtils.equals(dir.toLowerCase(), Sort.ASC) && !StringUtils.equals(dir.toLowerCase(), Sort.DESC)) {
					throw new IllegalAccessError("orderBy规则错误，当前为:" + dir + " 应该为:ASC或者:DESC");
				}
				setOrderToCriteria(criteria,property,dir);
			} else {
				setOrderToCriteria(criteria,ob,Sort.DESC);
			}
		}
	}
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.使用命名方式参数
	 * 
	 * <pre>
	 * 	from object o where o.property = :proprty and o.property = :proprty
	 * </pre>
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 * 
	 * @param queryString HQL
	 * @param values 值
	 * 
	 * @return long
	 */
	protected Long countHqlResult( String queryString,  Map<String, ?> values) {
		String countHql = prepareCountHql(queryString);

		try {
			return (Long)createQuery(queryString, values).uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException("hql不能自动计算总是:"+ countHql, e);
		}
	}
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.(使用jdbc方式参数)
	 * 
	 * <pre>
	 * 	from object o where o.property = ? and o.property = ?
	 * </pre>
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 * 
	 * @param queryString HQL
	 * @param values 值
	 * 
	 * @return long
	 */
	protected Long countHqlResult( String queryString,  Object... values) {
		String countHql = prepareCountHql(queryString);

		try {
			return (Long)createQuery(countHql, values).uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException("hql不能自动计算总数:"+ countHql, e);
		}
	}
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.(使用jpa风格参数)
	 * 
	 * <pre>
	 * 	from object o where o.property = ?1 and o.property = ?2
	 * </pre>
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 * 
	 * @param queryString HQL
	 * @param values 值
	 * 
	 * @return long
	 */
	protected Long countHqlResultUseJpaStyle( String queryString,  Object... values) {
		String countHql = prepareCountHql(queryString);

		try {
			return (Long)createQuery(countHql, values).uniqueResult();
		} catch (Exception e) {
			throw new RuntimeException("hql不能自动计算总数:"+ countHql, e);
		}
	}
	
	/**
	 * 绑定计算总数HQL语句,返回绑定后的hql字符串
	 * 
	 * @param orgHql hql
	 * 
	 * @return String
	 */
	private String prepareCountHql(String orgHql) {
		String countHql = "select count (*) " + removeSelect(removeOrders(orgHql));
		return countHql;
	}
	
	/**
	 * 移除from前面的select 字段 返回移除后的hql字符串
	 * @param hql 
	 * @return String
	 */
	private String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}
	
	/**
	 * 删除hql中的 orderBy的字段,返回删除后的新字符串
	 * 
	 * @param hql
	 * 
	 * @return String
	 */
	private String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * 为Query添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 * 
	 * @param query Hibernate Query 接口
	 * 
	 * @return {@link Query};
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 * 
	 * @param criteria Hibernate Criteria 接口
	 * 
	 * @return {@link Criteria}
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}
	
	/**
	 * 根据HQL创建迭代器
	 * 
	 * @param queryString hql
	 * @param values 值
	 * 
	 * @return Iterator
	 */
	public Iterator<T> iterator(String queryString,Object... values) {
		Query query = createQuery(queryString, values);
		return distinct(query).iterate();
		
	}
	
	/**
	 * 根据Criterion创建迭代器
	 * 
	 * @param criterions 可变的Criterion参数
	 * 
	 * @return Iterator
	 */
	public Iterator<T> iterator(Criterion...criterions) {
		return distinct(createCriteria(criterions)).list().iterator();
	}
	
	/**
	 * 立即关闭迭代器而不是等待session.close的时候才关闭
	 * @param it 要关闭的迭代器
	 * 
	 */
	public void closeIterator(Iterator<?> it) throws HibernateException {
		Hibernate.close(it);
	}
	
	/**
	 * 将persistable对象 Object对象、代理对象、持久化对象，Collection 对象或为空的对象里的代理属性全部加载(该方法使用了Hibernate.initialize方法)
	 * 
	 * @param proxy 一个persistable对象 Object对象、代理对象、持久化对象，Collection 对象或为空的对象
	 * 
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}
	
	/**
	 * 如果session中存在相同持久化识别的实例，用给出的对象的状态覆盖持久化实例
	 * 
	 * @param entity 持久化实例
	 */
	public void merge(T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().merge(entity);
	}
	
	/**
	 * 如果session中存在相同持久化识别的实例，用给出的对象的状态覆盖持久化实例
	 * 
	 * @param entity 持久化实例
	 * @param entityName 持久化对象名称
	 */
	public void merge(String entityName,T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().merge(entityName, entity);
	}
	
	/**
	 * 刷新操作对象
	 * 
	 * @param entity 操作对象
	 */
	public void refresh(T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().refresh(entity);
	}
	
	/**
	 * 刷新操作对象
	 * 
	 * @param entity 操作对象
	 * @param lockOptions Hibernate LockOptions
	 */
	public void refresh(T entity,LockOptions lockOptions) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		
		if (lockOptions == null) {
			refresh(entity);
		} else {
			getSession().refresh(entity, lockOptions);
		}
	}
	
	/**
	 * 把操作对象在缓存区中直接清除
	 * 
	 * @param entity 操作对象
	 */
	public void evict(T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().evict(entity);
	}
	
	/**
	 * 把session所有缓存区的对象全部清除，但不包括正在操作中的对象
	 */
	public void clear() {
		getSession().clear();
	}
	
	/**
	 * 对于已经手动给ID主键的操作对象进行insert操作
	 * 
	 * @param entity 操作对象
	 * @param replicationMode 创建策略
	 */
	public void replicate(T entity, ReplicationMode replicationMode) {
		if (!preproModifyEntity(entity) || replicationMode == null) {
			return ;
		}
		getSession().replicate(entity, replicationMode);
	}
	
	/**
	 * 对于已经手动给ID主键的操作对象进行insert操作
	 * 
	 * @param entityName 操作对象名称
	 * @param entity 操作对象
	 * @param replicationMode 创建策略
	 */
	public void replicate(String entityName,T entity, ReplicationMode replicationMode) {
		if (!preproModifyEntity(entity) || replicationMode == null) {
			return ;
		}
		getSession().replicate(entityName,entity, replicationMode);
	}
	
	/**
	 * 把一个瞬态的实例持久化，但很有可能不能立即持久化实例，可能会在flush的时候才会持久化
	 * 当它在一个transaction外部被调用的时候并不会触发insert。
	 * 
	 * @param entity 瞬态的实例
	 */
	public void persist(T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().persist(entity);
	}
	
	/**
	 * 把一个瞬态的实例持久化，但很有可能不能立即持久化实例，可能会在flush的时候才会持久化
	 * 当它在一个transaction外部被调用的时候并不会触发insert。
	 * 
	 * @param entity 瞬态的实例
	 * @param entityName 瞬态的实例名称
	 */
	public void persist(String entityName, T entity) {
		if (!preproModifyEntity(entity)) {
			return ;
		}
		getSession().persist(entityName,entity);
	}
	
	/**
	 * 从当前Session中获取一个能够操作JDBC的Connection并执行想要操作的JDBC语句
	 * 
	 * @param work Hibernate Work
	 */
	public void doWork(Work work) {
		getSession().doWork(work);
	}
	
	/**
	 * 判断entity实例是否已经与session缓存关联,是返回true,否则返回false
	 * 
	 * @param entity 实例
	 * 
	 * @return boolean
	 */
	public boolean contains(Object entity) {
		return getSession().contains(entity);
	}
	
	/**
	 * 执行HQL进行批量修改/删除操作.成功后返回更新记录数,参数形式为命名参数形式:
	 * <pre>
	 * //使用的是命名参数风格
	 * update object o set o.property = :property where o.condition = :condition
	 * </pre>
	 * 
	 * @param values 命名参数,按名称绑定.
	 *            
	 * @return int
	 */
	public int executeUpdate(String hql,  Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.成功后更新记录数,参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * update object o set o.property=? where o.condition = ?
	 * </pre>
	 * 
	 * @param values 参数值
	 *            
	 * @return int
	 */
	public int executeUpdate(String hql,  Object... values) {
		return createQuery(hql, values).executeUpdate();
	}
	
	/**
	 * 执行HQL进行批量修改/删除操作.成功后更新记录数,参数形式为jpa参数形式:
	 * <pre>
	 * //使用的是jpa参数风格(问号后面带有一个顺序值"?1")
	 * update object o set o.property=?1 where o.condition = ?2
	 * </pre>
	 * 
	 * @param values 参数值
	 *            
	 * @return int
	 */
	public int executeUpdateUseJpaStyle(String hql, Object...values) {
		return createQueryUseJpaStyle(hql, values).executeUpdate();
	}

	/**
	 * 通过queryNamed执行HQL进行批量修改/删除操作.成功后返回更新记录数,参数形式为命名参数形式:
	 * <pre>
	 * //使用的是命名参数风格
	 * update object o set o.property = :property where o.condition = :condition
	 * </pre>
	 * 
	 * @param values 命名参数,按名称绑定.
	 *            
	 * @return int
	 */
	public int executeUpdateByQueryNamed(String queryNamed,Map<String, ?> values) {
		return createQueryByQueryNamed(queryNamed, values).executeUpdate();
	}

	/**
	 * 通过queryNamed执行HQL进行批量修改/删除操作.成功后返回更新记录数,参数形式为jdbc参数形式:
	 * <pre>
	 * //使用是jdbc参数风格(问号后面没有带顺序值)
	 * update object o set o.property=? where o.condition = ?
	 * </pre>
	 * 
	 * @param values 参数值
	 *            
	 * @return int
	 */
	public int executeUpdateByQueryNamed(String queryNamed,Object... values) {
		return createQueryByQueryNamed(queryNamed, values).executeUpdate();
	}
	
	/**
	 * 通过queryNamed执行HQL进行批量修改/删除操作.成功后返回更新记录数,参数形式为jpa参数形式:
	 * <pre>
	 * //使用的是jpa参数风格(问号后面带有一个顺序值"?1")
	 * update object o set o.property=?1 where o.condition = ?2
	 * </pre>
	 * 
	 * @param values 参数值
	 *            
	 * @return int
	 */
	public int executeUpdateByQueryNamedUseJapStyle(String queryNamed,Object... values) {
		return createQueryByQueryNamedUseJpaStyle(queryNamed, values).executeUpdate();
	}
}

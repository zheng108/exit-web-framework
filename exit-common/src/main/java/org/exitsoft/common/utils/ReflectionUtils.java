
package org.exitsoft.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 反射工具类.
 * 
 * @author vincent
 */

@SuppressWarnings({"unchecked","rawtypes"})
public abstract class ReflectionUtils {

	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 调用Getter方法.
	 */
	public static <T> T invokeGetterMethod(Object o, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return (T)invokeMethod(o, getterMethodName, new Class[] {}, new Object[] {});
	}

	/**
	 * 调用Setter方法.使用value的Class来查找Setter方法.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}

	/**
	 * 调用Setter方法.
	 * 
	 * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
	}
	
	/**
	 * 判断obj参数是否存在fiedlName字段
	 * 
	 * @param obj 要判断的对象
	 * @param fieldName 字段名称
	 * 
	 * @return boolean
	 */
	public static boolean hasField(Object obj, String fieldName) {
		return getAccessibleField(obj, fieldName) != null;
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static <X> X getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("找不到字段 [" + fieldName + "] 在对象  [" + obj + "] 里");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return (X) result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("找不到字段 [" + fieldName + "] 在对象 [" + obj + "] 里");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		
		return getAccessibleField(obj.getClass(),fieldName);
	}
	
	/**
	 * 
	 * 更具类型获取o中的所有字段名称
	 * 
	 * @param o 对象CLASS
	 * @param type 要获取名称的类型
	 * 
	 * @return List
	 */
	public static List<String> getAllAccessibleFieldName(final Class o,Class type) {
		
		Assert.notNull(o, "o不能为空");
		Assert.notNull(type, "fieldName不能为空");
		
		List<String> list = new ArrayList<String>();
		
		for (Field field : o.getDeclaredFields()) {
			if (field.getType().equals(type)) {
				list.add(field.getName());
			}
		}
		
		return list;
	}
	
	/**
	 * 循环向上转型, 获取对象的DeclaredField,	 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * @param o 类型Class
	 * @param fieldName class中的字段名
	 */
	public static Field getAccessibleField(final Class o, final String fieldName) {
		Assert.notNull(o, "o不能为空");
		Assert.hasText(fieldName, "fieldName不能为空");
		for (Class<?> superClass = o; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}
	
	/**
	 * 循环向上转型, 获取对象的所有DeclaredField,	 并强制设置为可访问.
	 * 
	 * @param o Object对象
	 * 
	 * @return List
	 */
	public static List<Field> getAccessibleFields(final Object o) {
		return getAccessibleFields(o.getClass());
	}
	
	/**
	 * 循环向上转型, 获取对象的所有DeclaredField,	 并强制设置为可访问.
	 * 
	 * @param o 类型Clss
	 * 
	 * @return List
	 */
	public static List<Field> getAccessibleFields(final Class o) {
		Assert.notNull(o, "o不能为空");
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> superClass = o; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Field[] result = superClass.getDeclaredFields();
			
			if (ArrayUtils.isEmpty(result)) {
				continue;
			}
			
			for (Field field : result) {
				field.setAccessible(true);
			}
			
			CollectionUtils.addAll(fields, result);
			
		}
		return fields;
	}

	/**
	 * 对于被cglib AOP过的对象, 取得真实的Class类型.
	 */
	public static Class<?> getUserClass(Class<?> o) {
		if (o != null && o.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = o.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return o;
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况.
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("找不到 [" + methodName + "] 方法在对象 [" + obj + "] 里");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}
	
	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,Class<?>... parameterTypes) {
		return getAccessibleMethod(obj.getClass(),methodName,parameterTypes);
	}
	
	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Class o, final String methodName,Class<?>... parameterTypes) {
		Assert.notNull(o, "o不能为空");

		for (Class<?> superClass = o; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);

				method.setAccessible(true);

				return method;

			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}
	
	/**
	 * 循环向上转型, 获取对象的所有DeclaredMethod	 并强制设置为可访问.
	 * 
	 * @param o Object
	 * 
	 * @return List
	 */
	public static List<Method> getAccessibleMethods(final Object o) {
		return getAccessibleMethods(o.getClass());
	}
	
	/**
	 * 循环向上转型, 获取对象的所有DeclaredMethod	 并强制设置为可访问.
	 * 
	 * @param o 类型Clss
	 * 
	 * @return List
	 */
	public static List<Method> getAccessibleMethods(final Class o) {
		Assert.notNull(o, "o不能为空");
		List<Method> methods = new ArrayList<Method>();
		
		for (Class<?> superClass = o; superClass != Object.class; superClass = superClass.getSuperclass()) {
			Method[] result = superClass.getDeclaredMethods();
			
			if (ArrayUtils.isEmpty(result)) {
				continue;
			}
			
			for (Method method : result) {
				method.setAccessible(true);
			}
			
			CollectionUtils.addAll(methods, result);
			
		}
		
		return methods;
	}
	
	/**
	 * 判断是否该对象Class中存在注解，如果存在返回true,否则返回false
	 * @param o 目标对象Class
	 * @param annotationClass 要比较的注解
	 * @return boolean
	 */
	public static boolean hasAnnotation(Class o,Class annotationClass) {
		return o.isAnnotationPresent(annotationClass);
	}
	
	/**
	 * 获取对象中的注解
	 * @param o 目标对象Class
	 * @param annotationClass 注解
	 */
	public static <T> T getAnnotation(Class o,Class annotationClass) {
		if (!hasAnnotation(o, annotationClass)){
			return null;
		}
		return (T)o.getAnnotation(annotationClass);
	}
	
	/**
	 * 判断是否该对象中存在注解，如果存在返回true,否则返回false
	 * @param o 目标对象
	 * @param annotationClass 要比较的注解
	 * @return boolean
	 */
	public static boolean hasAnnotation(Object o,Class annotationClass) {
		return hasAnnotation(o.getClass(),annotationClass);
	}
	
	/**
	 * 获取对象中的注解
	 * @param o 目标对象
	 * @param annotationClass 注解
	 */
	public static <T> T getAnnotation(Object o,Class annotationClass) {
		
		return (T)getAnnotation(o.getClass(),annotationClass);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class，否则返回首个泛参数型类型
	 * <pre>
	 * 例如
	 * public UserDao extends HibernateDao<User>
	 * </pre>
	 *
	 * @param o 要反射的Object
	 * @return Object.clss或者T.class
	 */
	public static <T> Class<T> getSuperClassGenricType(final Class o) {
		return getSuperClassGenricType(o, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.否则返回泛参数型类型
	 * <pre>
	 * 例如
	 * public UserDao extends HibernateDao<User,Long>
	 * </pre>
	 *
	 * @param o 要反射的Object
	 * @param index 反省参数的位置
	 * 
	 * @return class
	 */
	public static Class getSuperClassGenricType(final Class o, final int index) {

		Type genType = o.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(o.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("索引: " + index + ", Size of " + o.getSimpleName() + "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(o.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}
	
	/**
	 * 通过Class创建对象
	 * @param o 目标对象Class
	 */
	public static <T> T newInstance(Class o) {
		Assert.notNull(o, "o不能为空");
		try {
			return (T) o.newInstance();
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		} 
		
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
}

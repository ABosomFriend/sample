package com.sample.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sample.annotation.Column;
import com.sample.annotation.Include;
import com.sample.test.User;


public class ClassUtil {

	/**
	 * 获取一个类中含有指定注解的成员变量集合
	 * @param obj 指定的类对象
	 * @param column  指定的Column注解
	 * @param include 指定的Include注解
	 * @return
	 */
	public static <T> List<Field> getFieldsByAnnotation(Class<T> obj, Class<? extends Annotation> column,Class<? extends Annotation> include) {

		List<Field> res  = new ArrayList<Field>();
		getFieldsByAnnotation(res,obj,column,include);
		return res;
	}


	private static <T> List<Field> getFieldsByAnnotation(List<Field> res, Class<T> obj, Class<? extends Annotation> column, Class<? extends Annotation> include) {


		Field[] fields = obj.getDeclaredFields();

		for (Field field : fields) {

			if(field.isAnnotationPresent(column)) {
				res.add(field);
			}

			//如果一个类的成员变量上面有Include这个注解的话，那么我们就要循环遍历这个注解指定的类了。
			if(field.isAnnotationPresent(Include.class)) {
				getFieldsByAnnotation(res,getIncludeAnnotationValue(field),column,include);
			}
		}

		return res;

	}

	/**
	 * 返回一个成员变量上面含有Include注解的值
	 * @see Include
	 * @param field 成员变量
	 * @return 这个成员变量上面含有Include注解的值
	 */
	private static Class<?> getIncludeAnnotationValue(Field field) {
		Include include = field.getAnnotation(Include.class);
		return include.includeClass();
	}

	/**
	 * 通过一个成员变量数组，获得此成员变量数组中含有特定注解的value()值，这里设计得不是很好
	 * 这里我们写死了，只能获得Column这个注解的值
	 * @param fields
	 * @param annotation
	 * @return
	 */
	public static List<String> getAnnotationName(List<Field> fields, Class<? extends Annotation> annotation) {

		List<String> res = new ArrayList<String>();

		if(fields != null && fields.size() > 0) {
			for(int i = 0; i < fields.size(); ++i) {
				Annotation anno = fields.get(i).getAnnotation(annotation);

				if(anno instanceof Column) {
					Column annota = (Column)anno;
					res.add(annota.value());
				}
			}
		}

		return res;
	}


	/**
	 * 调用实体类指定成员变量的setter方法
	 * @param obj 实体类
	 * @param field 成员变量
	 * @param value 设定的value值
	 */
	public static <T> void setValue(T obj ,Field field, Object value) {

		Class<?> c = obj.getClass();

		String fieldName = field.getName();
		Class<?> fieldType = field.getType();

		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		try {
			Method method = c.getMethod(methodName, fieldType);
			method.invoke(obj, value);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 通过这个类的类类型，来获得这个类的对象,注意这个类中必须要有一个无参的构造方法
	 * @param c
	 * @return
	 */
	public static <T> T getInstance(Class<T> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定的类的成员变量的值
	 * @param obj 类对象
	 * @param varName 成员变量名称
	 * @return 成员变量的值
	 */
	public static <T> Object getValue(T obj, String varName) {

		Class<?> c = obj.getClass();
		Object value = null;

		String methodName = "get" + varName.substring(0, 1).toUpperCase() + varName.substring(1);
		try {
			Method method = c.getMethod(methodName);
			value = method.invoke(obj);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return value;
	}


	/**
	 * 获得基本类型的包装类的构造方法
	 * @param c 包装类类类型
	 * @param params 包装类的构造方法的参数列表
	 * @return
	 */
	public static <T> Constructor<T> getConstructor(Class<T> c, Class<?>... params) {

		Constructor<T> constructor = null;

		try {
			constructor = c.getConstructor(params);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		return constructor;
	}

	/**
	 * 通过反射获取包装类型所对应的基本数据类型，这里我们其实是调用的api的basicDataTypeName + Value
	 * 这个方法
	 * @param dest 基本数据类型的Class对象
	 * @param src  包装类型的Class对象
	 * @param value 设置的值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBasicValue(Class<T> dest,Class<?> src, Object value) {
		//这里获取的为基本数据类型的名字
		String typeName = dest.getSimpleName();
		String methodName = typeName + "Value";
		T basicValue = null;
		Method method = null;

		try {
			method = src.getMethod(methodName);
			try {
				basicValue = (T) method.invoke(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return basicValue;
	}


	/**
	 * 判断指定的类上面是否含有指定的注解
	 * @param c 指定类的类类型
	 * @param annotation 注解的类型
	 * @return 指定的类上面有这个注解返回true，否则返回false
	 */
	public static boolean hasAnnotation(Class<?> c, Class<? extends Annotation> annotation) {
		if(c.getAnnotation(annotation) != null)
			return true;
		return false;
	}

	public static void main(String[] args) {
		List<Field> lists = getFieldsByAnnotation(User.class, Column.class, Include.class);

		for (Field field : lists) {
			System.out.println(field);
		}

		System.out.println("--------------");

		List<String> anotations = getAnnotationName(lists, Column.class);

		for (String string : anotations) {
			System.out.println(string);
		}
	}

}

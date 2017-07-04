package com.sample.types;

import java.util.ArrayList;
import java.util.List;

public class JudgeType {

	private static List<Class<?>> basicType = new ArrayList<>();

	private static List<Class<?>> packageType = new ArrayList<>();


	static {
		basicType.addAll(GenerateTypes.generateBasicTypes());
		packageType.addAll(GenerateTypes.generatePackageTypes());
	}


	/**
	 * 用来判断输入的类型是不是基本类型或者其类型的包装类型
	 * @param <T>
	 * @param type 输入类型
	 * @return 返回在集合中的位置，如果没找到返回-1
	 */
	public static <T> int judgeType(Class<T> type) {
		int index;
		if(-1 != (index = basicType.indexOf(type)) || -1 != (index = packageType.indexOf(type))) {
			return index;
		}
		return -1;
	}

	/**
	 * 判断指定的类型是不是在基本数据类型的集合中，如果是就返回集合中的index，否则返回-1
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> int judgeBasicType(Class<T> type) {
		int index;
		if(-1 != (index = basicType.indexOf(type)))
			return index;
		return -1;
	}

	/**
	 * 判断指定的类型是不是在包装类数据类型的集合中，如果是就返回集合中的index，否则返回-1
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> int judgePackageType(Class<T> type) {
		int index;
		if(-1 != (index = packageType.indexOf(type)))
			return index;
		return -1;
	}

	/**
	 * 判断指定的类型是不是在包装类数据类型的集合中，如果是就返回集合中的index，否则返回-1
	 * @return
	 */
	public static int judgePackageType(Object value) {
		return judgePackageType(value.getClass());
	}

	/**
	 * @param obj
	 * @return
	 */
	public static int judgeType(Object obj) {
		return judgeType(obj.getClass());
	}

}

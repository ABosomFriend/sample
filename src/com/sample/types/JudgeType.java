package com.sample.types;

import java.util.ArrayList;
import java.util.List;

public class JudgeType {

	private static List<Class<?>> basicType = new ArrayList<Class<?>>();

	private static List<Class<?>> packageType = new ArrayList<Class<?>>();


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
	 *
	 * @param obj
	 * @return
	 */
	public static int judgeType(Object obj) {
		Class<?> type = obj.getClass();
		return judgeType(type);
	}

}

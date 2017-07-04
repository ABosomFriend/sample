package com.sample.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * 产生相应的从数据库中获取的类类型，同时也包括了基本数据类型与其包装类类型的对应。
 * @author yizijun  created by 2017-7-3
 */
public class GenerateTypes {

	private static Class<?>[] basicTypes = {
			byte.class,
			short.class,
			int.class,
			long.class,
			float.class,
			double.class,
			double.class,
			long.class
	};

	private static Class<?>[] packageTypes = {
			Byte.class,
			Short.class,
			Integer.class,
			Long.class,
			Float.class,
			Double.class,
			BigDecimal.class,
			BigInteger.class
	};

	//数据库中转化成java对象可能的类类型
	private static Class<?>[] dateTypes = {
			Date.class,
			Time.class,
			Timestamp.class
	};

	private static Class<?> stringType = String.class;

	/**
	 * 生成基本数据类类型集合
	 * @return
	 */
	public static List<Class<?>> generateBasicTypes() {
		return Arrays.asList(basicTypes);
	}

	/**
	 * 生成基本数据的包装类类类型集合
	 * @return
	 */
	public static List<Class<?>> generatePackageTypes() {
		return Arrays.asList(packageTypes);
	}


	public static Class<?> getBasic(int index) {
		return basicTypes[index];
	}

	public static Class<?> getPackage(int index) {
		return packageTypes[index];
	}
}

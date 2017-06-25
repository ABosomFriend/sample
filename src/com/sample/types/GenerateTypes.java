package com.sample.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * 产生相应的基本数据类型和其对应的包装类型
 * @author yizijun
 *
 */
public class GenerateTypes {

	private static Class<?>[] basicTypes = {byte.class,
			short.class,
			int.class,
			long.class,
			float.class,
			double.class,
			boolean.class,
			char.class,
			double.class,
			long.class};

	private static Class<?>[] packageTypes = {Byte.class,
			Short.class,
			Integer.class,
			Long.class,
			Float.class,
			Double.class,
			Boolean.class,
			Character.class,
			BigDecimal.class,
			BigInteger.class};

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

package com.sample.util;

public class StringUtil {

	/**
	 * 判断一个字符串是否全为数字组成
	 * @deprecated
	 * @param judge
	 * @return
	 */
	public static boolean isDigital(String judge) {

		for(int i = 0; i < judge.length(); ++i) {
			if(!Character.isDigit(judge.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断该字符串是否全是由数字组成
	 * @param judge 判断的字符串
	 * @return 全是由数字组成返回true，否则返回false
	 */
	public static boolean allAreDigital(String judge) {
		if(judge.matches("\\d+"))
			return true;
		return false;
	}

	/**
	 * 判断字符串是否为null并且长度为0
	 */
	public static boolean isEmpty(String judge) {

		if(judge != null && !judge.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 *保留字符串最后一个句号的字符串
	 */
	public static String subString(String s) {

		return s.substring(s.lastIndexOf(".") + 1);

	}

	public static void main(String[] args) {
		System.out.println(allAreDigital("12124!"));
	}
}

package com.sample.database.operator;

import java.sql.ResultSet;
import java.util.List;
/**
 * 将具体的实现抽象出来
 * @author yizijun
 *
 */
public abstract class Operator {

	/**
	 * 获取具体的Object对象
	 * @param res ResultSet
	 * @param obj 指定对象的类类型
	 * @return 赋值的具体对象
	 */
	public abstract <T> T getObject(ResultSet res, Class<T> obj);
	/**
	 * 获取具体对象的List集合
	 * @param res ResultSet
	 * @param obj 指定了改对象的类类型
	 * @return 赋值后的具体对象的List集合
	 */
	public abstract <T> List<T> getList(ResultSet res, Class<T> obj);
	/**
	 * 将数据库获得的值设置到指定的对象中
	 * @param res ResultSet
	 * @param entity 对应的对象，如果是基本数据类型就没有这个对象，直接把其设置为null就行
	 */
	protected abstract <T> void setParamValues(ResultSet res, T entity);
}

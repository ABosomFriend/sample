package com.sample.database.getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.sample.annotation.Table;
import com.sample.database.DataBase;
import com.sample.database.operator.BasicOperator;
import com.sample.database.operator.ComplexOperator;
import com.sample.database.operator.Operator;
import com.sample.util.ClassUtil;

/**
 * 这个类主要是完成对相关get方法的二次封装
 * @author yizijun
 *
 * @param <T> 传入的对象的类型
 */
public class Getter<T> {

	private Connection conn;

	private String sql;

	private PreparedStatement pre;

	private ResultSet res;

	private Class<T> obj;

	private Operator operator;

	Getter() {}

	public Getter(Connection conn, String sql, Class<T> obj) {
		this.conn = conn;
		this.sql = sql;
		this.obj = obj;
	}

	/**
	 * 給PreparedStatement设置参数和得到ResultSet
	 * @param params 参数列表
	 */
	private void setPreAndRes(Object... params) {
		pre = DataBase.setParams(conn, sql, params);
		res = DataBase.executeQuery(pre);
	}

	/**
	 * 获得具体的实现类对象
	 * @param params PreparedStatement要设置的参数
	 * @return 具体的对象
	 */
	private Operator getInstance(Object... params) {

		setPreAndRes(params);
		/*
		 * 判断是基本数据，还是实体类，然后在调用不同的方法
		 */
		if(!ClassUtil.hasAnnotation(obj, Table.class)) {
			operator = new BasicOperator();
		} else {
			operator = new ComplexOperator(res,obj);
		}
		return operator;
	}


	/**
	 * 获取具体的对象
	 * @param params PreparedStatement要设置的参数
	 * @return 具体的对象
	 */
	public T getObject(Object... params) {

		Operator operator = getInstance(params);
		return operator.getObject(res, obj);

	}
	/**
	 * 获得具体对象的List集合
	 * @param params PreparedStatement要设置的参数
	 * @return 具体对象的List集合
	 */
	public List<T> getList(Object... params) {

		Operator operator = getInstance(params);
		return operator.getList(res, obj);

	}

}

package com.sample.database;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.sample.database.getter.Getter;
import com.sample.util.ClassUtil;
import com.sample.util.DBCPUtil;
import com.sample.util.StringUtil;

/**
 * 与数据库之间的相关的操作,同时也是用户直接进行操作的类
 * @author yizijun
 *
 */
public class DataBase {

	/**
	 * 设置sql语句的参数
	 * @param conn  数据库连接
	 * @param sql   sql语句
	 * @param params  参数列表
	 * @return 已给PreparedStatement对象赋过参数的PreparedStatement
	 */
	public static PreparedStatement setParams(Connection conn, String sql,
											  Object... params) {
		if(null == conn) {
			conn = DBCPUtil.getConnection();
		}

		PreparedStatement pre = null;

		try {
			pre = conn.prepareStatement(sql);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; ++i) {
					pre.setObject(i + 1, params[i]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pre;
	}

	/**
	 * 执行查询语句的执行
	 *
	 * @param pre 已经设置好预编译参数的PreparedStatement
	 * @return 查询完毕的ResultSet
	 */
	public static ResultSet executeQuery(PreparedStatement pre) {

		ResultSet res = null;
		try {
			res = pre.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取此 ResultSet 对象的列的编号、类型和属性。 也就是获取元数据
	 *
	 * @param res 结果集
	 * @return  元数据
	 */
	public static ResultSetMetaData getMetaData(ResultSet res){
		ResultSetMetaData metaData = null;
		try {
			metaData = res.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return metaData;
	}


	/**
	 * 获取数据库中指定列名的值。
	 * @param res ResultSet
	 * @param columnName 列名
	 * @return 所对应的值
	 */
	public static Object getValue(ResultSet res, String columnName) {
		Object value = null;
		try {
			value = res.getObject(columnName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}


	/**
	 * 获得一个查询过后的对象，可以对应相关的javaBean类型，同时也可以是基本数据类型
	 * @param conn 数据库连接
	 * @param sql sql语句
	 * @param obj 所得到的对象的类类型
	 * @param params 设置的参数
	 * @return obj参数指定的类类型的对象
	 * @throws SQLException
	 */
	public static <T> T getObject(Connection conn, String sql, Class<T> obj,
								  Object... params) throws SQLException {
		Getter<T> get = new Getter<>(conn,sql,obj);
		return get.getObject(params);
}

	/**
	 * 通过特殊的查询获得一个List集合，可以对应相关的javaBean类型，同时也可以是基本数据类型
	 * @param conn 数据库连接
	 * @param sql sql语句
	 * @param obj 所得到的对象的类类型
	 * @param params 设置的参数
	 * @return obj参数指定的类类型的对象所封装起来的List
	 * @throws SQLException
	 */
	public static <T> List<T> getList(Connection conn, String sql,
									  Class<T> obj, Object... params) throws SQLException {

		Getter<T> get = new Getter<>(conn,sql,obj);
		return get.getList(params);
	}

	/**
	 *
	 * 执行增删改都可以用这个方法。
	 @param conn 数据库连接
	  * @param sql sql语句
	 * @param params 设置的参数
	 * @return true表示执行成功，false表示执行失败
	 * @throws SQLException
	 */
	public static boolean modify(Connection conn, String sql, Object... params) {
		PreparedStatement pre = setParams(conn, sql, params);
		try {
			pre.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据批量插入到数据库中
	 * @param conn 数据库连接
	 * @param list 将要插入的对象封装起来的一个list
	 * @param sql  对应的sql语句
	 * @param varNames 对应的预编译参数的成员变量名称集合
	 * @return 当所有的条数都插入进数据库视为成功，成功返回true，失败返回flase
	 */
	public static <T> boolean addBatch(Connection conn,List<T> list,String sql,String... varNames) {

		PreparedStatement pre = null;
		boolean result = false;
		try {
			pre = conn.prepareStatement(sql);
			conn.setAutoCommit(false);

			if(list != null && list.size() > 0) {
				for (T t : list) {
					for (int i = 0; i < varNames.length; ++i) {
						Object value = ClassUtil.getValue(t, varNames[i]);
						pre.setObject(i + 1, value);
					}
					pre.addBatch();
				}
			}

			if(list.size() == pre.executeBatch().length) {
				conn.commit();
				result = true;
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过反射来获取数据表中的值
	 * @param res ResultSet
	 * @param columnName  列名
	 * @param ColumnClassName 列名的Class类型字符串表示形式
	 * @return
	 */
	public static Object getColumnValue(ResultSet res, String columnName, String ColumnClassName) {

		Class<?> resClass = res.getClass();
		String methodName = "get" + StringUtil.subString(ColumnClassName);

		System.out.println("method = " + methodName);

		Object value = null;
		try {
			//我们都是通过列名来获取值的，所以第二个参数我们可以固定为String.class
			Method method = resClass.getMethod(methodName, String.class);
			value = method.invoke(res, columnName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return value;

	}
}


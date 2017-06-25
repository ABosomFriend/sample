package com.sample.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBCPUtil {

	private static final Log log = LogFactory.getLog(DBCPUtil.class);

	private static final String configFile = "/db.properties";

	private static DataSource dataSource;

	static {
		// Properties 类表示了一个持久的属性集。Properties 可保存在流中或从流中加载。属性列表中每个键及其对应值都是一个字符串。
		Properties dbProperties = new Properties();
		try {
			/*
			 * InputStream getResourceAsStream(String fileName) 返回读取指定资源的输入流。
			 * void load(InputStream inStream) 从输入流中读出属性列表
			 */
			dbProperties.load(DBCPUtil.class.getResourceAsStream(configFile));

			// 然后用dbcp给出的工厂创建一个DataSource
			dataSource = BasicDataSourceFactory.createDataSource(dbProperties);
		} catch (Exception e) {
			log.error("初始化连接池失败：" + e);
		}
	}

	private DBCPUtil() {

	}

	/**
	 * 获取连接，用完后记得关闭
	 *
	 * @see DBCPUtil#closeConnection(Connection)
	 * @return 一个数据库连接
	 */
	public static final Connection getConnection() {

		Connection conn = null;

		try {
			conn = dataSource.getConnection();

			// 并且当我db.properties中设置了defalutAutoCommit = false
			// 这里获取的也为true；
			// 当我们这里的参数设置为了false，我们修改数据的时候就不会自动提交了
		} catch (SQLException e) {
			log.error("获取数据库连接失败：" + e);
		}
		return conn;
	}

	/**
	 * 关闭数据库的ResultSet和PreparedStatement连接，如果没有用到指定的参数， 则设为null
	 *
	 * @deprecated
	 * @param res ResultSet
	 * @param pre PreparedStatement
	 */
	public static void closeResAndPre(ResultSet res, PreparedStatement pre) {
		try {
			if (res != null) {
				res.close();
			}
			if (pre != null) {
				pre.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接
	 *
	 * @param conn
	 *            需要关闭的连接
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

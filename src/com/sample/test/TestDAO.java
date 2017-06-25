package com.sample.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sample.database.DataBase;
import com.sample.util.DBCPUtil;

public class TestDAO {

	public List<User> getAllUser() {
		
		Connection conn = DBCPUtil.getConnection();
		List<User> list = null;
		String sql = "select id,username,password,sex,phone,birthday from test";
		try {
			list = DataBase.getList(conn, sql, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.closeConnection(conn);
		}
		return list;
	}
	
	public int getAllNotes() {
		Connection conn = DBCPUtil.getConnection();
		String sql = "select count(*) from test";
		long notes = 0;
		
		try {
			notes = DataBase.getObject(conn, sql, long.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (int) notes;
	}
	
	public  List<Short> getAllId() {
		Connection conn = DBCPUtil.getConnection();
		String sql = "select id from test";
		List<Short> list = null;
		
		
		try {
			list = DataBase.getList(conn, sql, short.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCPUtil.closeConnection(conn);
		}
		
		return list;
	}
	
	
	public double getMaxPrice() {
		Connection conn = DBCPUtil.getConnection();
		String sql = "select max(price) from test";
		double maxPrice = 0.0;
		
		try {
			maxPrice = DataBase.getObject(conn, sql, double.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxPrice;
		
	}
	
	
	public static void main(String[] args) {

	}
}

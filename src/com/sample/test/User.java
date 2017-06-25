package com.sample.test;

import java.util.Date;

import com.sample.annotation.Column;
import com.sample.annotation.Include;
import com.sample.annotation.Table;

@Table("user")
public class User {
	
	@Column("id")
	private int id;
	
	@Column("username")
	private String name;
	
	@Column("password")
	private String password;
	
	@Column("sex")
	private String sex;
	
	@Column("phone")
	private String phone;
	
	@Column("birthday")
	private Date birthday;
	
	@Include(includeClass = Book.class)
	private Book book;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ ", sex=" + sex + ", phone=" + phone + ", birthday="
				+ birthday + "]";
	}
	
	

}

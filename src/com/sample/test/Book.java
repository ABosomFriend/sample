package com.sample.test;

import com.sample.annotation.Column;

public class Book {
	
	@Column("bookId")
	private int bookId;
	
	@Column("bookName")
	private String bookName;
	
	@Column("price")
	private double price;
	
	@Column("bookIntroduce")
	private String bookIntroduce;

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBookIntroduce() {
		return bookIntroduce;
	}

	public void setBookIntroduce(String bookIntroduce) {
		this.bookIntroduce = bookIntroduce;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", bookName=" + bookName + ", price="
				+ price + ", bookIntroduce=" + bookIntroduce + "]";
	}
	
	

}

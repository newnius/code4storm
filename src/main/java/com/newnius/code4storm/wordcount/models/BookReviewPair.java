package com.newnius.code4storm.wordcount.models;

import java.io.Serializable;

public class BookReviewPair implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3660392583133225896L;
	private Review review;
	private Book book;
	
	
	
	public BookReviewPair(Review review, Book book) {
		super();
		this.review = review;
		this.book = book;
	}
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	
}

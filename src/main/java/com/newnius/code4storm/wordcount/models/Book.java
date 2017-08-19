package com.newnius.code4storm.wordcount.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Book implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String asin;
	private String title;
	private String imgUrl;
	private ArrayList<ArrayList<String>> categories;
	private HashMap<String, Integer> salesRank;
	private String description;
	
	public Book(String asin) {
		super();
		this.asin = asin;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}


	public ArrayList<ArrayList<String>> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<ArrayList<String>> categories) {
		this.categories = categories;
	}

	public HashMap<String, Integer> getSalesRank() {
		return salesRank;
	}

	public void setSalesRank(HashMap<String, Integer> salesRank) {
		this.salesRank = salesRank;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}

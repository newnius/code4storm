package com.newnius.code4storm.marmot.model;

import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book implements Serializable{

	public static final int RELATE_TYPE_ALSO_VIEWED = 0;
	public static final int RELATE_TYPE_BUY_AFTER_VIEWING = 1;
	public static final int RELATE_TYPE_ALSO_BOUGHT = 2;
	public static final int RELATE_TYPE_BOUGHT_TOGETHER = 3;
	public static final int RELATE_TYPE_DEFAULT = 4;

	private static final long serialVersionUID = 1L;
	
	private String asin;
	private String title;
	private String imUrl;
	private List<List<String>> categories;
	private HashMap<String, Integer> salesRank;
	private String description;
	private Float price;
	private Map<String, List<String>> related;

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
	
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

	public String getImUrl() {
		return imUrl;
	}

	public void setImUrl(String imUrl) {
		this.imUrl = imUrl;
	}

	public List<List<String>> getCategories() {
		return categories;
	}

	public void setCategories(List<List<String>> categories) {
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

	public Map<String, List<String>> getRelated() {
		return related;
	}

	public void setRelated(Map<String, List<String>> related) {
		this.related = related;
	}

	public static int getRelateType(String relateType){
		switch(relateType){
			case "also_viewed":
				return RELATE_TYPE_ALSO_VIEWED;
			case "also_bought":
				return RELATE_TYPE_ALSO_BOUGHT;
			case "bought_together":
				return RELATE_TYPE_BOUGHT_TOGETHER;
			case "buy_after_viewing":
				return RELATE_TYPE_BUY_AFTER_VIEWING;
			default:
				LoggerFactory.getLogger(Book.class).info("New relate type found: " + relateType);
				return RELATE_TYPE_DEFAULT;
		}
	}
}

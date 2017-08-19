package com.newnius.code4storm.wordcount.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Review implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String reviewerId;
	private String asin;
	private String reviewerName;
	private ArrayList<Integer> helpful;
	private String reviewText;
	private double overall;
	private String summary;
	private long unixReviewTime;
	private String reviewTime;

	public String getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public ArrayList<Integer> getHelpful() {
		return helpful;
	}

	public void setHelpful(ArrayList<Integer> helpful) {
		this.helpful = helpful;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public double getOverall() {
		return overall;
	}

	public void setOverall(double overall) {
		this.overall = overall;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getUnixReviewTime() {
		return unixReviewTime;
	}

	public void setUnixReviewTime(long unixReviewTime) {
		this.unixReviewTime = unixReviewTime;
	}

	public String getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}

}

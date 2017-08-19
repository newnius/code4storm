package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 9/28/16.
 *
 */
public class Reviewer implements Serializable{
    private static final long serialVersionUID = -1931500582292225678L;
    private String reviewerID;
    private String reviewerName;

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
}

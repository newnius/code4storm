package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 10/25/16.
 *
 */
public class BookCategoryPair implements Serializable{
    private static final long serialVersionUID = 6315587568013179481L;
    private String asin;
    private int categoryID;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}

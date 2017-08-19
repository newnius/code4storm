package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 9/28/16.
 *
 */
public class Category implements Serializable{

    private static final long serialVersionUID = -3172078510358336252L;

    private int categoryID;
    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

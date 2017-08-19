package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 9/28/16.
 *
 */
public class RankList implements Serializable{
    private static final long serialVersionUID = -7332134833316831547L;
    private int listID;
    private String name;


    public RankList() {
    }

    public int getListID() {
        return listID;
    }

    public RankList setListID(int listID) {
        this.listID = listID;
        return this;
    }

    public String getName() {
        return name;
    }

    public RankList setName(String name) {
        this.name = name;
        return this;
    }
}

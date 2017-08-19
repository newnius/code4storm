package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 10/25/16.
 *
 */
public class BookRankListPair implements Serializable{
    private static final long serialVersionUID = -8023012266020140442L;
    private String asin;
    private int listID;
    private int rank;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

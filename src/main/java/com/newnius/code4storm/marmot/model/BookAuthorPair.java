package com.newnius.code4storm.marmot.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by newnius on 10/25/16.
 *
 */
public class BookAuthorPair implements Serializable{
    private static final long serialVersionUID = 631558756813179481L;
    private List<String> book_id;
    private int authorID;
    private String book_author;


    public List<String> getBook_id() {
        return book_id;
    }

    public void setBook_id(List<String> book_id) {
        this.book_id = book_id;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }
}

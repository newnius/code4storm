package com.newnius.code4storm.dblp;

import org.jdom2.Element;

import java.util.List;
import java.util.Map;

/**
 * Created by newnius on 4/26/17.
 *
 */
public class Inproceedings {
    private String mdate;
    private String key;
    private List<String> authors;
    private Map<String, String> cites;
    private List<Element> authorsEle;
    private List<Element> citesEle;
    private String title;
    private String pages;
    private String year;
    private String bookTitle;
    private String ee;
    private String crossRef;
    private String url;
    private String cdrom;

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Map<String, String> getCites() {
        return cites;
    }

    public void setCites(Map<String, String> cites) {
        this.cites = cites;
    }

    public List<Element> getAuthorsEle() {
        return authorsEle;
    }

    public void setAuthorsEle(List<Element> authorsEle) {
        this.authorsEle = authorsEle;
    }

    public List<Element> getCitesEle() {
        return citesEle;
    }

    public void setCitesEle(List<Element> citesEle) {
        this.citesEle = citesEle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getCrossRef() {
        return crossRef;
    }

    public void setCrossRef(String crossRef) {
        this.crossRef = crossRef;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCdrom() {
        return cdrom;
    }

    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }
}

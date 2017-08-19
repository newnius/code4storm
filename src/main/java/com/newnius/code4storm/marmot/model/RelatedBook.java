package com.newnius.code4storm.marmot.model;

import java.io.Serializable;

/**
 * Created by newnius on 10/25/16.
 *
 */
public class RelatedBook implements Serializable{
    public static final int RELATE_TYPE_ALSO_VIEWED = 0;
    public static final int RELATE_TYPE_BUY_AFTER_VIEWING = 1;
    public static final int RELATE_TYPE_ALSO_BOUGHT = 2;
    public static final int RELATE_TYPE_BOUGHT_TOGETHER = 3;
    public static final int RELATE_TYPE_DEFAULT = 4;
    private static final long serialVersionUID = 6009988315960233676L;

    private String asin;
    private String relatedBookAsin;
    private int relatedType;


    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getRelatedBookAsin() {
        return relatedBookAsin;
    }

    public void setRelatedBookAsin(String relatedBookAsin) {
        this.relatedBookAsin = relatedBookAsin;
    }

    public int getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(int relatedType) {
        this.relatedType = relatedType;
    }
}

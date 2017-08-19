package com.newnius.code4storm.marmot.util;

/**
 * 
 * Often use with {@code CRCallback} and {@code CRBackgroundTask} to finish a background task 
 * 
 * @author Newnius
 * @version 0.1.0(General)
 * Dependencies
 *  com.newnius.util.CRMsg
 * Created by newnius on 16-4-23.
 */
public interface CRWhatToDo {
    CRMsg doThis();
}

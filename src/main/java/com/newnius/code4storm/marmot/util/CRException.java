package com.newnius.code4storm.marmot.util;

/**
 * @author Newnius
 * @version 0.1.0(General)
 */
public class CRException extends Exception{
    private static final long serialVersionUID = 1L;
    private Exception exception;

    public CRException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return exception.getMessage();
    }

    @Override
    public void printStackTrace() {
        exception.printStackTrace();
    }
}

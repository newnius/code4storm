package com.newnius.code4storm.marmot.util;

import java.security.MessageDigest;

/**
 *
 * Created by newnius on 16-4-19.
 */
public class HashMethods {
    public static String md5(String str){
        return md5(str, false, false);
    }

    public static String md5(String str, boolean isUpper, boolean isShort) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {

        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        String res = md5StrBuff.toString();
        res = isUpper?res.toUpperCase():res.toLowerCase();
        ////16位加密，从第9位到25位
        res = isShort?res.substring(8,24):res;
        return res;
    }
}

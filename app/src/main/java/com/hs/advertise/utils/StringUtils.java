//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hs.advertise.utils;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    private static final int MAX_EXTENSION_LENGTH = 4;

    public StringUtils() {

    }

    public static byte[] getBytesIso8859_1(String string) {

        return getBytesUnchecked(string, "ISO-8859-1");
    }

    public static byte[] getBytesUsAscii(String string) {

        return getBytesUnchecked(string, "US-ASCII");
    }

    public static byte[] getBytesUtf16(String string) {

        return getBytesUnchecked(string, "UTF-16");
    }

    public static byte[] getBytesUtf16Be(String string) {

        return getBytesUnchecked(string, "UTF-16BE");
    }

    public static byte[] getBytesUtf16Le(String string) {

        return getBytesUnchecked(string, "UTF-16LE");
    }

    public static byte[] getBytesUtf8(String string) {

        return getBytesUnchecked(string, "UTF-8");
    }

    public static byte[] getBytesUnchecked(String string, String charsetName) {

        if (string == null) {
            return null;
        } else {
            try {
                return string.getBytes(charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {

        return new IllegalStateException(charsetName + ": " + e);
    }

    public static String newString(byte[] bytes, String charsetName) {

        if (bytes == null) {
            return null;
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException var3) {
                throw newIllegalStateException(charsetName, var3);
            }
        }
    }

    public static String newStringIso8859_1(byte[] bytes) {

        return newString(bytes, "ISO-8859-1");
    }

    public static String newStringUsAscii(byte[] bytes) {

        return newString(bytes, "US-ASCII");
    }

    public static String newStringUtf16(byte[] bytes) {

        return newString(bytes, "UTF-16");
    }

    public static String newStringUtf16Be(byte[] bytes) {

        return newString(bytes, "UTF-16BE");
    }

    public static String newStringUtf16Le(byte[] bytes) {

        return newString(bytes, "UTF-16LE");
    }

    public static String newStringUtf8(byte[] bytes) {

        return newString(bytes, "UTF-8");
    }
    public static  String getExtension(String url) {
        int dotIndex = url.lastIndexOf('.');
        int slashIndex = url.lastIndexOf('/');
        return dotIndex != -1 && dotIndex > slashIndex && dotIndex + 2 + MAX_EXTENSION_LENGTH > url.length() ?
                url.substring(dotIndex + 1, url.length()) : "";
    }

    //判断是否是数字
    public static boolean isDigit(String str){
        if( null == str || str.length() == 0 ){
            return false;
        }

        for(int i = str.length(); --i >= 0;){
            int c = str.charAt(i);
            if( c < 48 || c > 57 ){
                return false;
            }
        }

        return true;
    }
}

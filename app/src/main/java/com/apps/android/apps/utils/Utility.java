package com.apps.android.apps.utils;

/**
 * Created by DARSHAN HUIDROJM on 8/9/2016.
 */
public class Utility {

    public static String getHtml(String msg){
        String s ="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<p align=\"center\">"+msg+"<p/>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        return s;
    }
}

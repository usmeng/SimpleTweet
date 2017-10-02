package com.meng.simpletweet.util;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mengzhou on 10/1/17.
 */
public class UtilsTest extends TestCase {

    public void testConvertTime() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ssZ yyyy");
        Date date = format.parse("Thu Sep 28 22:41:07 +0000 2017");
        System.out.println("date: " + date.getTime());
    }

}
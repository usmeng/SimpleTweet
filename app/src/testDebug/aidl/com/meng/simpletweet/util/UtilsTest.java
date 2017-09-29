package com.meng.simpletweet.util;


import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mengzhou on 9/28/17.
 */
public class UtilsTest extends TestCase {

    public Date testConvertTime(String date) throws Exception {
//        Thu Sep 28 22:41:07 +0000 2017
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ssZ yyyy");
        return format.parse(date);
    }

}
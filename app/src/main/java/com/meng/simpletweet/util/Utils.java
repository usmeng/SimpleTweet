package com.meng.simpletweet.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mengzhou on 9/28/17.
 */

public class Utils {

    public static void showInputMethod(View v, boolean show) {
        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(!show) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        else imm.showSoftInputFromInputMethod(v.getWindowToken(), 0);
    }

    public static void writeToCacheDir(JSONArray json) {
        try {
            File file = new File("/tweets.json");
            if (!file.exists()) file.createNewFile();
            System.out.println(file.getAbsoluteFile());
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Thu Sep 28 22:41:07 +0000 2017
    public static long convertTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ssZ yyyy");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    // Thu Sep 28 22:41:07 +0000 2017
    public static String convertTimeStamp(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ssZ yyyy");
        return format.format(date);
    }

    public static String getTime(String time) {
        long diff = System.currentTimeMillis() - convertTime(time);
        diff /= 1000;
        if(diff < 60) return "just";
        if(diff < 60 * 60) return diff / 60 + " mins";
        if(diff < 24 * 60 * 60) return diff / 60 / 60 + " hrs";
        return diff / 24 / 60 / 60 + "days";
    }
}

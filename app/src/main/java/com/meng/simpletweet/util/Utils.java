package com.meng.simpletweet.util;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by mengzhou on 9/28/17.
 */

public class Utils {

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

    public static String getTime(String time) {
        long diff = System.currentTimeMillis() - convertTime(time);
        diff /= 1000;
        if(diff < 60) return "just";
        if(diff < 60 * 60) return diff / 60 + " mins";
        if(diff < 24 * 60 * 60) return diff / 60 / 60 + " hrs";
        return diff / 24 / 60 / 60 + "days";
    }
}

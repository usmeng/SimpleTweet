package com.meng.simpletweet.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.meng.simpletweet.data.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the DBFlow wiki for more details:
 * https://github.com/codepath/android_guides/wiki/DBFlow-Guide
 *
 * Note: All models **must extend from** `BaseModel` as shown below.
 * 
 */

@Table(database = MyDatabase.class)
public class Tweet extends BaseModel {
    // Define database columns and associated fields
    @PrimaryKey
    @Column
    @SerializedName("id_str")
    Long id;

    @Column
    @SerializedName("created_at")
    String createdTime;

    @Column
    @SerializedName("text")
    String content;
    @Column
    String source;
    @Column
    String geo;
    @Column
    int retweet_count;
    @Column
    int favorite_count;
    @Column
    boolean favorited;
    @Column
    boolean retweeted;

    User user;
    Entity entities;

    public String getCreatedTime() {
        return createdTime;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    public String getGeo() {
        return geo;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public Entity getEntities() {
        return entities;
    }

    public Tweet() {
    }

    public static Tweet from(JSONObject object) {
        Tweet tweet = new Gson().fromJson(object.toString(), Tweet.class);
        tweet.save();
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.from(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }

        return tweets;
    }
}

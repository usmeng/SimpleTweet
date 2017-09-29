package com.meng.simpletweet.models;

import com.google.gson.annotations.SerializedName;
import com.meng.simpletweet.data.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by mengzhou on 9/28/17.
 */

@Table(database = MyDatabase.class)
public class User extends BaseModel {

    @PrimaryKey
    @Column
    @SerializedName("id_str")
    public String id;

    @Column
    public String name;
    @Column
    public String screen_name;
    public String location;
    public String description;
    public String url;
    @Column
    public int followers_count;
    @Column
    public int friends_count;
    @Column
    public int listed_count;
    @Column
    public int favourites_count;
    @Column
    public boolean verified;
    public String lang;
    @Column
    public String profile_image_url;
    public String profile_background_color;
    @Column
    public String profile_background_image_url;
    @Column
    public String profile_banner_url;
    public boolean profile_use_background_image;
    @Column
    public boolean following;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public int getListed_count() {
        return listed_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getLang() {
        return lang;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getProfile_background_color() {
        return profile_background_color;
    }

    public String getProfile_background_image_url() {
        return profile_background_image_url;
    }

    public String getProfile_banner_url() {
        return profile_banner_url;
    }

    public boolean isProfile_use_background_image() {
        return profile_use_background_image;
    }

    public boolean isFollowing() {
        return following;
    }
}

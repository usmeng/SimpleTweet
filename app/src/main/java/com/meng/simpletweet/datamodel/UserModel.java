package com.meng.simpletweet.datamodel;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.models.User;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mengzhou on 10/8/17.
 */

public class UserModel {

    public static final String TAG = UserModel.class.getSimpleName();

    public interface UserListCallback {
        void onResponse(List<User> list, String message);
    }

    public interface UserCallback {
        void onResponse(User tweet, String message);
    }

    public void getMyProfile(final UserCallback tweetCallback) {
        RestApplication.getRestClient().getMyProfile(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                super.onSuccess(statusCode, headers, json);
                tweetCallback.onResponse(User.from(json), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void getUserProfile(String screenName, String id, final UserCallback tweetCallback) {
        RestApplication.getRestClient().getUserProfile(screenName, id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                super.onSuccess(statusCode, headers, json);
                tweetCallback.onResponse(User.from(json), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }
}

package com.meng.simpletweet.datamodel;

import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.models.Tweet;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mengzhou on 10/4/17.
 */

public class TweetModel {

    public static final String TAG = TweetModel.class.getSimpleName();

    public interface TweetArrayCallback {
        void onResponse(List<Tweet> list, String message);
    }

    public interface TweetCallback {
        void onResponse(Tweet tweet, String message);
    }

    public void fetchTimelineAsync(int page, final TweetArrayCallback tweetCallback) {
        RestApplication.getRestClient().getHomeTimeline(page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                tweetCallback.onResponse(Tweet.fromJson(json), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void fetchUserTimelineAsync(String screenName, int page, final TweetArrayCallback tweetCallback) {
        RestApplication.getRestClient().getUserTimeline(screenName, page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                tweetCallback.onResponse(Tweet.fromJson(json), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }


    public void retrieveTweet(String id, final TweetCallback tweetCallback) {
        RestApplication.getRestClient().retrieveTweet(id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("TweetModel", "response success: " + response.toString());
                tweetCallback.onResponse(Tweet.from(response), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("TweetModel", "response failed: " + responseString);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void postTweet(String tweet, final TweetCallback tweetCallback) {
        RestApplication.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                tweetCallback.onResponse(Tweet.from(response), null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void fetchMentionUser(int page, final TweetArrayCallback tweetCallback) {
            RestApplication.getRestClient().getMentionLine(10, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e(TAG, response.toString());
                    tweetCallback.onResponse(Tweet.fromJson(response), null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    tweetCallback.onResponse(null, responseString + " : " + throwable.getMessage());
                }
            });
    }

    public void loadTimelineFromLocal(int page, final TweetArrayCallback tweetCallback) {
        final int QUERY_LENGTH = 20;
        List<Tweet> tweets = SQLite.select().from(Tweet.class).offset(page * QUERY_LENGTH).limit(QUERY_LENGTH).queryList();
        tweetCallback.onResponse(tweets, null);
    }
}

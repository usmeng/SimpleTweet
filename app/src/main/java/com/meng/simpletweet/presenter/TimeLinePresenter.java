package com.meng.simpletweet.presenter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.ui.ITimeLineView;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mengzhou on 9/29/17.
 */
public class TimeLinePresenter {

    ITimeLineView mTimeLineView;

    public TimeLinePresenter(ITimeLineView iTimeLineView) {
        this.mTimeLineView = iTimeLineView;
    }

    // Send the network request to fetch the updated data
    // `client` here is an instance of Android Async HTTP
    // getHomeTimeline is an example endpoint.
    public void fetchTimelineAsync(int page) {
        RestApplication.getRestClient().getHomeTimeline(page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                super.onSuccess(statusCode, headers, json);
                mTimeLineView.showFreshTweets(Tweet.fromJson(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mTimeLineView.showError(responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void retrieveTweet(String id) {
        RestApplication.getRestClient().retrieveTweet(id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mTimeLineView.showTweetDetail(Tweet.from(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mTimeLineView.showError(responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void postTweet(String tweet) {
        RestApplication.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mTimeLineView.showPostDonePage(Tweet.from(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mTimeLineView.showError(responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void loadTimelineFromLocal(int page) {
        final int QUERY_LENGTH = 20;
        List<Tweet> tweets = SQLite.select().from(Tweet.class).offset(page * QUERY_LENGTH).limit(QUERY_LENGTH).queryList();
//        for(Tweet tweet : tweets) {
//            tweet.setUser();
//        }
        mTimeLineView.showHistoryTweets(tweets);
    }
}

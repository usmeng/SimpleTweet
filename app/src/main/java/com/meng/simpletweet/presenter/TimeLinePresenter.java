package com.meng.simpletweet.presenter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.ui.ITimeLineView;

import org.json.JSONArray;

import java.util.ArrayList;

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
                mTimeLineView.showFetchTweetError(responseString + " : " + throwable.getMessage());
            }
        });
    }

    public void loadTimelineFromLocal(int page, int totalItemsCount) {
        mTimeLineView.showHistoryTweets(new ArrayList<>());
    }
}

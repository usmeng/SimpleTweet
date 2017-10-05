package com.meng.simpletweet.presenter;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.datamodel.TweetModel;
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
    private final TweetModel tweetModel;

    public TimeLinePresenter(ITimeLineView iTimeLineView) {
        this.mTimeLineView = iTimeLineView;

        tweetModel = new TweetModel();
    }

    // Send the network request to fetch the updated data
    // `client` here is an instance of Android Async HTTP
    // getHomeTimeline is an example endpoint.
    public void fetchTimelineAsync(int page) {
        tweetModel.fetchTimelineAsync(0, (list, message) -> {
            if(list != null) mTimeLineView.showFreshTweets(list);
            else mTimeLineView.showError(message);
        });
    }

    public void retrieveTweet(String id) {
        tweetModel.retrieveTweet(id, (tweet, message) -> {
            if(tweet != null) mTimeLineView.showTweetDetail(tweet);
            else mTimeLineView.showError(message);
        });
    }

    public void postTweet(String tweetStr) {
        tweetModel.postTweet(tweetStr, (tweet, message) -> {
            if(tweet != null) mTimeLineView.showPostDonePage(tweet);
            else mTimeLineView.showError(message);
        });
    }

    public void loadTimelineFromLocal(int page) {
        tweetModel.loadTimelineFromLocal(page, (list, message) -> {
            if(list != null) mTimeLineView.showHistoryTweets(list);
        });
    }
}

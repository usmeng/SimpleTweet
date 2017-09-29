package com.meng.simpletweet.ui;

import com.meng.simpletweet.models.Tweet;

import java.util.List;

/**
 * Created by mengzhou on 9/29/17.
 */

public interface ITimeLineView {

    /**
     * fetch new tweets from web server when pull down
     * @param tweets
     */
    void showFreshTweets(List<Tweet> tweets);

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    void showHistoryTweets(List<Tweet> tweets);

    void showFetchTweetError(String message);
}

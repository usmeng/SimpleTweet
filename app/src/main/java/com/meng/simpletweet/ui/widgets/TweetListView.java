package com.meng.simpletweet.ui.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.ui.fragments.EndlessRecyclerViewScrollListener;
import com.meng.simpletweet.ui.fragments.TimeLineAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 10/8/17.
 */

public class TweetListView extends SwipeRefreshLayout {

    private TimeLineAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private TimelineCallBack mListener;
    public interface TimelineCallBack {
        void fetchTimelineAsync(int page);
        void loadTimelineFromLocal(int page);
    }

    public TweetListView(Context context) {
        super(context);
    }

    public void init(TimelineCallBack mListener) {

        setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call mSwipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            mListener.fetchTimelineAsync(0);
        });

        // Configure the refreshing colors
        setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        // recycler view
        mRecyclerView = new RecyclerView(getContext());
        LayoutParams params = new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mRecyclerView.setLayoutParams(params);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TimeLineAdapter(new ArrayList<>(), getContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mListener.loadTimelineFromLocal(page);
            }
        });
    }

    public void postTweet(String contentEt) {
        Tweet tweet = new Tweet(contentEt);
        mAdapter.addToHead(tweet);
        tweet.save();
    }

    /**
     * fetch new tweets from web server when pull down
     * @param tweets
     */
    public void addFreshTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToHead(tweets);
        for(Tweet tweet : tweets) tweet.save();
        if (isRefreshing()) setRefreshing(false);
    }

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    public void addHistoryTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToTail(tweets);
    }

    public void addFreshTweets(Tweet tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToHead(tweets);
        tweets.save();
        if (isRefreshing()) setRefreshing(false);
    }

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    public void addHistoryTweets(Tweet tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToTail(tweets);
    }

    public void showError(String message) {
        if (isRefreshing()) setRefreshing(false);
    }

    public void showTweetDetail(Tweet from) {
    }

    public void showPostDonePage(Tweet from) {
        mAdapter.addToHead(from);
    }
}
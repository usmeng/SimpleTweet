package com.meng.simpletweet.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.ui.widgets.TweetListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 10/4/17.
 */

public class TimeLineFragment2 extends Fragment{
    public static final String TAG = TimeLineFragment2.class.getSimpleName();
    private TweetListView.TimelineCallBack mListener;
    private TweetListView mView;

    public static TimeLineFragment2 newInstance() {
        Bundle args = new Bundle();
        TimeLineFragment2 fragment = new TimeLineFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TweetListView.TimelineCallBack) {
            mListener = (TweetListView.TimelineCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TimelineCallBack");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_list_layout, null);
        TweetListView listView = view.findViewById(R.id.tweet_list_view);
        listView.init(mListener);
        return view;
    }

    public void setOnItemClickListerner(TimeLineAdapter.TimelineItemOnClickListener listener) {
//        if(mAdapter == null) return;
//        mAdapter.setOnItemClickListener(listener);
    }

    public void postTweet(String contentEt) {
        Tweet tweet = new Tweet(contentEt);
        mView.addFreshTweets(tweet);
        tweet.save();
    }

    /**
     * fetch new tweets from web server when pull down
     * @param tweets
     */
    public void addFreshTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mView.addFreshTweets(tweets);
        for(Tweet tweet : tweets) tweet.save();
        if (mView.isRefreshing()) mView.setRefreshing(false);
    }

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    public void addHistoryTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mView.addHistoryTweets(tweets);
    }

    public void showError(String message) {
        if (mView.isRefreshing()) mView.setRefreshing(false);
    }

    public void showTweetDetail(Tweet from) {
    }

    public void showPostDonePage(Tweet from) {
        mView.addFreshTweets(from);
    }
}

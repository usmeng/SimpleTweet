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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 10/4/17.
 */

public class TimeLineFragment extends Fragment{
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mTimeLineRecyclerView;
    private TimeLineAdapter mAdapter;
    TimeLineAdapter.TimelineItemOnClickListener listener;
    public static final String TAG = TimeLineFragment.class.getSimpleName();
    private TimelineCallBack mListener;

    public interface TimelineCallBack {
        void fetchTimelineAsync(int page);
        void loadTimelineFromLocal(int page);
    }

    public static TimeLineFragment newInstance() {
        Bundle args = new Bundle();
        TimeLineFragment fragment = new TimeLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimelineCallBack) {
            mListener = (TimelineCallBack) context;
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
        View view = inflater.inflate(R.layout.timeline_fragment, null);
        initSwipeContainer(view);
        initRecyclerView(view);
        mListener.fetchTimelineAsync(0);
        return view;
    }

    public void setOnItemClickListerner(TimeLineAdapter.TimelineItemOnClickListener listener) {
        this.listener = listener;
        if(mAdapter == null) return;
        mAdapter.setOnItemClickListener(listener);
    }

    private void initSwipeContainer(View view) {
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        mSwipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call mSwipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            mListener.fetchTimelineAsync(0);
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initRecyclerView(View view) {
        mTimeLineRecyclerView = view.findViewById(R.id.time_line_rvItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTimeLineRecyclerView.setLayoutManager(linearLayoutManager);
        mTimeLineRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TimeLineAdapter(new ArrayList<>(), getContext());
        mTimeLineRecyclerView.setAdapter(mAdapter);
        if(listener != null) mAdapter.setOnItemClickListener(listener);

        mTimeLineRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
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
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    public void addHistoryTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToTail(tweets);
    }

    public void showError(String message) {
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }

    public void showTweetDetail(Tweet from) {
    }

    public void showPostDonePage(Tweet from) {
        mAdapter.addToHead(from);
    }
}

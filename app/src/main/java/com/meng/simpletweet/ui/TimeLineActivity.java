package com.meng.simpletweet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.presenter.TimeLinePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 9/28/17.
 */
public class TimeLineActivity extends AppCompatActivity implements ITimeLineView {
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mTimeLineRecyclerView;
    private TimeLineAdapter mAdapter;
    public static final String TAG = TimeLineActivity.class.getSimpleName();
    private TimeLinePresenter mTimeLinePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTimeLinePresenter = new TimeLinePresenter(this);

        setContentView(R.layout.time_line_activity);

        initSwipeContainer();
        initRecyclerView();

        mTimeLinePresenter.fetchTimelineAsync(0);
    }

    private void initRecyclerView() {
        mTimeLineRecyclerView = findViewById(R.id.time_line_rvItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTimeLineRecyclerView.setLayoutManager(linearLayoutManager);
        mTimeLineRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new TimeLineAdapter(new ArrayList<>(), this);
        mTimeLineRecyclerView.setAdapter(mAdapter);

        mTimeLineRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mTimeLinePresenter.loadTimelineFromLocal(page, totalItemsCount);
            }
        });
    }

    private void initSwipeContainer() {
        mSwipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        mSwipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call mSwipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            mTimeLinePresenter.fetchTimelineAsync(0);
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * fetch new tweets from web server when pull down
     * @param tweets
     */
    @Override
    public void showFreshTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToHead(tweets);
        if(mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }

    /**
     * load tweets from local storage when pull up
     * @param tweets
     */
    @Override
    public void showHistoryTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToTail(tweets);
    }

    @Override
    public void showFetchTweetError(String message) {
        if(mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }
}

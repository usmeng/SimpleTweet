package com.meng.simpletweet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.meng.simpletweet.R;
import com.meng.simpletweet.RestApplication;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.net.TweetClient;
import com.meng.simpletweet.util.Utils;

import org.json.JSONArray;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mengzhou on 9/28/17.
 */
public class TimeLineActivity extends AppCompatActivity{
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mTimeLineRecyclerView;
    private TimeLineAdapter mAdapter;
    public static final String TAG = TimeLineActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.time_line_activity);

        initSwipeContainer();
        initRecyclerView();

        TweetClient client = RestApplication.getRestClient();
        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                // json.getJSONObject(0).getLong("id");
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                mAdapter.addAll(tweets);
            }
        });
    }

    private void initRecyclerView() {
        mTimeLineRecyclerView = findViewById(R.id.time_line_rvItems);
        mTimeLineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mTimeLineRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new TimeLineAdapter(new ArrayList<Tweet>(), this);
        mTimeLineRecyclerView.setAdapter(mAdapter);
    }

    private void initSwipeContainer() {
        mSwipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call mSwipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                mSwipeContainer.clearAnimation();
            }
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        TweetClient client = RestApplication.getRestClient();
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                mAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                mAdapter.addAll(null);
                // Now we call setRefreshing(false) to signal refresh has finished
                mSwipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

}

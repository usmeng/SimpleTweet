package com.meng.simpletweet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.time_line_activity);

        mTimeLinePresenter = new TimeLinePresenter(this);

        initActionBar();
        initSwipeContainer();
        initRecyclerView();

        mTimeLinePresenter.fetchTimelineAsync(0);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.action_bar);

        TextView actionTitle = toolbar.findViewById(R.id.action_bar_title);
        SearchView searchView = toolbar.findViewById(R.id.action_bar_search);

        actionTitle.setText(R.string.home_title);
        searchView.setOnSearchClickListener(listener -> actionTitle.setVisibility(View.GONE));
        searchView.setOnCloseListener(() -> {
            actionTitle.setVisibility(View.VISIBLE);
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                mTimeLinePresenter.loadTimelineFromLocal(page);
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

    public void onGoUserCenter(View view) {
        Toast.makeText(this, "user center", Toast.LENGTH_SHORT).show();
    }

    public void onCreateTweet(View view) {
        Toast.makeText(this, "create", Toast.LENGTH_SHORT).show();
        showTweetDialog();
    }

    private void showTweetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.tweet_layout, null);
        EditText contentEt = view.findViewById(R.id.tweet_content_edt);
        TextView wordCount = view.findViewById(R.id.tweet_word_count_tv);
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput(contentEt, wordCount);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        view.findViewById(R.id.tweet_back_img).setOnClickListener(listener -> dialog.cancel());
        view.findViewById(R.id.tweet_post_btn).setOnClickListener(listener -> {
            if (checkInput(contentEt, wordCount)) {
                postTweet(contentEt.getText().toString());
                dialog.cancel();
            } else {
                Snackbar.make(view, R.string.can_excess_140_words, Snackbar.LENGTH_SHORT).show();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void postTweet(String contentEt) {
        Tweet tweet = new Tweet(contentEt);
        mAdapter.addToHead(tweet);
        tweet.save();
        mTimeLinePresenter.postTweet(contentEt);
    }

    private boolean checkInput(EditText contentEt, TextView wordCount) {
        String[] words = contentEt.getText().toString().split(" ");
        int leftWord = 140 - words.length;
        if(leftWord >= 0) {
            wordCount.setText(String.valueOf(leftWord));
            wordCount.setTextColor(getResources().getColor(android.R.color.black));
            return true;
        } else {
            wordCount.setText(R.string.excess_limited_words);
            wordCount.setTextColor(getResources().getColor(R.color.red_primary));
            return false;
        }
    }

    /**
     * fetch new tweets from web server when pull down
     * @param tweets
     */
    @Override
    public void showFreshTweets(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToHead(tweets);
        for(Tweet tweet : tweets) tweet.save();
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
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
    public void showError(String message) {
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showTweetDetail(Tweet from) {

    }

    @Override
    public void showPostDonePage(Tweet from) {

    }

}
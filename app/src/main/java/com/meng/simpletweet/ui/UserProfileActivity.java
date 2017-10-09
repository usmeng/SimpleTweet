package com.meng.simpletweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meng.simpletweet.R;
import com.meng.simpletweet.datamodel.UserModel;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.models.User;
import com.meng.simpletweet.presenter.TimeLinePresenter;
import com.meng.simpletweet.ui.fragments.EndlessRecyclerViewScrollListener;
import com.meng.simpletweet.ui.fragments.TimeLineAdapter;
import com.meng.simpletweet.ui.widgets.TweetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 10/8/17.
 */

public class UserProfileActivity extends AppCompatActivity implements ITimeLineView, TimeLineAdapter.TimelineItemOnClickListener {

    private static final String SCREEN_NAME = "screen_name";
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mTimeLineRecyclerView;
    private TimeLineAdapter mAdapter;
    private UserModel userModel;
    private TextView actionTitle;
    private TimeLinePresenter timeLinePresenter;
    private User mUser;
    private ImageView profileBg;
    private TextView userIdTV;
    private TextView followerTV;
    private TextView followingTV;
    private ImageView headImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        initProfileHeader();
        initActionBar();

        userModel = new UserModel();
        timeLinePresenter = new TimeLinePresenter(this);
        String screenName = getIntent().getStringExtra(SCREEN_NAME);
        showProfile(screenName);
    }

    private void showProfile(String screenName) {
        if(screenName.length() == 0) {
            userModel.getMyProfile((user, message) -> {
                this.mUser = user;
                if(user != null) showUserProfile(user);
            });
        } else {
            userModel.getUserProfile(screenName, null, (user, message) -> {
                this.mUser = user;
                if(user != null) showUserProfile(user);
            });
        }
    }

    private void initProfileHeader() {
        profileBg = findViewById(R.id.img_profile_bg);
        userIdTV = findViewById(R.id.tv_user_id);
        followerTV = findViewById(R.id.tv_follower_value);
        followingTV = findViewById(R.id.tv_following_value);
        headImg = findViewById(R.id.img_head_icon);

        mSwipeContainer = findViewById(R.id.swipeContainer);
        mTimeLineRecyclerView = findViewById(R.id.time_line_rvItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mTimeLineRecyclerView.setLayoutManager(linearLayoutManager);
        mTimeLineRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new TimeLineAdapter(new ArrayList<>(), this);
        mTimeLineRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mSwipeContainer.setOnRefreshListener(() -> {
            timeLinePresenter.fetchUserTimeAsync(mUser.getScreen_name(), 20);
        });
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mTimeLineRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                timeLinePresenter.loadTimelineFromLocal(page);
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.user_action_bar);

        actionTitle = toolbar.findViewById(R.id.action_bar_title);
        SearchView searchView = toolbar.findViewById(R.id.action_bar_search);

        actionTitle.setText(R.string.user_name);
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

    private void showUserProfile(User user) {
        Glide.with(this).load(user.getProfile_background_image_url()).into(profileBg);
        Glide.with(this).load(user.getProfile_image_url()).into(headImg);
        userIdTV.setText(String.valueOf(user.getScreen_name()));
        followerTV.setText(String.valueOf(user.getFriends_count()));
        followingTV.setText(String.valueOf(user.getFollowers_count()));
        actionTitle.setText(user.getName() + " profile");

        mAdapter.clear();
        timeLinePresenter.fetchUserTimeAsync(mUser.getScreen_name(), 20);
    }


    public static Intent initIntent(HomeActivity homeActivity, String screenName) {
        Intent intent = new Intent(homeActivity, UserProfileActivity.class);
        intent.putExtra(SCREEN_NAME, screenName);
        return intent;
    }

    @Override
    public void showFreshTweets(List<Tweet> tweets) {
        mAdapter.addToHead(tweets);
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }


    @Override
    public void showHistoryTweets(List<Tweet> tweets) {
//        mAdapter.addToTail(tweets);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void showTweetDetail(Tweet from) {

    }

    @Override
    public void showPostDonePage(Tweet from) {

    }

    @Override
    public void showMentionUserList(List<Tweet> tweets) {

    }

    @Override
    public void onUserProfile(String screenName) {
        if(screenName == null || screenName.length() == 0) return;
        if(screenName.equals(mUser.getScreen_name())) return;
        userModel.getUserProfile(screenName, null, (user, message) -> {
            this.mUser = user;
            if(user != null) showUserProfile(user);
        });
    }

    @Override
    public void onItemClick(String id) {
        startActivity(TweetDetailActivity.initIntent(this, id));
    }

    public void onGoUserCenter(View view) {
        showProfile("");
    }

    public void onCreateTweet(View view) {
        AlertDialog dialog = new TweetDialog(this, tweet -> {
            timeLinePresenter.postTweet(tweet);
            finish();
        });
        dialog.create();
        dialog.show();
    }

}

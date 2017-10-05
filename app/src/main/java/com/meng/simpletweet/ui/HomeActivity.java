package com.meng.simpletweet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.presenter.TimeLinePresenter;
import com.meng.simpletweet.ui.fragments.MentionFragment;
import com.meng.simpletweet.ui.fragments.TimeLineFragment;
import com.meng.simpletweet.ui.widgets.TweetDialog;

import java.util.List;

/**
 * Created by mengzhou on 10/4/17.
 */
public class HomeActivity extends AppCompatActivity implements ITimeLineView, TimeLineFragment.TimelineCallBack{

    private TimeLinePresenter mTimeLinePresenter;
    private HomeFragmentPagerAdapter mViewPagerAdapter;
    private TimeLineFragment mTimeLineFragment;
    private MentionFragment mMentionFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_main_activity);

        mTimeLinePresenter = new TimeLinePresenter(this);

        initActionBar();

        initSlidingTab();
    }

    private void initSlidingTab() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        mViewPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager(), HomeActivity.this);
        viewPager.setAdapter(mViewPagerAdapter);

        mViewPagerAdapter.startUpdate(viewPager);
        mTimeLineFragment = (TimeLineFragment) mViewPagerAdapter.instantiateItem(viewPager, 0);
        mMentionFragment = (MentionFragment) mViewPagerAdapter.instantiateItem(viewPager, 1);
        mViewPagerAdapter.finishUpdate(viewPager);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.action_bar);

        TextView actionTitle = toolbar.findViewById(R.id.action_bar_title);
        SearchView searchView = toolbar.findViewById(R.id.action_bar_search);

        actionTitle.setText(R.string.app_name);
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

    public void onGoUserCenter(View view) {

    }

    public void onCreateTweet(View view) {
        AlertDialog dialog = new TweetDialog(this, tweet -> {
            mTimeLineFragment.postTweet(tweet);
            mTimeLinePresenter.postTweet(tweet);
        });
        dialog.create();
        dialog.show();
    }

    /**
     * fetch new tweets from web server when pull down
     *
     * @param tweets
     */
    @Override
    public void showFreshTweets(List<Tweet> tweets) {
        mTimeLineFragment.addFreshTweets(tweets);
    }

    /**
     * load tweets from local storage when pull up
     *
     * @param tweets
     */
    @Override
    public void showHistoryTweets(List<Tweet> tweets) {
        mTimeLineFragment.addHistoryTweets(tweets);
    }

    @Override
    public void showError(String message) {
        mTimeLineFragment.showError(message);
    }

    @Override
    public void showTweetDetail(Tweet from) {
    }

    @Override
    public void showPostDonePage(Tweet from) {
        mTimeLineFragment.showPostDonePage(from);
    }

    @Override
    public void fetchTimelineAsync(int page) {
        mTimeLinePresenter.fetchTimelineAsync(page);
    }

    @Override
    public void loadTimelineFromLocal(int page) {
        mTimeLinePresenter.loadTimelineFromLocal(page);
    }

    public void changeTitle(String name) {

    }
}

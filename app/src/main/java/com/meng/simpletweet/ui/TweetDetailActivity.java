package com.meng.simpletweet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meng.simpletweet.R;
import com.meng.simpletweet.datamodel.TweetModel;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.models.User;
import com.meng.simpletweet.ui.widgets.PatternEditableBuilder;
import com.meng.simpletweet.util.Utils;

import java.util.regex.Pattern;

/**
 * Created by mengzhou on 10/8/17.
 */

public class TweetDetailActivity extends AppCompatActivity {

    public final static String TWEET_ID = "id";

    TextView mUserNameTv;
    TextView mAtUserTv;
    TextView mTweetTimeTv;
    TextView mTweetContentTv;
    ImageView mHeadIcon;
    ImageView mAttachImg;
    ImageView mReTweetImg;
    TextView mCommentTv;
    TextView mSaveTv;
    TweetModel mTweetModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_detail_activity);

        mUserNameTv = findViewById(R.id.tv_user_name);
        mAtUserTv = findViewById(R.id.tv_at_user);
        mTweetContentTv = findViewById(R.id.tv_tweet_body);
        mTweetTimeTv = findViewById(R.id.tv_tweet_time);

        mHeadIcon = findViewById(R.id.img_head_icon);
        mAttachImg = findViewById(R.id.img_attachment);
        mReTweetImg = findViewById(R.id.img_retweet);
        mCommentTv = findViewById(R.id.tv_tweet_comment);
        mSaveTv = findViewById(R.id.tv_tweet_save);

        String id = getIntent().getStringExtra(TWEET_ID);
        Toast.makeText(this, "id: " + id, Toast.LENGTH_SHORT).show();

        mTweetModel = new TweetModel();
        mTweetModel.retrieveTweet(id, (tweet, message) -> {
            if(tweet != null) update(tweet);
            else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }
    
    public void update(Tweet tweet) {
        Toast.makeText(this, tweet.toString(), Toast.LENGTH_SHORT).show();
        User user = tweet.getUser();
        PatternEditableBuilder mEditableBuilder = new PatternEditableBuilder();
        if(user != null) mUserNameTv.setText(user.getName());
        mTweetTimeTv.setText(Utils.getTime(tweet.getCreatedTime()));
        mTweetContentTv.setText(tweet.getContent());
        if(user != null) {
            mAtUserTv.setText("@" + user.getScreen_name());
            mEditableBuilder.addPattern(Pattern.compile("\\@(\\w+)"), Color.GRAY, text -> {
                Toast.makeText(this, user.getScreen_name(), Toast.LENGTH_SHORT).show();
                /*if(mItemOnClickListener != null) {
                    mItemOnClickListener.onUserProfile(user.getScreen_name());
                }*/
            }).into(mAtUserTv);
        }
        mEditableBuilder.addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE, text -> {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//            if(mItemOnClickListener != null) mItemOnClickListener.onUserProfile(text);
        }).into(mTweetContentTv);
        mTweetContentTv.setLines(tweet.getContent().length() / 40 + 1);

        if(user!= null) Glide.with(this).load(user.profile_image_url).into(mHeadIcon);
        mCommentTv.setText(String.valueOf(tweet.getRetweet_count()));
        mSaveTv.setText(String.valueOf(tweet.getFavorite_count()));

        mAttachImg.setVisibility(View.INVISIBLE);
    }

    public static Intent initIntent(Activity homeActivity, String id) {
        Intent intent = new Intent(homeActivity, TweetDetailActivity.class);
        intent.putExtra(TWEET_ID, id);
        return intent;
    }
}

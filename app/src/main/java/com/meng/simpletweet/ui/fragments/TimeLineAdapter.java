package com.meng.simpletweet.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.models.User;
import com.meng.simpletweet.ui.widgets.PatternEditableBuilder;
import com.meng.simpletweet.util.Utils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by mengzhou on 9/28/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TweetHolderView> {

    private List<Tweet> mData;
    private final LayoutInflater mInflater;
    private Context mContext;
//    private final PatternEditableBuilder mEditableBuilder;
    public interface TimelineItemOnClickListener {
        void onUserProfile(String screenName);
        void onItemClick(String id);
    }

    private TimelineItemOnClickListener mItemOnClickListener;

    public TimeLineAdapter(List<Tweet> data, Context context) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(TimelineItemOnClickListener listener) {
        this.mItemOnClickListener = listener;
    }

    @Override
    public TweetHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TweetHolderView(mInflater.inflate(R.layout.tweet_list_item, null));
    }

    @Override
    public void onBindViewHolder(TweetHolderView holder, int position) {
        Tweet tweet = mData.get(position);

        User user = tweet.getUser();

        if(user != null) holder.mUserNameTv.setText(user.getName());
        holder.mTweetTimeTv.setText(Utils.getTime(tweet.getCreatedTime()));
        holder.mTweetContentTv.setText(tweet.getContent());
        if(user != null) {
            holder.mAtUserTv.setText("@" + user.getScreen_name());
            PatternEditableBuilder mEditableBuilder = new PatternEditableBuilder();
            mEditableBuilder.addPattern(Pattern.compile("\\@(\\w+)"), Color.GRAY, text -> {
                if(mItemOnClickListener != null) {
                    mItemOnClickListener.onUserProfile(user.getScreen_name());
                }
            }).into(holder.mAtUserTv);
        }
        PatternEditableBuilder mEditableBuilder = new PatternEditableBuilder();
        mEditableBuilder.addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE, text -> {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            if(mItemOnClickListener != null) mItemOnClickListener.onUserProfile(text);
        }).into(holder.mTweetContentTv);
        holder.mTweetContentTv.setLines(tweet.getContent().length() / 40 + 1);

        if(user!= null) Glide.with(mContext).load(user.profile_image_url).into(holder.mHeadIcon);
        holder.mCommentTv.setText(String.valueOf(tweet.getRetweet_count()));
        holder.mSaveTv.setText(String.valueOf(tweet.getFavorite_count()));

        holder.mAttachImg.setVisibility(View.INVISIBLE);

        holder.mView.setOnClickListener(listener -> {
            if(mItemOnClickListener != null) mItemOnClickListener.onItemClick(String.valueOf(tweet.getId()));
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addToTail(List<Tweet> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addToHead(List<Tweet> list) {
        mData.addAll(0, list);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addToTail(Tweet tweet) {
        mData.add(tweet);
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addToHead(Tweet tweet) {
        mData.add(0, tweet);
        notifyDataSetChanged();
    }

    class TweetHolderView extends RecyclerView.ViewHolder {
        View mView;
        TextView mUserNameTv;
        TextView mAtUserTv;
        TextView mTweetTimeTv;
        TextView mTweetContentTv;
        ImageView mHeadIcon;
        ImageView mAttachImg;
        ImageView mReTweetImg;
        TextView mCommentTv;
        TextView mSaveTv;

        TweetHolderView(View itemView) {
            super(itemView);
            mView = itemView;
            mUserNameTv = itemView.findViewById(R.id.tv_user_name);
            mAtUserTv = itemView.findViewById(R.id.tv_at_user);
            mTweetContentTv = itemView.findViewById(R.id.tv_tweet_body);
            mTweetTimeTv = itemView.findViewById(R.id.tv_tweet_time);

            mHeadIcon = itemView.findViewById(R.id.img_head_icon);
            mAttachImg = itemView.findViewById(R.id.img_attachment);
            mReTweetImg = itemView.findViewById(R.id.img_retweet);
            mCommentTv = itemView.findViewById(R.id.tv_tweet_comment);
            mSaveTv = itemView.findViewById(R.id.tv_tweet_save);
        }
    }
}
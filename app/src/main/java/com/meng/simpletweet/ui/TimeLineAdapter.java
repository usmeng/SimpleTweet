package com.meng.simpletweet.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;
import com.meng.simpletweet.models.User;
import com.meng.simpletweet.util.Utils;

import java.util.List;

/**
 * Created by mengzhou on 9/28/17.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TweetHolderView> {

    private List<Tweet> mData;
    private final LayoutInflater mInflater;
    private Context mContext;

    TimeLineAdapter(List<Tweet> data, Context context) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TweetHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TweetHolderView(mInflater.inflate(R.layout.tweet_list_item, null));
    }

    @Override
    public void onBindViewHolder(TweetHolderView holder, int position) {
        Tweet tweet = mData.get(position);

        User user = tweet.getUser();
        holder.mUserNameTv.setText(user.getName());
        holder.mTweetTimeTv.setText(Utils.getTime(tweet.getCreatedTime()));
        holder.mTweetContentTv.setText(tweet.getContent());
        holder.mTweetContentTv.setLines(tweet.getContent().length() / 50);

        Glide.with(mContext).load(user.profile_image_url).into(holder.mHeadIcon);
        holder.mCommentTv.setText(String.valueOf(tweet.getRetweet_count()));
        holder.mSaveTv.setText(String.valueOf(tweet.getFavorite_count()));

        holder.mAttachImg.setVisibility(View.INVISIBLE);
        /*if(tweet.getUserHandle().length() > 0) {
            holder.mAttachImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(tweet.getUserHandle()).into(holder.mAttachImg);
        } else holder.mAttachImg.setVisibility(View.GONE);*/
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
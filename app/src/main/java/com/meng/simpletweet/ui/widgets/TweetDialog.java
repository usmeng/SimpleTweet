package com.meng.simpletweet.ui.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meng.simpletweet.R;

/**
 * Created by mengzhou on 9/30/17.
 */
public class TweetDialog extends AlertDialog {

    public interface TweetCallbackListener {
        void onPost(String tweet);
    }

    private TweetCallbackListener mTweetCallbackListener;

    public TweetDialog(@NonNull Context context, TweetCallbackListener tweetCallbackListener) {
        super(context);
        this.mTweetCallbackListener = tweetCallbackListener;

        View view = LayoutInflater.from(context).inflate(R.layout.tweet_layout, null);
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
        view.findViewById(R.id.tweet_back_img).setOnClickListener(listener -> cancel());
        view.findViewById(R.id.tweet_post_btn).setOnClickListener(listener -> {
            if (checkInput(contentEt, wordCount)) {
                mTweetCallbackListener.onPost(contentEt.getText().toString());
                cancel();
            } else {
                Snackbar.make(view, R.string.can_excess_140_words, Snackbar.LENGTH_SHORT).show();
            }
        });
        setView(view);
    }

    private boolean checkInput(EditText contentEt, TextView wordCount) {
        String[] words = contentEt.getText().toString().split(" ");
        int leftWord = 140 - words.length;
        if(leftWord >= 0) {
            wordCount.setText(String.valueOf(leftWord));
            wordCount.setTextColor(getContext().getResources().getColor(android.R.color.black));
            return true;
        } else {
            wordCount.setText(R.string.excess_limited_words);
            wordCount.setTextColor(getContext().getResources().getColor(R.color.red_primary));
            return false;
        }
    }
}

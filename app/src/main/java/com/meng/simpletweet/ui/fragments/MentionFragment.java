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
import android.widget.TextView;

import com.meng.simpletweet.R;
import com.meng.simpletweet.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengzhou on 10/4/17.
 */

// In this case, the fragment displays simple text based on the page
public class MentionFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private SwipeRefreshLayout mSwipeContainer;
    private TimeLineAdapter mAdapter;

    private MentionFragment.MentionUserCallBack mListener;

    public interface MentionUserCallBack {
        void fetchMentionedUserAsync(int page);
    }

    public static MentionFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MentionFragment fragment = new MentionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MentionUserCallBack) {
            mListener = (MentionUserCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MentionUserCallBack");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline_fragment, container, false);
        initSwipeContainer(view);
        initRecyclerView(view);
        mListener.fetchMentionedUserAsync(0);
        return view;
    }

    private void initSwipeContainer(View view) {
        mSwipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        mSwipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call mSwipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            mListener.fetchMentionedUserAsync(0);
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initRecyclerView(View view) {
        RecyclerView mTimeLineRecyclerView = view.findViewById(R.id.time_line_rvItems);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mTimeLineRecyclerView.setLayoutManager(linearLayoutManager);
        mTimeLineRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new TimeLineAdapter(new ArrayList<>(), getContext());
        mTimeLineRecyclerView.setAdapter(mAdapter);

        /*mTimeLineRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mListener.loadTimelineFromLocal(page);
            }
        });*/
    }

    public void addMentionedUser(List<Tweet> tweets) {
        // ...the data has come back, add new items to your adapter...
        mAdapter.addToHead(tweets);
        for(Tweet tweet : tweets) tweet.save();
        if (mSwipeContainer.isRefreshing()) mSwipeContainer.setRefreshing(false);
    }
}

package com.meng.simpletweet.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.meng.simpletweet.ui.fragments.MentionFragment;
import com.meng.simpletweet.ui.fragments.TimeLineFragment;

/**
 * Created by mengzhou on 10/4/17.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "HOME", "MENTION" };
    private Context context;

    public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) return TimeLineFragment.newInstance();
        return MentionFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}

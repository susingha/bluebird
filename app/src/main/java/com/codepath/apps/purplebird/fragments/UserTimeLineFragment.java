package com.codepath.apps.purplebird.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by supsingh on 11/4/2016.
 */

public class UserTimeLineFragment extends TweetsListFragment {

    public String getCACHE_FILE() {
        return CACHE_FILE;
    }

    public TimelineType getTimelineType_t() {
        return timelineType_t;
    }

    private final String TAG = "UserTimeline";
    private final String CACHE_FILE = "cacheUserTimeline.txt";
    private final TimelineType timelineType_t = TimelineType.TIMELINE_USER;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Static
    public static UserTimeLineFragment newInstance(String screenName) {
        UserTimeLineFragment userFragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }
}

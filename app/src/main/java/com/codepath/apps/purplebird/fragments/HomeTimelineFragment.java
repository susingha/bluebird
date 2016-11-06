package com.codepath.apps.purplebird.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.purplebird.models.Tweet;

/**
 * Created by supsingh on 11/4/2016.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    public String getCACHE_FILE() {
        return CACHE_FILE;
    }

    public TimelineType getTimelineType_t() {
        return timelineType_t;
    }

    private final String TAG = "HomeTimeline";
    private final String CACHE_FILE = "cacheHomeTimeline.txt";
    private final TimelineType timelineType_t = TimelineType.TIMELINE_HOME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addTweetOnTop(Tweet newTweet) {
        Log.d(TAG, "addTweetOnTop");
        lvTweets.smoothScrollToPosition(0);
        tweets.add(0, newTweet);
        aTweets.notifyDataSetChanged();
    }
}

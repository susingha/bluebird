package com.codepath.apps.purplebird.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by supsingh on 11/4/2016.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    public String getCACHE_FILE() {
        return CACHE_FILE;
    }

    public TimelineType getTimelineType_t() {
        return timelineType_t;
    }

    private final String TAG = "MentionsTimeline";
    private final String CACHE_FILE = "cacheMentionsTimeline.txt";
    private final TimelineType timelineType_t = TimelineType.TIMELINE_MENTIONS;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

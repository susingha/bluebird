package com.codepath.apps.purplebird.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by supsingh on 11/6/2016.
 */

public class SearchTimeLineFragment extends TweetsListFragment {

    public String getCACHE_FILE() {
        return CACHE_FILE;
    }

    public TimelineType getTimelineType_t() {
        return timelineType_t;
    }

    private final String TAG = "SearchTimeline";
    private final String CACHE_FILE = "cacheSearchTimeline.txt";
    private final TimelineType timelineType_t = TimelineType.TIMELINE_SEARCH;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Static
    public static SearchTimeLineFragment newInstance(String search_query) {
        SearchTimeLineFragment searchFragment = new SearchTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("search_query", search_query);
        searchFragment.setArguments(args);
        return searchFragment;
    }
}

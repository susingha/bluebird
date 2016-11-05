package com.codepath.apps.purplebird.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.purplebird.EndlessScrollListener;
import com.codepath.apps.purplebird.ProfileActivity;
import com.codepath.apps.purplebird.R;
import com.codepath.apps.purplebird.TweetsArrayAdapter;
import com.codepath.apps.purplebird.TwitterNetworkClient;
import com.codepath.apps.purplebird.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/**
 * Created by supsingh on 11/3/2016.
 */

public abstract class TweetsListFragment extends Fragment {

    ActivityCommunicator callingContext;
    ArrayList<Tweet> tweets;
    TweetsArrayAdapter aTweets;
    ListView lvTweets;
    SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

//      callingContext = (ActivityCommunicator) getContext();

        // construct the adapter
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(TwitterNetworkClient.PageType.NEXT, getCACHE_FILE());
                return true;
            }
        });
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // callingContext.launchProfileActivity(tweets.get(position).getUser());

                Log.d(TAG, "position: " + position + " user: " + tweets.get(position).getUser().getScreenName());
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                i.putExtra("screen_name", tweets.get(position).getUser().getScreenName());
                startActivity(i);
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(TwitterNetworkClient.PageType.FIRST, getCACHE_FILE());
            }
        });
        return v;
    }

    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
        swipeContainer.setRefreshing(false);
        swipeContainer.setEnabled(true);
    }

    public void clearList() {
        // simply aTweets.clear(); ?? or
        tweets.clear();
        aTweets.notifyDataSetChanged();

    }

    public TweetsArrayAdapter getAdapter () {
        return null;
    }

    public abstract void populateTimeline(TwitterNetworkClient.PageType page_t, final String cacheFile);
    public abstract String getCACHE_FILE();

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        // callingContext = (ActivityCommunicator) context;
    }


}

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
import android.widget.Toast;

import com.codepath.apps.purplebird.EndlessScrollListener;
import com.codepath.apps.purplebird.ProfileActivity;
import com.codepath.apps.purplebird.R;
import com.codepath.apps.purplebird.TweetsArrayAdapter;
import com.codepath.apps.purplebird.TwitterApp;
import com.codepath.apps.purplebird.TwitterNetworkClient;
import com.codepath.apps.purplebird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/**
 * Created by supsingh on 11/3/2016.
 */

public abstract class TweetsListFragment extends Fragment {

    public class ObjMaxId {
        public Long max_id;
    }

    public enum TimelineType {
        TIMELINE_HOME,
        TIMELINE_MENTIONS,
        TIMELINE_USER,
    }

    ActivityCommunicator callingContext;
    ArrayList<Tweet> tweets;
    TweetsArrayAdapter aTweets;
    ListView lvTweets;
    SwipeRefreshLayout swipeContainer;
    private TwitterNetworkClient client;
    ObjMaxId objMaxId = new ObjMaxId();
    String screenName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
//      callingContext = (ActivityCommunicator) getContext();

        // construct the adapter
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "sup: onLoadMore page: " + page);
                populateTimeline(TwitterNetworkClient.PageType.NEXT, getCACHE_FILE(), getTimelineType_t());
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
                populateTimeline(TwitterNetworkClient.PageType.FIRST, getCACHE_FILE(), getTimelineType_t());
            }
        });

        objMaxId.max_id = Long.valueOf(0);
        client = TwitterApp.getRestClient();
        populateTimeline(TwitterNetworkClient.PageType.FIRST, getCACHE_FILE(), getTimelineType_t());
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

    public TweetsArrayAdapter getAdapter() {
        return null;
    }

    public void populateTimeline(TwitterNetworkClient.PageType page, final String cacheFile, TimelineType timelineType_t) {

        // If requesting first page, reset adapter steps
        final TwitterNetworkClient.PageType page_t = page;
        if (page == TwitterNetworkClient.PageType.FIRST) {
            clearList();
        }

        if (timelineType_t == TimelineType.TIMELINE_USER)
            screenName = getArguments().getString("screen_name");

        client.getTimeline(timelineType_t, page, objMaxId.max_id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "onSuccess");
                Toast.makeText(getActivity(), "Next Page Loading", Toast.LENGTH_SHORT).show();

                addAll(Tweet.fromJSONArray(response, objMaxId));

                // Save the json in persistent storage
                if (page_t == TwitterNetworkClient.PageType.FIRST) {
                    File filesDir = getActivity().getFilesDir();
                    File todoFile = new File(filesDir, cacheFile);
                    try {
                        FileUtils.writeStringToFile(todoFile, response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d(TAG, "home_timeline - onFailure");
                if (statusCode == TwitterNetworkClient.REST_NO_INTERNET_STATUS_CODE) {
                    Toast.makeText(getActivity(), "NO INTERNET. Loading from storage", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "API Rate Limited. Loading from storage", Toast.LENGTH_SHORT).show();
                }

                if (page_t == TwitterNetworkClient.PageType.FIRST) {
                    // Check if we have stored json in persistent storage
                    File filesDir = getActivity().getFilesDir();
                    File todoFile = new File(filesDir, cacheFile);
                    try {
                        String json = FileUtils.readFileToString(todoFile).toString();
                        try {
                            // Retrieve json from Persistent storage and show on screen
                            JSONArray jsonArray = new JSONArray(json);
                            addAll(Tweet.fromJSONArray(jsonArray, objMaxId));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "API Rate Limited. Cannot load more", Toast.LENGTH_SHORT).show();
                }
            }
        }, screenName);
    }

    public abstract String getCACHE_FILE();

    public abstract TimelineType getTimelineType_t();

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        // callingContext = (ActivityCommunicator) context;
    }


}

package com.codepath.apps.purplebird.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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

import cz.msebera.android.httpclient.Header;

/**
 * Created by supsingh on 11/4/2016.
 */

public class MentionsTimelineFragment extends TweetsListFragment {

    public String getCACHE_FILE() {
        return CACHE_FILE;
    }

    private final String TAG = "MentionsTimeline";
    private final String CACHE_FILE = "cacheMentionsTimeline.txt";
    private TwitterNetworkClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient(); // sup: should get a client for hometimeline
        populateTimeline(TwitterNetworkClient.PageType.FIRST, CACHE_FILE);
    }

    // sup:TODO move this routine inside the TweetsTimelineFragment class. Need to change the implementation of the client
    public void populateTimeline(TwitterNetworkClient.PageType page, final String cacheFile) {

        // If requesting first page, reset adapter steps
        final TwitterNetworkClient.PageType page_t = page;
        if (page == TwitterNetworkClient.PageType.FIRST) {
            clearList();
        }

        client.getMentionsTimeline(page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "onSuccess");
                Toast.makeText(getActivity(), "Next Page Loading", Toast.LENGTH_SHORT).show();

                addAll(Tweet.fromJSONArray(response));

                // Save the json in persistent storage
                if(page_t == TwitterNetworkClient.PageType.FIRST) {
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
                Toast.makeText(getActivity(), "API Rate Limited. Loading from storage", Toast.LENGTH_SHORT).show();

                if(page_t == TwitterNetworkClient.PageType.FIRST) {
                    // Check if we have stored json in persistent storage
                    File filesDir = getActivity().getFilesDir();
                    File todoFile = new File(filesDir, cacheFile);
                    try {
                        String json = FileUtils.readFileToString(todoFile).toString();
                        try {
                            // Retrieve json from Persistent storage and show on screen
                            JSONArray jsonArray = new JSONArray(json);
                            addAll(Tweet.fromJSONArray(jsonArray));
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
        });
    }
}

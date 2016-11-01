package com.codepath.apps.purplebird;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.purplebird.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private static String TAG = "sup: TimelineActivity";
    private static final String PERSIST_FILE = "lastTweetsArray.txt";
    TwitterNetworkClient client;
    ArrayList<Tweet> tweets;
    TweetsArrayAdapter aTweets;
    ListView lvTweets;
    SwipeRefreshLayout swipeContainer;
    ArrayList<Tweet> newTweetsArrayRef = null;
    MenuItem itCompose;
    String COMPOSE_TAG = "COMPOSE_TAG";
    EditText etCompose;
    String status;
    ComposeFragment fgCompose;
    FragmentTransaction ftCompose;

    // < TEMP CODE //
    ArrayList<Tweet> lastTweetsArray;
    //  TEMP CODE > //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSendTweet(View v) {
        etCompose = (EditText) findViewById(R.id.etCompose);
        status = etCompose.getText().toString();
        Log.d(TAG, "Send Tweet ->" + status + "<-");

        if (fgCompose == null) {
            if ((fgCompose = (ComposeFragment) getSupportFragmentManager().findFragmentByTag(COMPOSE_TAG)) == null) {
                return;
            }
        }

        ftCompose = getSupportFragmentManager().beginTransaction();
        ftCompose.remove(fgCompose);
        ftCompose.commit();

        updateStatus(status);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itCompose) {
            Log.d(TAG, "Compose");

            ftCompose = getSupportFragmentManager().beginTransaction();
            fgCompose = new ComposeFragment();
//          ftCompose.replace(R.id.frComposePlaceHolder, new ComposeFragment());
            ftCompose.add(R.id.frComposePlaceHolder, fgCompose, COMPOSE_TAG);
            ftCompose.addToBackStack(null);
            ftCompose.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateStatus(String status) {
        client.postComposeTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "update - onSuccess");
                lvTweets.smoothScrollToPosition(0);

                Tweet newTweet = Tweet.fromJSON(response);
                tweets.add(0, newTweet);
                aTweets.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "update - onFailure sup:1");
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "update - onFailure sup:2");
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "update - onFailure sup:3");
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        setupViews();
        populateTimeline(PageType.FIRST);
    }


    private void setupViews() {
        // construct the adapter
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline(PageType.NEXT);
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(PageType.FIRST);
            }
        });

        client = TwitterApp.getRestClient();
        lastTweetsArray = new ArrayList<>();
    }

    private void populateTimeline(PageType page) {

        // If requesting first page, reset adapter steps
        final PageType page_t = page;
        if (page == PageType.FIRST) {
            tweets.clear();
            aTweets.notifyDataSetChanged();
        }

        client.getHomeTimeline(page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, "home_timeline - onSuccess");
                Toast.makeText(getApplicationContext(), "Next Page Loading", Toast.LENGTH_SHORT).show();

                newTweetsArrayRef = Tweet.fromJSONArray(response);
                aTweets.addAll(newTweetsArrayRef);

                // Save the json in persistent storage
                if(page_t == PageType.FIRST) {
                    lastTweetsArray.clear();
                    lastTweetsArray.addAll(newTweetsArrayRef);
                    File filesDir = getFilesDir();
                    File todoFile = new File(filesDir, PERSIST_FILE);
                    try {
                        // FileUtils.writeLines(todoFile, lastTweetsArray);
                        FileUtils.writeStringToFile(todoFile, response.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                swipeContainer.setRefreshing(false);
                swipeContainer.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d(TAG, "home_timeline - onFailure");
                Toast.makeText(getApplicationContext(), "API Rate Limited. Loading from storage", Toast.LENGTH_SHORT).show();

                if(page_t == PageType.FIRST) {
                    // Check if we have stored json in persistent storage
                    if (lastTweetsArray.isEmpty()) {
                        File filesDir = getFilesDir();
                        File todoFile = new File(filesDir, PERSIST_FILE);
                        try {
                            String json = FileUtils.readFileToString(todoFile).toString();
                            try {
                                JSONArray jsonArray = new JSONArray(json);
                                newTweetsArrayRef = Tweet.fromJSONArray(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        newTweetsArrayRef = lastTweetsArray;
                    }

                    // Retrieve json from Persistent storage and show on screen
                    aTweets.addAll(newTweetsArrayRef);
                } else {
                    Toast.makeText(getApplicationContext(), "API Rate Limited. Cannot load more", Toast.LENGTH_SHORT).show();
                    aTweets.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);
                swipeContainer.setEnabled(true);
            }
        });

    }
}

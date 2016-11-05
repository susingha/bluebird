package com.codepath.apps.purplebird;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.purplebird.fragments.ActivityCommunicator;
import com.codepath.apps.purplebird.fragments.HomeTimelineFragment;
import com.codepath.apps.purplebird.fragments.MentionsTimelineFragment;
import com.codepath.apps.purplebird.models.Tweet;
import com.codepath.apps.purplebird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ActivityCommunicator {

    String TAG = "sup: TimelineActivity";
    String COMPOSE_TAG = "COMPOSE_TAG";
    MenuItem itCompose;
    EditText etCompose;
    ComposeFragment fgCompose;
    FragmentTransaction ftCompose;
    String status;
    private TwitterNetworkClient client;

    HomeTimelineFragment homeTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient();

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onComposeView(MenuItem menuItem) {
        Log.d(TAG, "onComposeView");

        // Launch the Compose Fragment
        ftCompose = getSupportFragmentManager().beginTransaction();
        fgCompose = new ComposeFragment();
        ftCompose.add(R.id.frComposePlaceHolder, fgCompose, COMPOSE_TAG); // may also use replace
        ftCompose.addToBackStack(null);
        ftCompose.commit();
    }

    public void onProfileView(MenuItem menuItem) {
        Log.d(TAG, "onProfileView");

        // launch the profile activity
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
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


    public void updateStatus(String status) {
        client.postComposeTweet(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "update - onSuccess");
                Tweet newTweet = Tweet.fromJSON(response);
                if (homeTimelineFragment != null)
                    homeTimelineFragment.addTweetOnTop(newTweet);
                else
                    Log.d(TAG, "updateStatus onSuccess homeTimelineFragment is null");
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


    // Return the order of fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        // Get the manager
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem position: " + position);
            if (position == 0) {
                homeTimelineFragment = new HomeTimelineFragment();
                return homeTimelineFragment;
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void launchProfileActivity(User user) {
        Log.d(TAG, "User: " + user.getScreenName());
/*
        // This did not work. It kept crashing in TweetsListFragment.java - callingContext = (ActivityCommunicator) context;
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", user.getScreenName().toString());
        startActivity(i);
*/
    }
}

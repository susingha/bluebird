package com.codepath.apps.purplebird;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.purplebird.fragments.UserTimeLineFragment;
import com.codepath.apps.purplebird.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    final String TAG = "ProfileActivity";
    TwitterNetworkClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get the screem name from the activity that launches it
        String screenName = getIntent().getStringExtra("screen_name");


        client = TwitterApp.getRestClient();
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "sup:0 onSuccess");
                user = User.fromJSON(response);
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "sup:1 onFailure");
                if(statusCode == TwitterNetworkClient.REST_NO_INTERNET_STATUS_CODE)
                    return;
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "sup:2 onFailure");
                if(statusCode == TwitterNetworkClient.REST_NO_INTERNET_STATUS_CODE)
                    return;
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "sup:3 onFailure");
                if(statusCode == TwitterNetworkClient.REST_NO_INTERNET_STATUS_CODE)
                    return;
                Toast.makeText(getApplicationContext(), "API Rate Limit Exceeded", Toast.LENGTH_SHORT).show();
            }
        });

        if (savedInstanceState == null) {
            // create the user timeline fragment
            UserTimeLineFragment fragmentUserTimeline = UserTimeLineFragment.newInstance(screenName);
            // Display the user fragment dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivPorfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(5, 5)).into(ivPorfileImage);
    }
}

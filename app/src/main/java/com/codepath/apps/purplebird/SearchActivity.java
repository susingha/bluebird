package com.codepath.apps.purplebird;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.apps.purplebird.fragments.SearchTimeLineFragment;


/**
 * Created by supsingh on 11/6/2016.
 */

public class SearchActivity extends AppCompatActivity {
    final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get the query from the activity that launches it
        String searchQuery = getIntent().getStringExtra("search_query");
        TextView tvQuery = (TextView) findViewById(R.id.tvQuery);

        if (savedInstanceState == null) {
            tvQuery.setText("Search: " + searchQuery);
            // create the seacrh timeline fragment
            SearchTimeLineFragment fragmentSearchTimeline = SearchTimeLineFragment.newInstance(searchQuery);
            // Display the search fragment dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flSearchContainer, fragmentSearchTimeline);
            ft.commit();
        }
    }
}

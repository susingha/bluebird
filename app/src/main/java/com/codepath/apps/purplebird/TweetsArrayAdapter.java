package com.codepath.apps.purplebird;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.purplebird.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.apps.purplebird.R.id.ivProfileImage;
import static com.codepath.apps.purplebird.R.id.tvBody;
import static com.codepath.apps.purplebird.R.id.tvId;
import static com.codepath.apps.purplebird.R.id.tvName;
import static com.codepath.apps.purplebird.R.id.tvTimeStamp;
import static com.codepath.apps.purplebird.R.id.tvUserName;
import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/**
 * Created by supsingh on 10/27/2016.
 */

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    // Override and create custom template


    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int position_l = position;
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder = null;
        Log.d(TAG, "getView for position " + position);


        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, null);

            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(ivProfileImage);
            viewHolder.tvName = (TextView) convertView.findViewById(tvName);
            viewHolder.tvUserName = (TextView) convertView.findViewById(tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(tvBody);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(tvTimeStamp);
            viewHolder.tvId = (TextView) convertView.findViewById(tvId);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(5, 5)).into(viewHolder.ivProfileImage);
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "position: " + position_l + " user: " + tweet.getUser().getScreenName());
                Intent i = new Intent(parent.getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tweet.getUser().getScreenName());
                parent.getContext().startActivity(i);
            }
        });
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUserName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimeStamp.setText(tweet.getTimeStamp());
        viewHolder.tvId.setText(tweet.getUid().toString());


        new PatternEditableBuilder().
                addPattern(Pattern.compile("(\\@|\\#)(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                String generic_string = text.substring(1);
                                if (text.charAt(0) == '@') {
                                    Toast.makeText(parent.getContext(), "Loading Profile: " + generic_string, Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(parent.getContext(), ProfileActivity.class);
                                    i.putExtra("screen_name", generic_string);
                                    parent.getContext().startActivity(i);
                                } else if (text.charAt(0) == '#') {
                                    Toast.makeText(parent.getContext(), "Loading Search: " + generic_string, Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(parent.getContext(), SearchActivity.class);
                                    i.putExtra("search_query", generic_string);
                                    parent.getContext().startActivity(i);
                                }
                            }
                        }).into(viewHolder.tvBody);



        return convertView;
    }
}

class ViewHolder {
    public ImageView ivProfileImage;
    public TextView tvName;
    public TextView tvUserName;
    public TextView tvBody;
    public TextView tvTimeStamp;
    public TextView tvId;
}
package com.codepath.apps.purplebird;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.purplebird.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
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
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUserName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimeStamp.setText(tweet.getTimeStamp());
        viewHolder.tvId.setText(tweet.getUid().toString());

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
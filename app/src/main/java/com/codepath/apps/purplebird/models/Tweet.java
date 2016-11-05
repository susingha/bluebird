package com.codepath.apps.purplebird.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by supsingh on 10/27/2016.
 */

public class Tweet {
    public String getBody() {
        return body;
    }

    public Long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTimeStamp() {

        DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        inputFormat.setLenient(true);

        Date currentDate = new Date(System.currentTimeMillis());
        Date tweetDate = null;

        try {
            tweetDate = inputFormat.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = currentDate.getTime() - tweetDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        StringBuilder elapsedString = new StringBuilder();
        int stampCounter = 2;

        if (diffDays > 0 && stampCounter != 0) {
            elapsedString.append(diffDays + "d ");
            stampCounter--;
        }
        if (diffHours > 0 && stampCounter != 0) {
            elapsedString.append(diffHours + "h ");
            stampCounter--;
        }
        if (diffMinutes > 0 && stampCounter != 0) {
            elapsedString.append(diffMinutes + "m ");
            stampCounter--;
        }
        if (diffSeconds > 0 && stampCounter != 0) {
            elapsedString.append(diffSeconds + "s ");
            stampCounter--;
        }

        elapsedString.append("ago");

        Log.d(TAG, diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds.");
        return elapsedString.toString();
    }

    public User getUser() {
        return user;
    }

    public static Long getMax_id() {
        return max_id;
    }

    // Attributes
    private static Long max_id = Long.valueOf(0);
    private Long uid; // data base id for the tweet
    private String body;
    private String createdAt;
    private String timeStamp;
    private User user;

    // Deserialize
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.timeStamp = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.uid = jsonObject.getLong("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    Log.d(TAG, "new tweet id: " + tweet.uid);
                    if (max_id > tweet.uid || max_id == 0) {
                        max_id = tweet.uid;
                        Log.d(TAG, "max_id updated to: " + max_id);
                    }
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}

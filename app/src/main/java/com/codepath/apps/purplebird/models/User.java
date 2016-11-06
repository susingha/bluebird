package com.codepath.apps.purplebird.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by supsingh on 10/27/2016.
 */

public class User {

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followerCount;
    }

    public String getTagLine() {
        return tagline;
    }

    // Attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private int tweetsCount;
    private int followerCount;
    private int followingCount;

    // Deserialize
    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.tweetsCount = json.getInt("statuses_count");
            u.followerCount = json.getInt("followers_count");
            u.followingCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }
}

package com.codepath.apps.purplebird.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by supsingh on 10/27/2016.
 */

public class User {
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // Attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    // Deserialize
    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }
}

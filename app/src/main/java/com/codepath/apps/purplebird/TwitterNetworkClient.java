package com.codepath.apps.purplebird;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.codepath.apps.purplebird.fragments.TweetsListFragment;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import static com.raizlabs.android.dbflow.config.FlowLog.TAG;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */


public class TwitterNetworkClient extends OAuthBaseClient {


    public enum PageType {
        FIRST,
        NEXT,
    }

    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "NelFk49mtKeK3SbceWJomihfi";       // Change this
    public static final String REST_CONSUMER_SECRET = "VgA77OCHM3lKaQWdcBIngv1oWxnJf1uk4foGgM8STN8zyCu3BI"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cppurplebird"; // Change this (here and in manifest)
    public static final int REST_TWEET_COUNT = 25;
    public static final int REST_NO_INTERNET_STATUS_CODE = 999;


    public TwitterNetworkClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }


/*
    // CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}
*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void postComposeTweet(String status, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);

        // Execute the request
        Log.d(TAG, "compose url: " + apiUrl.toString() + "?" + params.toString());
        getClient().post(apiUrl, params, handler);
    }


    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {

        if (isNetworkAvailable() == false) {
            Log.d(TAG, "NO INTERNET");
            handler.onFailure(REST_NO_INTERNET_STATUS_CODE, null, null, null);
            return;
        }

        String apiUrl = null;
        RequestParams params = null;

        if (screenName == null) {
            // show self profilr
            apiUrl = getApiUrl("account/verify_credentials.json");
            Log.d(TAG, "profile url: " + apiUrl.toString());
        } else {
            // show user profilr
            apiUrl = getApiUrl("users/show.json");
            params = new RequestParams();
            params.put("screen_name", screenName);
            Log.d(TAG, "profile url: " + apiUrl.toString() + "?" + params.toString());
        }

        getClient().get(apiUrl, params, handler);
    }


    public void getHomeTimeline(PageType page, Long max_id, AsyncHttpResponseHandler handler) {

        if (isNetworkAvailable() == false) {
            Log.d(TAG, "NO INTERNET");
            handler.onFailure(REST_NO_INTERNET_STATUS_CODE, null, null, null);
            return;
        }

        String apiUrl = getApiUrl("statuses/home_timeline.json");
        // Set the url params
        RequestParams params = new RequestParams();
        params.put("count", REST_TWEET_COUNT);
        if (page == PageType.FIRST) {
            params.put("since_id", 1);
        } else if (page == PageType.NEXT) {
            params.put("max_id", max_id - 1);
        }

        // Execute the request
        Log.d(TAG, "home timeline url: " + apiUrl.toString() + "?" + params.toString());
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(PageType page, Long max_id, AsyncHttpResponseHandler handler) {

        if (isNetworkAvailable() == false) {
            Log.d(TAG, "NO INTERNET");
            handler.onFailure(REST_NO_INTERNET_STATUS_CODE, null, null, null);
            return;
        }

        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        // Set the url params
        RequestParams params = new RequestParams();
        params.put("count", REST_TWEET_COUNT);
        if (page == PageType.FIRST) {
            params.put("since_id", 1);
        } else if (page == PageType.NEXT) {
            params.put("max_id", max_id - 1);
        }

        // Execute the request
        Log.d(TAG, "mentions timeline url: " + apiUrl.toString() + "?" + params.toString());
        getClient().get(apiUrl, params, handler);
    }


    public void getUserTimeline(PageType page, Long max_id, AsyncHttpResponseHandler handler, String screenName) {

        if (isNetworkAvailable() == false) {
            Log.d(TAG, "NO INTERNET");
            handler.onFailure(REST_NO_INTERNET_STATUS_CODE, null, null, null);
            return;
        }

        String apiUrl = getApiUrl("statuses/user_timeline.json");
        // Set the url params
        RequestParams params = new RequestParams();

        if (screenName != null) {
            params.put("screen_name", screenName);
        }

        params.put("count", REST_TWEET_COUNT);
        if (page == PageType.FIRST) {
            params.put("since_id", 1);
        } else if (page == PageType.NEXT) {
            params.put("max_id", max_id - 1);
        }

        // Execute the request
        Log.d(TAG, "user timeline url: " + apiUrl.toString() + "?" + params.toString());
        getClient().get(apiUrl, params, handler);
    }

    public void getSearchTimeline(PageType page, Long max_id, AsyncHttpResponseHandler handler, String searchQuery) {

        if (isNetworkAvailable() == false) {
            Log.d(TAG, "NO INTERNET");
            handler.onFailure(REST_NO_INTERNET_STATUS_CODE, null, null, null);
            return;
        }

        String apiUrl = getApiUrl("search/tweets.json");
        // Set the url params
        RequestParams params = new RequestParams();

        if (searchQuery != null) {
            params.put("q", searchQuery);
        }

        params.put("count", REST_TWEET_COUNT);
        if (page == PageType.FIRST) {
            params.put("since_id", 1);
        } else if (page == PageType.NEXT) {
            params.put("max_id", max_id - 1);
        }

        // Execute the request
        Log.d(TAG, "search timeline url: " + apiUrl.toString() + "?" + params.toString());
        getClient().get(apiUrl, params, handler);
    }


    public void getTimeline(TweetsListFragment.TimelineType timelineType_t, PageType page, Long max_id, AsyncHttpResponseHandler handler, String generic_string) {
        switch (timelineType_t) {
            case TIMELINE_HOME:
                getHomeTimeline(page, max_id, handler);
                break;
            case TIMELINE_MENTIONS:
                getMentionsTimeline(page, max_id, handler);
                break;
            case TIMELINE_USER:
                getUserTimeline(page, max_id, handler, generic_string);
                break;
            case TIMELINE_SEARCH:
                getSearchTimeline(page, max_id, handler, generic_string);
                break;
            default:
                Log.d(TAG, "Timeline Support not available");
                break;
        }
    }


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
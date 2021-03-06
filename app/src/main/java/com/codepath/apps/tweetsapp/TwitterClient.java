package com.codepath.apps.tweetsapp;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "E84zw5XywJzIcv4EWk31vZxWv";       // Change this
	public static final String REST_CONSUMER_SECRET = "5wRNMo1fc8b68ZQykjhbczPjGBD0e2vYC4DIZZFxznTXYgUR9n"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptweetapp"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// GET statuses/home_timeline.json
    // count=25
    // since_id=1 (all tweets sorted by date)
    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        if (maxId != -1) {
            params.put("max_id", maxId);
        }
        getClient().get(apiUrl, params, handler);
    }


    // GET statuses/mentions_timeline.json
    // count=25
    public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        getClient().get(apiUrl, params, handler);
    }

    // POST statuses/update.json
    // status=str
    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        getClient().post(apiUrl, params, handler);
    }

    // POST statuses/update.json
    // status=str
    public void postReplyTweet(String tweet, Long tweet_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        params.put("in_reply_to_status_id", tweet_id);
        getClient().post(apiUrl, params, handler);
    }

    // POST statuses/retweet:id.json
    public void postRetweet(Long tweet_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/"+ tweet_id + ".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    // POST statuses/unretweet:id.json
    public void postUnRetweet(Long tweet_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/unretweet/"+ tweet_id + ".json");
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    // POST favorites/create.json
    // id=num
    public void postFavorite(Long id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        getClient().post(apiUrl, params, handler);
    }


    // POST favorites/create.json
    // id=num
    public void postUnFavorite(Long id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        getClient().post(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, null, handler);
    }


    public void getUserFromScreenName(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void getSearchResults(String search, long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        params.put("q", search);
        params.put("count", 25);
        if (maxId > 0) {
            params.put("max_id", maxId);
        }
        getClient().get(apiUrl, params, handler);
    }
}
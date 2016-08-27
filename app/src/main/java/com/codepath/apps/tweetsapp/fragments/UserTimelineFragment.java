package com.codepath.apps.tweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.tweetsapp.TwitterApplication;
import com.codepath.apps.tweetsapp.TwitterClient;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by cwong on 8/26/16.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
//        if (!isNetworkAvailable() || !isOnline()) {
//            Toast.makeText(getActivity(), "Unable to connect to the internet", Toast.LENGTH_LONG).show();
//            List<TweetModel> queryResults = new Select().from(TweetModel.class)
//                    .orderBy("created_at DESC").limit(40).execute();
//            for (int i = 0; i < queryResults.size(); i++) {
//                add(0, Tweet.fromModel(queryResults.get(i)));
//            }
//        } else {
//            populateTimeline(-1);
//        }
        populateTimeline();
    }

    public static UserTimelineFragment newInstance(String screenname) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenname", screenname);
        userFragment.setArguments(args);
        return userFragment;
    }

    // Send API request to get timeline json
    // fill listview by creating tweet objects
    private void populateTimeline() {
        String screenname = getArguments().getString("screenname");
        client.getUserTimeline(screenname, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(0, Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }
}
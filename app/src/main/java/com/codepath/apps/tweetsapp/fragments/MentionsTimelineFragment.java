package com.codepath.apps.tweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.tweetsapp.TwitterApplication;
import com.codepath.apps.tweetsapp.TwitterClient;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.codepath.apps.tweetsapp.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by carolinewong on 8/24/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
//    public static final String ARG_PAGE = "ARG_PAGE";
//
//    private int mPage;
//
//    public static HomeTimelineFragment newInstance(int page) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
//        HomeTimelineFragment fragment = new HomeTimelineFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //   mPage = getArguments().getInt(ARG_PAGE);
        client = TwitterApplication.getRestClient();
        if (!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(getActivity(), "Unable to connect to the internet", Toast.LENGTH_LONG).show();
            List<TweetModel> queryResults = new Select().from(TweetModel.class)
                    .orderBy("created_at DESC").limit(40).execute();
            for (int i = 0; i < queryResults.size(); i++) {
                add(0, Tweet.fromModel(queryResults.get(i)));
            }
        } else {
            populateTimeline(-1);
        }
    }

    // Send API request to get timeline json
    // fill listview by creating tweet objects
    private void populateTimeline(long maxId) {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
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

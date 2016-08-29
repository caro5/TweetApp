package com.codepath.apps.tweetsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.tweetsapp.R;
import com.codepath.apps.tweetsapp.TweetsAdapter;
import com.codepath.apps.tweetsapp.TwitterApplication;
import com.codepath.apps.tweetsapp.TwitterClient;
import com.codepath.apps.tweetsapp.fragments.ComposeFragment;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by carolinewong on 8/28/16.
 */
public class SearchActivity extends BaseActivity implements ComposeFragment.ComposeFragmentListener {
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsAdapter tweetsAdapter;
    private String query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);

        Intent intent = getIntent();
        query = intent.getStringExtra("search");
        getSupportActionBar().setTitle(query);
        retrieveTweets();
    }


    public void retrieveTweets() {
        showProgressBar();
        long maxId = -1;
        if (tweets.size() > 0) {
            maxId = tweets.get(tweets.size() - 1).getUid();
        }
        client.getSearchResults(query, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ArrayList<Tweet> allTweets = new ArrayList<>();
                try {
                    allTweets = Tweet.fromJSONArray(response.getJSONArray("statuses"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tweets.addAll(0, allTweets);
                tweetsAdapter.notifyDataSetChanged();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideProgressBar();
            }
        });
    }

    @Override
    public void onSuccessfulTweet(Tweet tweet) {
        tweets.add(0, tweet);
    }
}

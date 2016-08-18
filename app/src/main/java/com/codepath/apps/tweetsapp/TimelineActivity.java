package com.codepath.apps.tweetsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.codepath.apps.tweetsapp.models.TweetModel;
import com.codepath.apps.tweetsapp.models.UserModel;
import com.codepath.apps.tweetsapp.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.tweetsapp.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.ComposeFragmentListener {
    @BindView (R.id.rvTweets) RecyclerView rvTweets;
    @BindView (R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.fabCreate) FloatingActionButton fabCreate;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    //private TweetsArrayAdapter aTweets;
    private TweetsAdapter adapter;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(TweetModel.class, UserModel.class);
        ActiveAndroid.initialize(config.create());

        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        client = TwitterApplication.getRestClient();

        if (!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(this, "Unable to connect to the internet", Toast.LENGTH_LONG).show();
            List<TweetModel> queryResults = new Select().from(TweetModel.class)
                    .orderBy("created_at DESC").limit(40).execute();
            for (int i = 0; i < queryResults.size(); i++) {
                TweetModel model = queryResults.get(i);
//                List<Entity> entities = new Select().from(EntityModel.class)
//                        .where("tweet_id = ?", model.remoteId)
//                        .execute();
                tweets.add(Tweet.fromModel(queryResults.get(i)));
            }
        } else {
            populateTimeline(0, -1);
        }
        setupListeners();
    }

    public void setupListeners() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(0, -1);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fabCreate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isOnline() || !isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Connect to the internet before tweeting", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    composeMessage();
                    return true;
                } else {
                    return false;
                }
            }
        });

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOnline() || !isNetworkAvailable()) {
                    Toast.makeText(getApplicationContext(), "Unable to connect to the internet", Toast.LENGTH_LONG).show();
                    return;
                }
                Tweet lastTweet = tweets.get(tweets.size() - 1);
                long tweetId = lastTweet.getUid();
                populateTimeline(page, tweetId);
            }
        });

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Tweet tw = tweets.get(position);
                        Intent i = new Intent(TimelineActivity.this, DetailActivity.class);
                        i.putExtra("tweet", Parcels.wrap(tw));
                        startActivity(i);

                    }
                }
        );
    }

    // Send API request to get timeline json
    // fill listview by creating tweet objects
    private void populateTimeline(int page, long maxId) {
        client.getHomeTimeline(page, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(0, Tweet.fromJSONArray(json));
                adapter.notifyItemRangeInserted(0, json.length());
                // rvTweets.scrollToPosition(0);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void composeMessage() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReply", false);
        composeFragment.setArguments(bundle);
        composeFragment.show(fm, "fragment_compose");
    }

    @Override
    public void onSuccessfulTweet(Tweet t) {
        tweets.add(0, t);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}

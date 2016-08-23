package com.codepath.apps.tweetsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.tweetsapp.fragments.TweetsListFragment;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.codepath.apps.tweetsapp.models.TweetModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeFragment.ComposeFragmentListener {
    @BindView (R.id.toolbar) Toolbar toolbar;

    private TweetsListFragment fragmentTweetsList;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }
        client = TwitterApplication.getRestClient();
        if (!isNetworkAvailable() || !isOnline()) {
            Toast.makeText(this, "Unable to connect to the internet", Toast.LENGTH_LONG).show();
            List<TweetModel> queryResults = new Select().from(TweetModel.class)
                    .orderBy("created_at DESC").limit(40).execute();
            for (int i = 0; i < queryResults.size(); i++) {
                fragmentTweetsList.add(0, Tweet.fromModel(queryResults.get(i)));
            }
        } else {
            populateTimeline(-1);
        }
    }
    // Send API request to get timeline json
    // fill listview by creating tweet objects
    private void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                fragmentTweetsList.addAll(0, Tweet.fromJSONArray(json));
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
        fragmentTweetsList.add(0, t);
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

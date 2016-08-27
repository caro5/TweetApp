package com.codepath.apps.tweetsapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.tweetsapp.ComposeFragment;
import com.codepath.apps.tweetsapp.R;
import com.codepath.apps.tweetsapp.TweetsAdapter;
import com.codepath.apps.tweetsapp.models.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cwong on 8/22/16.
 */
public class TweetsListFragment extends Fragment implements ComposeFragment.ComposeFragmentListener {
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.fabCreate) FloatingActionButton fabCreate;

    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        ButterKnife.bind(this, v);
        layoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);
        setupListeners();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(getActivity(), tweets);

//        Configuration.Builder config = new Configuration.Builder(getActivity());
//        config.addModelClasses(TweetModel.class, UserModel.class);
//        ActiveAndroid.initialize(config.create());

    }
    public void setupListeners() {

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fabCreate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!isOnline() || !isNetworkAvailable()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connect to the internet before tweeting", Toast.LENGTH_LONG).show();
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
    }

    public void composeMessage() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isReply", false);
        composeFragment.setArguments(bundle);
        composeFragment.setTargetFragment(TweetsListFragment.this, 300);
        composeFragment.show(fm, "fragment_compose");
    }

    @Override
    public void onSuccessfulTweet(Tweet t) {
        add(0, t);
    }

    public void addAll(int position, List<Tweet> tweetsList) {
        tweets.addAll(position, tweetsList);
        adapter.notifyItemRangeInserted(0, tweetsList.size());
        rvTweets.scrollToPosition(0);
    }
    public void add(int position, Tweet tweet) {
        tweets.add(position, tweet);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    public Tweet getTweet(int position){
        return tweets.get(position);
    }

    public int getTweetArraySize() {
        return tweets.size();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

package com.codepath.apps.tweetsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweetsapp.R;
import com.codepath.apps.tweetsapp.TweetsAdapter;
import com.codepath.apps.tweetsapp.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by cwong on 8/22/16.
 */
public class TweetsListFragment extends Fragment {
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


        // setupListeners();
    }
//    public void setupListeners() {
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                populateTimeline(-1);
//                swipeContainer.setRefreshing(false);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        fabCreate.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (!isOnline() || !isNetworkAvailable()) {
//                    Toast.makeText(getActivity().getApplicationContext(), "Connect to the internet before tweeting", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    composeMessage();
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//
//        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                if (!isOnline() || !isNetworkAvailable()) {
//                    Toast.makeText(getActivity().getApplicationContext(), "Unable to connect to the internet", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                Tweet lastTweet = tweets.get(tweets.size() - 1);
//                long tweetId = lastTweet.getUid();
//                populateTimeline(tweetId);
//            }
//        });
//    }
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

}

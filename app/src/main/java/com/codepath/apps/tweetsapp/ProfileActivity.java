package com.codepath.apps.tweetsapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetsapp.fragments.TweetsListFragment;
import com.codepath.apps.tweetsapp.fragments.UserTimelineFragment;
import com.codepath.apps.tweetsapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends BaseActivity implements TweetsListFragment.TweetsListFragmentListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    User user;

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        TwitterClient client = TwitterApplication.getRestClient();

        if (getIntent().getStringExtra("screenname") != null) {
            showProgressBar();
            client.getUserFromScreenName(getIntent().getStringExtra("screenname"), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                    checkInstanceState(savedInstanceState);
                    hideProgressBar();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    hideProgressBar();
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else if (getIntent().getParcelableExtra("user") != null) {
            user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
            populateProfileHeader(user);
            checkInstanceState(savedInstanceState);
        } else {
            showProgressBar();
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    // get current user account info
                    getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                    checkInstanceState(savedInstanceState);
                    hideProgressBar();
                }
            });
        }
    }

    private void checkInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // only run if haven't run before
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.commit(); //changes fragment
        }
    }
    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingsCount() + " Following");
        Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivProfileImage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncCallStart() {
        showProgressBar();
    }

    @Override
    public void onAsyncCallEnd() {
        hideProgressBar();
    }
}

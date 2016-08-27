package com.codepath.apps.tweetsapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetsapp.fragments.UserTimelineFragment;
import com.codepath.apps.tweetsapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    TwitterClient client;
    User user;

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // get current user account info
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateProfileHeader(user);
            }
        });
        String screenname = getIntent().getStringExtra("screenname");
        if (savedInstanceState == null) {
            // only run if haven't run before
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenname);
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
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
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
}

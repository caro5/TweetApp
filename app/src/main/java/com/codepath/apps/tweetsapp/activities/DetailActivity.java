package com.codepath.apps.tweetsapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetsapp.fragments.ComposeFragment;
import com.codepath.apps.tweetsapp.R;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.codepath.apps.tweetsapp.utils.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by cwong on 8/17/16.
 */
public class DetailActivity extends BaseActivity implements ComposeFragment.ComposeFragmentListener {
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView (R.id.toolbar) Toolbar toolbar;
    @BindView (R.id.ivReply) ImageView ivReply;
    @BindView(R.id.ivEntity) ImageView ivEntity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_tweet);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        ParseRelativeDate parseRelativeDate = new ParseRelativeDate();
        tvDate.setText(parseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(2, 2)).into(ivProfileImage);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivEntity.setImageResource(0);
        if (tweet.getEntities() != null && tweet.getEntities().size() > 0) {
            Picasso.with(this).load(tweet.getEntities().get(0).mediaUrl).transform(new RoundedCornersTransformation(2, 2)).into(ivEntity);
        }

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeFragment composeFragment = ComposeFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putParcelable("tweet", Parcels.wrap(tweet));
                bundle.putBoolean("isReply", true);
                composeFragment.setArguments(bundle);
                composeFragment.show(fm, "fragment_compose");
            }
        });
    }
    @Override
    public void onSuccessfulTweet(Tweet t) {
        Toast.makeText(this, "Successfully replied", Toast.LENGTH_LONG).show();
    }
}


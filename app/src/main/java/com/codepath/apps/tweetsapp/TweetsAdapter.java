package com.codepath.apps.tweetsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetsapp.activities.DetailActivity;
import com.codepath.apps.tweetsapp.activities.ProfileActivity;
import com.codepath.apps.tweetsapp.activities.SearchActivity;
import com.codepath.apps.tweetsapp.fragments.ComposeFragment;
import com.codepath.apps.tweetsapp.models.Tweet;
import com.codepath.apps.tweetsapp.models.TweetModel;
import com.codepath.apps.tweetsapp.utils.ParseRelativeDate;
import com.codepath.apps.tweetsapp.utils.PatternEditableBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by cwong on 8/16/16.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> implements ComposeFragment.ComposeFragmentListener {
    private ArrayList<Tweet> mTweets;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivProfileImage)ImageView ivProfileImage;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvScreenName) TextView tvScreenName;
        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.ivEntity) ImageView ivEntity;
        @BindView(R.id.ivRetweets) ImageView ivRetweets;
        @BindView(R.id.ivFavorites) ImageView ivFavorites;
        @BindView(R.id.ivReply) ImageView ivReply;
        @BindView(R.id.tvFavoritesCount) TextView tvFavoritesCount;
        @BindView(R.id.tvRetweetCount) TextView tvRetweetCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Tweet tw = mTweets.get(getLayoutPosition());
            Intent i = new Intent(view.getContext(), DetailActivity.class);
            i.putExtra("tweet", Parcels.wrap(tw));
            view.getContext().startActivity(i);
        }

        @OnClick(R.id.ivRetweets)
        public void toggleRetweet(View view) {
            final int position = getLayoutPosition();
            final Tweet tweet = mTweets.get(position);
            final ArrayList<Long> entityIds = new ArrayList<>();
            for (int i = 0; i < tweet.getEntities().size(); i++) {
                entityIds.add(tweet.getEntities().get(i).getId());
            }
            TwitterClient client = new TwitterClient(mContext);
            if (tweet.isRetweeted()) {
                // unretweet
                client.postUnRetweet(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        TweetModel model = Tweet.getByUID(tweet.getUid());
                        model.setRetweeted(false);
                        model.setRetweetCount(model.retweetCount - 1);
                        model.save();
                        notifyItemChanged(position);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(mContext, "Unable to unretweet ", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                client.postRetweet(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        TweetModel model = Tweet.getByUID(tweet.getUid());
                        model.setRetweeted(true);
                        model.setRetweetCount(model.retweetCount + 1);
                        model.save();
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(mContext, "Unable to retweet ", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
        @OnClick(R.id.ivFavorites)
        public void toggleFavorites(View view) {
            final int position = getLayoutPosition();
            final Tweet tweet = mTweets.get(position);
            final ArrayList<Long> entityIds = new ArrayList<>();
            for (int i = 0; i < tweet.getEntities().size(); i++) {
                entityIds.add(tweet.getEntities().get(i).getId());
            }
            TwitterClient client = new TwitterClient(mContext);
            if (tweet.isFavorited()) {
                // unfavorite
                client.postUnFavorite(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        TweetModel model = Tweet.getByUID(tweet.getUid());
                        model.setFavorited(false);
                        model.setFavouritesCount(model.favouritesCount - 1);
                        model.save();
                        tweet.setFavorited(false);
                        notifyItemChanged(position);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(mContext, "Unable to unlike ", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                client.postFavorite(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        TweetModel model = Tweet.getByUID(tweet.getUid());
                        model.setFavorited(true);
                        model.setFavouritesCount(model.favouritesCount + 1);
                        model.save();
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(mContext, "Unable to like ", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        @OnClick(R.id.ivReply)
        public void replyTweet(View v) {
            final int position = getLayoutPosition();
            final Tweet tweet = mTweets.get(position);
            FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
            ComposeFragment composeFragment = ComposeFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable("tweet", Parcels.wrap(tweet));
            bundle.putBoolean("isReply", true);
            composeFragment.setArguments(bundle);
            composeFragment.setTargetFragment(fm.getFragments().get(0), 300);
            composeFragment.show(fm, "fragment_compose");
        }

        @OnClick(R.id.ivProfileImage)
        public void goToProfile(View v) {
            final int position = getLayoutPosition();
            final Tweet tweet = mTweets.get(position);
            Intent i = new Intent(getContext(), ProfileActivity.class);
            i.putExtra("user", Parcels.wrap(tweet.getUser()));
            getContext().startActivity(i);
        }
    }

    @Override
    public void onSuccessfulTweet(Tweet t) {
        Toast.makeText(mContext, "Successfully replied", Toast.LENGTH_LONG).show();
    }

    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }
    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TweetsAdapter.ViewHolder viewHolder, int position) {
        Tweet tweet = mTweets.get(position);
        try {
            TweetModel m = Tweet.getByUID(tweet.getUid());
            tweet = Tweet.fromModel(m);
        } catch (Exception exception) {
        }

        mTweets.set(position, tweet);

        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        ParseRelativeDate parseRelativeDate = new ParseRelativeDate();
        viewHolder.tvDate.setText(parseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(2, 2)).into(viewHolder.ivProfileImage);
        viewHolder.ivEntity.setImageResource(android.R.color.transparent);
        if (tweet.getEntities() != null && tweet.getEntities().size() > 0) {
            Picasso.with(getContext()).load(tweet.getEntities().get(0).getMediaUrl()).transform(new RoundedCornersTransformation(2, 2)).into(viewHolder.ivEntity);
        }
        if (tweet.isRetweeted()) {
            viewHolder.ivRetweets.setImageResource(R.drawable.retweeted);
        } else {
            viewHolder.ivRetweets.setImageResource(R.drawable.retweet);
        }
        if (tweet.isFavorited()) {
            viewHolder.ivFavorites.setImageResource(R.drawable.liked);
        } else {
            viewHolder.ivFavorites.setImageResource(R.drawable.like);
        }
        viewHolder.tvFavoritesCount.setText(Integer.toString(tweet.getFavouritesCount()));
        viewHolder.tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));

        new PatternEditableBuilder().addPattern(Pattern.compile("\\@(\\w+)"), ContextCompat.getColor(getContext(), R.color.clickable_names),
            new PatternEditableBuilder.SpannableClickedListener() {
                @Override
                public void onSpanClicked(String text) {
                    clickedText(text);
                }
            }).addPattern(Pattern.compile("\\#(\\w+)"), ContextCompat.getColor(getContext(), R.color.clickable_tags),
                new PatternEditableBuilder.SpannableClickedListener() {
                    @Override
                    public void onSpanClicked(String text) {
                        clickedHashtag(text);
                    }
                }).into(viewHolder.tvBody);
    }

    public void clickedText(String screenname) {
        String name = screenname.substring(1);
        Intent i = new Intent(mContext, ProfileActivity.class);
        i.putExtra("screenname", name);
        mContext.startActivity(i);
    }
    public void clickedHashtag(String tag) {
        String tagString = tag.substring(1);
        Intent i = new Intent(mContext, SearchActivity.class);
        i.putExtra("search", tagString);
        mContext.startActivity(i);
    }
    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}

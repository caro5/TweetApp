package com.codepath.apps.tweetsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetsapp.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cwong on 8/15/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {


    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }


    // override and setup custom template
    // ViewHolder

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. get tweet
        // 2. find or inflate template
        // 3. find subviews to fill with data
        // 4. populate data into subviews
        // 5. return view
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
//        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
//        @BindView(R.id.tvBody) TextView tvBody;
//        @BindView(R.id.tvUserName) TextView tvUserName;
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);

        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        return convertView;
    }
}

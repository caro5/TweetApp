package com.codepath.apps.tweetsapp.models;

import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by cwong on 8/15/16.
 */
@Parcel
public class Tweet {

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    private String body;
    private long uid;
    private User user;
    private String createdAt;

    public Tweet() {}

    public Tweet(String body, long uid, User user, String createdAt) {
        this.body = body;
        this.uid = uid;
        this.user = user;
        this.createdAt = createdAt;
    }

    //Deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            TweetModel tweetModel = Tweet.getByUID(tweet.getUid());

            if (tweetModel == null) {
                UserModel userModel = User.getByUID(tweet.user.getUid());
                //save each tweet
                if (userModel == null) {
                    userModel = tweet.user.saveUser();
                }
                tweet.saveTweet(userModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static TweetModel getByUID(long uid) {
        return new Select()
                .from(TweetModel.class)
                .where("remote_id = ?", uid)
                .executeSingle();
    }

    public void saveTweet(UserModel user) {
        TweetModel model = new TweetModel(this.getUid(), this.getBody(), this.getCreatedAt(), user);
        model.save();
    }
    public static Tweet fromModel(TweetModel model) {
        Tweet t = new Tweet();
        t.body = model.body;
        t.uid = model.remoteId;
        t.createdAt = model.createdAt;
        t.user = User.fromModel(model.user);
        return t;
    }
}

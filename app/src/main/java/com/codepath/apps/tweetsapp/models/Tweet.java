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

    public int getRetweetCount() {
        return retweetCount;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public int retweetCount;

    public void setRetweetCount(int retweetCount) {
        TweetModel model = Tweet.getByUID(this.getUid());
        this.retweetCount = retweetCount;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean retweeted;
    public int favouritesCount;
    public boolean favorited;
    public ArrayList<Entity> entities;

    public Tweet() {}

    public Tweet(String body, long uid, User user, String createdAt, int retweetCount, boolean retweeted, int favouritesCount, boolean favorited, ArrayList<Entity> entities) {
        this.body = body;
        this.uid = uid;
        this.user = user;
        this.createdAt = createdAt;
        this.retweetCount = retweetCount;
        this.retweeted = retweeted;
        this.favouritesCount = favouritesCount;
        this.favorited = favorited;
        this.entities = entities;
    }

    //Deserialize JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.favouritesCount = jsonObject.getInt("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.entities = new ArrayList<Entity>();

            TweetModel tweetModel = Tweet.getByUID(tweet.getUid());

            if (tweetModel == null) {
                UserModel userModel = User.getByUID(tweet.user.getUid());
                //save each tweet
                if (userModel == null) {
                    userModel = tweet.user.saveUser();
                }
                tweet.saveTweet(userModel);
            }
            // save tweet first
            if (jsonObject.getJSONObject("entities").has("media")) {
                tweet.entities = Entity.fromJSONArray(jsonObject.getJSONObject("entities").getJSONArray("media"));
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
        ArrayList<Long> entityIds = new ArrayList<>();
        if (this.getEntities().size() > 0) {
            for (int i = 0; i < this.getEntities().size(); i++) {
                entityIds.add(this.getEntities().get(i).getId());
            }
        }
        TweetModel model = new TweetModel(this.getUid(), this.getBody(), this.getCreatedAt(), user, this.getRetweetCount(), this.isRetweeted(), this.getFavouritesCount(), this.isFavorited(), entityIds);
        model.save();
    }
    public static Tweet fromModel(TweetModel model) {
        Tweet t = new Tweet();
        t.body = model.body;
        t.uid = model.remoteId;
        t.createdAt = model.createdAt;
        t.user = User.fromModel(model.user);
        t.retweetCount = model.retweetCount;
        t.retweeted = model.retweeted;
        t.favouritesCount = model.favouritesCount;
        t.favorited = model.favorited;
        t.entities = new ArrayList<Entity>();
        if (model.entityIds != null) {
            for (int i = 0; i < model.entityIds.size(); i++) {
                Entity e = Entity.fromModel(Entity.getById(model.entityIds.get(i)));
                t.entities.add(e);
            }
        }
        return t;
    }
}

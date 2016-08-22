package com.codepath.apps.tweetsapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by cwong on 8/15/16.
 */
@Table(name = "Tweets")
public class TweetModel extends Model {
    // unique id returned from server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "body", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String body;

    @Column(name = "created_at", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String createdAt;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public UserModel user;

    @Column(name = "retweet_count", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int retweetCount;

    @Column(name = "retweeted", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public boolean retweeted;

    @Column(name = "favourites_count", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int favouritesCount;

    @Column(name = "favorited", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public boolean favorited;

    // need default constructor for every ActiveAndroid model
    public TweetModel() {
        super();
    }

    public TweetModel(long remoteId, String body, String createdAt, UserModel user, int retweetCount, boolean retweeted, int favouritesCount, boolean favorited) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.retweetCount = retweetCount;
        this.retweeted = retweeted;
        this.favouritesCount = favouritesCount;
        this.favorited = favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }
}

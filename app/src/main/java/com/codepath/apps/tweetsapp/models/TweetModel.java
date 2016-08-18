package com.codepath.apps.tweetsapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

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

    @Column(name = "retweet_count", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public int retweetCount;

    @Column(name = "retweeted", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public boolean retweeted;

    @Column(name = "favourites_count", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public int favouritesCount;

    @Column(name = "favorited", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public boolean favorited;

    @Column(name = "entities", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ArrayList<Long> entityIds;


    // need default constructor for every ActiveAndroid model
    public TweetModel() {
        super();
    }

    public TweetModel(long remoteId, String body, String createdAt, UserModel user, int retweetCount, boolean retweeted, int favouritesCount, boolean favorited, ArrayList<Long> entityIds) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.retweetCount = retweetCount;
        this.retweeted = retweeted;
        this.favouritesCount = favouritesCount;
        this.favorited = favorited;
        this.entityIds = entityIds;
    }
}

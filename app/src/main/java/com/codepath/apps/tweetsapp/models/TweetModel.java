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

    // need default constructor for every ActiveAndroid model
    public TweetModel() {
        super();
    }

    public TweetModel(long remoteId, String body, String createdAt, UserModel user) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
    }
}

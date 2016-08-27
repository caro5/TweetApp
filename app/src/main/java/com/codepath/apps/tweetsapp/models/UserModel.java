package com.codepath.apps.tweetsapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by cwong on 8/15/16.
 */
@Table(name = "User")
public class UserModel extends Model {
    // unique id returned from server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String name;

    @Column(name = "screen_name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String screenName;

    @Column(name = "profile_image_url", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String profileImageUrl;

    @Column(name = "tagline", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String tagline;

    @Column(name = "followers_count", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int followersCount;

    @Column(name = "following_count", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int followingsCount;

    // need default constructor for every ActiveAndroid model
    public UserModel() {
        super();
    }

    public UserModel(long remoteId, String name, String screenName, String profileImageUrl, String tagline, int followersCount, int followingsCount) {
        super();
        this.remoteId = remoteId;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
    }
}

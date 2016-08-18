package com.codepath.apps.tweetsapp.models;

/**
 * Created by cwong on 8/17/16.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by cwong on 8/15/16.
 */
@Table(name = "Entity")
public class EntityModel extends Model {
    // unique id returned from server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long id;

    @Column(name = "media_url", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String mediaUrl;

    @Column(name = "type", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String type;

    // need default constructor for every ActiveAndroid model
    public EntityModel() {
        super();
    }

    public EntityModel(long id, String mediaUrl, String type) {
        super();
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.type = type;
    }
}

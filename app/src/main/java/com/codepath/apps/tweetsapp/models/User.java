package com.codepath.apps.tweetsapp.models;

import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by cwong on 8/15/16.
 */
@Parcel
public class User {
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    private String tagline;
    private int followersCount;
    private int followingsCount;


    public User() {}

    public User(String name, long uid, String screenName, String profileImageUrl, String tagline, int followersCount, int followingsCount) {
        this.name = name;
        this.uid = uid;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.tagline = tagline;
        this.followersCount = followersCount;
        this.followingsCount = followingsCount;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.tagline = jsonObject.getString("description");
            u.followersCount = jsonObject.getInt("followers_count");
            u.followingsCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static UserModel getByUID(long uid) {
        return new Select()
                .from(UserModel.class)
                .where("remote_id = ?", uid)
                .executeSingle();
    }

    public UserModel saveUser() {
        UserModel model = new UserModel(this.getUid(), this.getName(), this.getScreenName(), this.getProfileImageUrl(), this.getTagline(), this.getFollowersCount(), this.getFollowingsCount());
        model.save();
        return model;
    }

    public static User fromModel(UserModel model) {
        User u = new User();
        u.name = model.name;
        u.uid = model.remoteId;
        u.screenName = model.screenName;
        u.profileImageUrl = model.profileImageUrl;
        u.tagline = model.tagline;
        u.followersCount = model.followersCount;
        u.followingsCount = model.followingsCount;
        return u;
    }
}

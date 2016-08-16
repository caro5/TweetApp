package com.codepath.apps.tweetsapp.models;

import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cwong on 8/15/16.
 */
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

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public static User fromJSON(JSONObject jsonObject) {
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
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
        UserModel model = new UserModel(this.getUid(), this.getName(), this.getScreenName(), this.getProfileImageUrl());
        model.save();
        return model;
    }

    public static User fromModel(UserModel model) {
        User u = new User();
        u.name = model.name;
        u.uid = model.remoteId;
        u.screenName = model.screenName;
        u.profileImageUrl = model.profileImageUrl;
        return u;
    }
}

package com.codepath.apps.tweetsapp.models;

/**
 * Created by cwong on 8/17/16.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by cwong on 8/15/16.
 */
@Parcel
public class Entity {
    public long getId() {
        return id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getType() {
        return type;
    }

    public long id;
    public String mediaUrl;
    public String type;

    public Entity() {}

    public Entity(long id, String mediaUrl, String type) {
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.type = type;
    }

    //Deserialize JSON
    public static Entity fromJSON(JSONObject jsonObject) {
        Entity ent = new Entity();
        try {
            ent.id = jsonObject.getLong("id");
            ent.mediaUrl = jsonObject.getString("media_url");
            ent.type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ent;
    }
    public static ArrayList<Entity> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject entityJSON = jsonArray.getJSONObject(i);
                Entity ent = Entity.fromJSON(entityJSON);
                if (ent != null) {
                    entities.add(ent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return entities;
    }
}

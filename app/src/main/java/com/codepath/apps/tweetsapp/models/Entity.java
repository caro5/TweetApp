package com.codepath.apps.tweetsapp.models;

/**
 * Created by cwong on 8/17/16.
 */

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

    private long id;
    private String mediaUrl;
    private String type;

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

            EntityModel entityModel = Entity.getById(ent.getId());

            if (entityModel == null) {
                ent.saveEntity();
            }
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

    public static EntityModel getById(long id) {
        return new Select()
                .from(EntityModel.class)
                .where("remote_id = ?", id)
                .executeSingle();
    }

    public void saveEntity() {
        EntityModel model = new EntityModel(this.getId(), this.getMediaUrl(), this.getType());
        model.save();
    }
    public static Entity fromModel(EntityModel model) {
        Entity entity = new Entity();
        entity.id = model.id;
        entity.mediaUrl = model.mediaUrl;
        entity.type = model.type;
        return entity;
    }
}

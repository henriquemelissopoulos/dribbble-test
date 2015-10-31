package com.henriquemelissopoulos.dribbbletest.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by h on 30/10/15.
 */

@RealmClass
public class User extends RealmObject {

    @PrimaryKey private int id;
    private String name = "";
    @SerializedName("avatar_url") private String avatar = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

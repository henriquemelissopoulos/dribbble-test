package com.henriquemelissopoulos.dribbbletest.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by h on 30/10/15.
 */

@RealmClass
public class Shot extends RealmObject {

    public static final String FIELD_SHOT_ID = "id";

    @PrimaryKey private int id;
    private String title = "";
    private String description = "";
    @SerializedName("views_count") private int viewCount = 0;
    private boolean animated = false;
    @SerializedName("html_url") private String url = "";
    @SerializedName("user") private User user = new User();
    @SerializedName("images") private Image images = new Image();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

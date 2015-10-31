package com.henriquemelissopoulos.dribbbletest.model;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by h on 30/10/15.
 */

@RealmClass
public class Image extends RealmObject {

    private String hidpi = "";
    private String normal = "";
    private String teaser = "";

    public String getHidpi() {
        return hidpi;
    }

    public void setHidpi(String hidpi) {
        this.hidpi = hidpi;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }
}

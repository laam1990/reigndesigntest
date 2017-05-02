package com.reigndesign.model;

import io.realm.Realm;
import io.realm.RealmObject;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class Hits extends RealmObject {

    private String title;

    @PrimaryKey
    private int objectID;

    private String createdAt;

    private String author;

    private String url;

    public Hits() {
    }

    public Hits(String title, int objectID, String createdAt, String author, String url) {
        this.title = title;
        this.objectID = objectID;
        this.createdAt = createdAt;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

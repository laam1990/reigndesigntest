package com.reigndesign.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class Hits {

    @SerializedName("title")
    private String title;

    @SerializedName("story_title")
    private String storyTitle;

    @SerializedName("objectID")
    private int objectID;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("author")
    private String author;

    public Hits() {}

    public Hits(String title, String storyTitle, int objectID, String author, String createdAt) {
        this.title = title;
        this.storyTitle = storyTitle;
        this.objectID = objectID;
        this.author = author;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
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
}

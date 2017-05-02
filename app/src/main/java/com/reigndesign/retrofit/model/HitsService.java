package com.reigndesign.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class HitsService {

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

    @SerializedName("story_url")
    private String storyUrl;

    @SerializedName("url")
    private String url;

    public HitsService() {}

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

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

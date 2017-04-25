package com.reigndesign.model;

/**
 * Created by Luis Adrian on 24/04/2017.
 */

public class modelHits {

    private String title;

    private String storyTitle;

    private int objectID;

    private String createdAt;

    private String author;

    private String url;

    private String storyUrl;

    public modelHits() {
    }

    public modelHits(String title, String storyTitle, int objectID, String createdAt, String author, String url, String storyUrl) {
        this.title = title;
        this.storyTitle = storyTitle;
        this.objectID = objectID;
        this.createdAt = createdAt;
        this.author = author;
        this.url = url;
        this.storyUrl = storyUrl;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }
}

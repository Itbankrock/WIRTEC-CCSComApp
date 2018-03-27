package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 20/03/2018.
 */

public class ReviewComment {
    private String id;
    private String userID;
    private String postID;
    private String content;
    private String timestamp;
    private boolean isActive;

    public ReviewComment(String id, String userID, String postID, String content, String timestamp, boolean isActive) {
        this.id = id;
        this.userID = userID;
        this.postID = postID;
        this.content = content;
        this.timestamp = timestamp;
        this.isActive = isActive;
    }

    public ReviewComment(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

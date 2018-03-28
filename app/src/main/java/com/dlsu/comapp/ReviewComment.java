package com.dlsu.comapp;

/**
 * Created by Enrico Zabayle on 20/03/2018.
 */

public class ReviewComment {
    private String id;
    private String userID;
    private String targetreviewID;
    private String content;
    private String timestamp;
    private String lastupdated;
    private boolean isActive;

    public ReviewComment(String id, String userID, String reviewID, String content, String timestamp, String lastupdated, boolean isActive) {
        this.id = id;
        this.userID = userID;
        this.targetreviewID = reviewID;
        this.content = content;
        this.timestamp = timestamp;
        this.isActive = isActive;
        this.lastupdated = lastupdated;
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

    public String getTargetreviewID() {
        return targetreviewID;
    }

    public void setTargetreviewID(String targetreviewID) {
        this.targetreviewID = targetreviewID;
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

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }
}

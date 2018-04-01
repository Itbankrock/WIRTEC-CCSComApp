package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew Santiago on 3/11/2018.
 */

public class ThreadPost implements Parcelable {
    private String id;
    private String userID;
    private String postID;
    private String content;
    private String timestamp;
    private String lastupdated;
    private boolean isActive;

    public ThreadPost(String id, String userID, String postID, String content, String timestamp, String lastupdated, boolean isActive) {
        this.id = id;
        this.userID = userID;
        this.postID = postID;
        this.content = content;
        this.timestamp = timestamp;
        this.isActive = isActive;
        this.lastupdated = lastupdated;
    }

    protected ThreadPost(Parcel in) {
        id = in.readString();
        userID = in.readString();
        postID = in.readString();
        content = in.readString();
        timestamp = in.readString();
        isActive = true;
        lastupdated = in.readString();
    }

    public static final Creator<ThreadPost> CREATOR = new Creator<ThreadPost>() {
        @Override
        public ThreadPost createFromParcel(Parcel in) {
            return new ThreadPost(in);
        }

        @Override
        public ThreadPost[] newArray(int size) {
            return new ThreadPost[size];
        }
    };

    public ThreadPost(){}

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

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userID);
        parcel.writeString(postID);
        parcel.writeString(content);
        parcel.writeString(timestamp);
        parcel.writeString(lastupdated);
    }
}

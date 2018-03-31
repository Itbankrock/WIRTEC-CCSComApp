package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew Santiago on 3/10/2018.
 */

public class ForumThread implements Parcelable{
    private String id;
    private String title;
    private String userID;
    private String timestamp;
    private String content;
    private long views;
    private boolean isActive;

    public ForumThread(String id, String title, String userID, String timestamp, long views, boolean isActive, String content) {
        this.id = id;
        this.title = title;
        this.userID = userID;
        this.timestamp = timestamp;
        this.views = views;
        this.isActive = isActive;
        this.content = content;
    }

    public ForumThread(){}

    protected ForumThread(Parcel in) {
        id= in.readString();
        title= in.readString();
        userID= in.readString();
        timestamp= in.readString();
        content= in.readString();
        views= in.readLong();
    }

    public static final Creator<ForumThread> CREATOR = new Creator<ForumThread>() {
        @Override
        public ForumThread createFromParcel(Parcel in) {
            return new ForumThread(in);
        }

        @Override
        public ForumThread[] newArray(int size) {
            return new ForumThread[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(userID);
        parcel.writeString(timestamp);
        parcel.writeString(content);
        parcel.writeLong(views);
    }

}

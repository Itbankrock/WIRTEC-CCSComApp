package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Enrico Zabayle on 09/03/2018.
 */

public class Note implements Parcelable {
    private String id, courseID, title, author, content, date, lastupdated;

    public Note (String id, String courseID, String title, String author, String content, String date, String lastupdated) {
        this.id = id;
        this.courseID = courseID;
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = date;
        this.lastupdated = lastupdated;
    }

    public Note(){}

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        courseID = in.readString();
        author = in.readString();
        content = in.readString();
        date = in.readString();
        lastupdated = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeString(lastupdated);
        parcel.writeString(courseID);
    }
}

package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew Santiago on 3/9/2018.
 */

public class User implements Parcelable {
    private String googleuid;
    private String name;
    private String email;
    private String photourl;
    private String phonenumber;
    private String course;
    private String address;

    public User(String uid, String name,String email, String photourl, String phonenumber, String course, String address) {
        this.name = name;
        this.googleuid = uid;
        this.email = email;
        this.photourl = photourl;
        this.phonenumber = phonenumber;
        this.course = course;
        this.address = address;
    }
    public User(){}

    protected User(Parcel in) {
        googleuid = in.readString();
        name = in.readString();
        email = in.readString();
        photourl = in.readString();
        phonenumber = in.readString();
        course = in.readString();
        address = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleuid() {
        return googleuid;
    }

    public void setGoogleuid(String googleuid) {
        this.googleuid = googleuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(googleuid);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(photourl);
        parcel.writeString(phonenumber);
        parcel.writeString(course);
        parcel.writeString(address);
    }
}

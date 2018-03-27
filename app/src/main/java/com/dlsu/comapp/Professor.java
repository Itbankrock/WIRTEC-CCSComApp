package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Enrico Zabayle on 08/03/2018.
 */

public class Professor implements Parcelable {
    private String id, name, email, phone, rating, department, position, photourl;

    public Professor(){}

    public Professor(String id, String name, String email, String rating, String department, String position, String photourl){
        this.id = id;
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.department = department;
        this.position = position;
        this.photourl = photourl;
    }

    public Professor(String name, String email, String rating, String department, String position, String photourl){
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.department = department;
        this.position = position;
        this.photourl = photourl;
    }


    protected Professor(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        rating = in.readString();
        department = in.readString();
        position = in.readString();
        photourl = in.readString();
    }

    public static final Creator<Professor> CREATOR = new Creator<Professor>() {
        @Override
        public Professor createFromParcel(Parcel in) {
            return new Professor(in);
        }

        @Override
        public Professor[] newArray(int size) {
            return new Professor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(rating);
        parcel.writeString(department);
        parcel.writeString(position);
        parcel.writeString(photourl);
        parcel.writeString(phone);
    }
}

package com.dlsu.comapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Course implements Parcelable {
    private String id, code, desc, units, overview;

    public Course(String code, String desc, String units, String overview){
        this.code = code;
        this.desc = desc;
        this.units = units;
        this.overview = overview;
    }

    public Course(){}

    public Course(String id, String code, String desc, String units, String overview){
        this. id = id;
        this.code = code;
        this.desc = desc;
        this.units = units;
        this.overview = overview;
    }

    protected Course(Parcel in) {
        id = in.readString();
        code = in.readString();
        desc = in.readString();
        units = in.readString();
        overview = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnits() {
        return units;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(code);
        parcel.writeString(desc);
        parcel.writeString(units);
        parcel.writeString(overview);
    }
}

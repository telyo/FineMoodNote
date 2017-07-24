package com.telyo.finemoodnote.entity;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class RecyclerPlans implements Parcelable{
    private String date;
    private String time;
    private String done_state;
    private String title;
    private String set_time;

    private List<RecyclerDescribe> describes;

    public RecyclerPlans(){

    }
    protected RecyclerPlans(Parcel in) {
        date = in.readString();
        time = in.readString();
        done_state = in.readString();
        title = in.readString();
        set_time = in.readString();
    }

    public static final Creator<RecyclerPlans> CREATOR = new Creator<RecyclerPlans>() {
        @Override
        public RecyclerPlans createFromParcel(Parcel in) {
            return new RecyclerPlans(in);
        }

        @Override
        public RecyclerPlans[] newArray(int size) {
            return new RecyclerPlans[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDone_state() {
        return done_state;
    }

    public void setDone_state(String done_state) {
        this.done_state = done_state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSet_time() {
        return set_time;
    }

    public void setSet_time(String set_time) {
        this.set_time = set_time;
    }

    public List<RecyclerDescribe> getDescribes() {
        return describes;
    }

    public void setDescribes(List<RecyclerDescribe> describes) {
        this.describes = describes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(done_state);
        dest.writeString(title);
        dest.writeString(set_time);
    }
}

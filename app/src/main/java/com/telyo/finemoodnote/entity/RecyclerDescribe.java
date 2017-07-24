package com.telyo.finemoodnote.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/17.
 */

public class RecyclerDescribe implements Parcelable {
    private String describe;
    private boolean isFinished;
    private String planLevel;

    public RecyclerDescribe(){

    }

    public String getPlanLevel() {
        return planLevel;
    }

    public void setPlanLevel(String planLevel) {
        this.planLevel = planLevel;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(describe);
        parcel.writeString(String.valueOf(isFinished));
        parcel.writeString(planLevel);
    }

    protected RecyclerDescribe(Parcel in){
        describe = in.readString();
        isFinished = Boolean.parseBoolean(in.readString());
        planLevel = in.readString();
    }
    public static final Creator<RecyclerDescribe> CREATOR = new Creator<RecyclerDescribe>() {
        @Override
        public RecyclerDescribe createFromParcel(Parcel parcel) {
            return new RecyclerDescribe(parcel);
        }

        @Override
        public RecyclerDescribe[] newArray(int i) {
            return new RecyclerDescribe[i];
        }
    };
}

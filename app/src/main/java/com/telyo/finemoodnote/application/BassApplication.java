package com.telyo.finemoodnote.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/7/19.
 */

public class BassApplication extends Application {
    private static BassApplication bassApplication;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static BassApplication getInstance(){
        if (bassApplication == null){
            bassApplication = new BassApplication();
        }
        return bassApplication;
    }
}

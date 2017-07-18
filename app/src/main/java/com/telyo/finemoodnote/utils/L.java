package com.telyo.finemoodnote.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/18.
 */

public class L {
    private static boolean isLog = true;

    public static void i(String i){
        if (isLog){
            Log.i(Constants.FINE_MOOD_NOTE,i);
        }
    }
    public static void d(String d){
        if (isLog){
            Log.d(Constants.FINE_MOOD_NOTE,d);
        }
    }
    public static void w(String w){
        if (isLog){
            Log.w(Constants.FINE_MOOD_NOTE,w);
        }
    }
    public static void e(String e){
        if (isLog){
            Log.e(Constants.FINE_MOOD_NOTE,e);
        }
    }
}

package com.telyo.finemoodnote.utils;

import com.telyo.finemoodnote.entity.RecyclerPlans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class DateUtil {

    public static boolean isUsefulDate(String selectedDay, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        String today = getToday(type);
        Date todayData = null;
        Date selectData = null;
        try {
            todayData = sdf.parse(today);
            selectData = sdf.parse(selectedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ((todayData.getTime() - selectData.getTime()) > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String getToday(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static long getLongFromStringDate(String date, String type){
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        try {
           Date date1 =  sdf.parse(date);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取最近的计划position
    public static int getCurrentPlanPosition(List<RecyclerPlans> plans,String type){
        int position = 0;
        long todayTime = getLongFromStringDate(getToday(type),type);
        long cutNum = 0;
        for (int i = 0;i<plans.size();i++) {
            long planTime = getLongFromStringDate(plans.get(i).getSet_time(),type);
            long temp = Math.abs(planTime - todayTime);
            if (i == 0){
                cutNum = temp;
            }
            if (temp < cutNum){
                cutNum = temp;
            }
        }
        for (int j = 0;j < plans.size();j++){
            if (Math.abs(getLongFromStringDate(plans.get(j).getSet_time(),
                    type) - todayTime) == cutNum){
                position  = j;
                return position;
            }
        }
        return position;
    }


    public static String getTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        //时
        Long hour = ms / hh;
        String h = "";
        if (hour > 0 && hour < 10){
            h = "0" + hour + ":";
        }else if (hour > 10){
            h = hour + ":";
        }
        //分
        Long minute = (ms  - hour * hh) / mi;
        String m = "00:";
        if (minute > 0 && minute < 10){
            m = "0" + minute+":";
        }else if (minute > 10){
            m = minute + ":";
        }
        //秒
        Long second = (ms - hour * hh - minute * mi) / ss;
        String s = "00";
        if (second > 0 && second < 10){
            s = "0" + second;
        }else if (second > 10){
            s = second + "";
        }
        return h+m+s;
    }
}

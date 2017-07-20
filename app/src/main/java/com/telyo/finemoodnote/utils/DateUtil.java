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

}
